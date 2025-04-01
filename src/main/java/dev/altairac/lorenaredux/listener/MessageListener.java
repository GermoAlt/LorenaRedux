package dev.altairac.lorenaredux.listener;

import dev.altairac.lorenaredux.model.Server;
import dev.altairac.lorenaredux.model.User;
import dev.altairac.lorenaredux.service.ServerService;
import dev.altairac.lorenaredux.service.UserService;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static dev.altairac.lorenaredux.enums.Role.GULAG;

@Component
public class MessageListener extends ListenerAdapter {

    private final UserService userService;
    private final ServerService serverService;
    private String[] lorenaNames = {"lorena", "lorenzo"};

    public MessageListener(UserService userService, ServerService serverService) {
        this.userService = userService;
        this.serverService = serverService;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().contains("@everyone")) handleAtEveryone(event);
        if (containsLorenaName(event.getMessage().getContentRaw())) handleLoreEvent(event);
    }

    private void handleLoreEvent(MessageReceivedEvent event) {
    }

    private void handleAtEveryone(MessageReceivedEvent event) {
        User user = userService.findUserById(event.getAuthor().getIdLong())
                .orElse(new User().initFromMember(event.getMember()));

        if (user.getLastAtEveryoneTime() != null && (
                user.getLastAtEveryoneTime().getYear() == LocalDateTime.now().getYear()
                        && user.getLastAtEveryoneTime().getMonth() == LocalDateTime.now().getMonth()
        )) {
            Server server = serverService.findServerById(event.getGuild().getIdLong()).get();
            Role role = event.getGuild().getRoleById(server.getManagedRoles().get(GULAG));
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
                        .anyMatch(word::equalsIgnoreCase));
    }

}
