package dev.altairac.lorenaredux.listener;

import dev.altairac.lorenaredux.enums.ConversionUnit;
import dev.altairac.lorenaredux.model.Server;
import dev.altairac.lorenaredux.model.User;
import dev.altairac.lorenaredux.service.ConversionService;
import dev.altairac.lorenaredux.service.LoreService;
import dev.altairac.lorenaredux.service.ServerService;
import dev.altairac.lorenaredux.service.UserService;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static dev.altairac.lorenaredux.enums.Role.GULAG;

@Component
public class MessageListener extends ListenerAdapter {

    private final UserService userService;
    private final ServerService serverService;
    private final ConversionService conversionService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final LoreService loreService;

    private String[] lorenaNames = {"lorena", "lorenzo"};
    private final List<Character> specials = Arrays.asList(
            '!', '\\', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '|', '~', '`', '{', '}', '[', ']', ':', ';',
            '"', '\'', '<', '>', '?', '/', ',', '.'
    );

    public MessageListener(UserService userService, ServerService serverService, ConversionService conversionService, LoreService loreService) {
        this.userService = userService;
        this.serverService = serverService;
        this.conversionService = conversionService;
        this.loreService = loreService;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().contains("@everyone")) handleAtEveryone(event);
        if (containsLorenaName(event.getMessage().getContentRaw())) handleLoreEvent(event);
        if (containsUnitForConversion(event.getMessage().getContentRaw())) handleUnitConversion(event);
    }

    private void handleUnitConversion(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getMessage().getType().equals(MessageType.SLASH_COMMAND)) {
            return;
        }
        List<Map.Entry<Double, ConversionUnit>> forConversion = collectUnitsToConvert(event.getMessage().getContentRaw());
        List<String> converted = forConversion.stream()
                .map(entry -> {
                    Double value = entry.getKey();
                    ConversionUnit conversionUnit = entry.getValue();
                    if (conversionUnit != null) {
                        ConversionUnit correspondingConversionUnit = ConversionUnit.corresponding(conversionUnit);
                        if (correspondingConversionUnit != null) {
                            try {
                                return conversionService.convert(conversionUnit, correspondingConversionUnit, value);
                            } catch (Exception e) {
                                logger.warn("ConversionException: {} to {}", conversionUnit, correspondingConversionUnit, e);
                            }
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        String msg = String.join(", ", converted);
        if (!msg.isEmpty()) {
            event.getChannel().sendMessage(msg).queue();
        }
    }

    private List<Map.Entry<Double, ConversionUnit>> collectUnitsToConvert(String input) {
        List<String> tokens = Arrays.stream(input.split("\\s"))
                .map(this::removeTrailingSpecials)
                .flatMap(token -> ConversionUnit.splitTokenAuto(token).stream())
                .filter(token -> !token.isBlank())
                .toList();


        List<Map.Entry<Double, ConversionUnit>> forConversion = new ArrayList<>();
        if (tokens.isEmpty()) return forConversion;

        for (int index = 0; index < tokens.size(); index++) {
            String token = tokens.get(index);

            if (input.isBlank()) {
                return Collections.emptyList(); // Return early for invalid inputs
            }


            if (index > 0
                    && ConversionUnit.AUTO_CONVERSION_NAMES.stream()
                    .anyMatch(unit -> unit.equals(token))
                    && isNumeric(tokens.get(index - 1))
            ) {
                forConversion.add(Map.entry(Double.parseDouble(tokens.get(index - 1)), ConversionUnit.matchAuto(token)));
            } else {
                int finalIndex = index;
                if (index > 1 && ConversionUnit.AUTO_CONVERSION_NAMES.stream()
                        .anyMatch(unit -> unit.equals(tokens.get(finalIndex - 1) + " " + token)
                        && isNumeric(tokens.get(finalIndex - 2)))) {
                    forConversion.add(Map.entry(Double.parseDouble(tokens.get(index - 2)),
                            ConversionUnit.matchAuto(tokens.get(index - 1), token)));
                }
            }
        }
        return forConversion;
    }

    private void handleLoreEvent(MessageReceivedEvent event) {
        loreService.sendRandomLore(event);
    }

    private void handleAtEveryone(MessageReceivedEvent event) {
        User user = userService.findUserById(event.getAuthor().getIdLong())
                .orElse(new User().initFromMember(Objects.requireNonNull(event.getMember())));

        if (user.getLastAtEveryoneTime() != null && (
                user.getLastAtEveryoneTime().getYear() == LocalDateTime.now().getYear()
                        && user.getLastAtEveryoneTime().getMonth() == LocalDateTime.now().getMonth()
        )) {
            Server server = serverService.findServerById(event.getGuild().getIdLong()).get();
            Role role = event.getGuild().getRoleById(server.getManagedRoles().get(GULAG));
            assert role != null;
            event.getGuild().addRoleToMember(event.getAuthor(), role).queue();
        }
        user.setLastAtEveryoneTime(LocalDateTime.now());
        userService.updateUser(user);
    }

    private boolean containsLorenaName(String input) {
        if (input == null || lorenaNames == null) {
            return false;
        }

        // Split input into individual words and check for exact match
        return java.util.Arrays.stream(input.split("\\s+")) // Split input by whitespace
                .anyMatch(word -> java.util.Arrays.stream(lorenaNames)
                        .anyMatch(word::equalsIgnoreCase)) || input.toLowerCase().contains("!lorebot dolore");
    }


    private boolean containsUnitForConversion(String input) {
     return !collectUnitsToConvert(input).isEmpty();
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String removeTrailingSpecials(String input) {
        if (input.isEmpty() || !specials.contains(input.charAt(input.length() - 1))) {
            return input;
        } else {
            return removeTrailingSpecials(input.substring(0, input.length() - 1));
        }
    }



}
