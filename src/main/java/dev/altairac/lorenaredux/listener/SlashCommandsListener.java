package dev.altairac.lorenaredux.listener;

import dev.altairac.lorenaredux.enums.ChannelType;
import dev.altairac.lorenaredux.enums.ServerThreshold;
import dev.altairac.lorenaredux.enums.SlashCommand;
import dev.altairac.lorenaredux.enums.ConversionUnit;
import dev.altairac.lorenaredux.model.Server;
import dev.altairac.lorenaredux.service.ConversionService;
import dev.altairac.lorenaredux.service.ServerService;
import dev.altairac.lorenaredux.service.SlashCommandsService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static dev.altairac.lorenaredux.enums.ChannelType.LORE;
import static dev.altairac.lorenaredux.enums.ChannelType.SUGGESTIONS;
import static dev.altairac.lorenaredux.enums.ServerThreshold.*;

@Component
public class SlashCommandsListener extends ListenerAdapter {
    private final ServerService serverService;
    private final ConversionService conversionService;
    private final SlashCommandsService slashCommandsService;

    @Autowired
    private SlashCommandsListener(ServerService serverService, ConversionService conversionService, SlashCommandsService slashCommandsService) {
        this.serverService = serverService;
        this.conversionService = conversionService;
        this.slashCommandsService = slashCommandsService;
    }

    public void registerSlashCommands(JDA jda) {
        List<SlashCommandData> commandList = Arrays.stream(SlashCommand.values()).map(SlashCommand::getCommandData).toList();
        for(Server server : serverService.findAllServers()) {
            jda.getGuildById(server.getId()).updateCommands().addCommands(commandList).queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        SlashCommand slashCommand = Arrays.stream(SlashCommand.values())
                .filter(cmd -> cmd.getName().equals(event.getName()))
                .findFirst()
                .orElseThrow();


        switch (slashCommand) {
            case PING -> handlePing(event);
            case SET_LORE_THRESHOLD -> handleThresholdChange(event, LORE_THRESHOLD);
            case SET_GULAG_THRESHOLD -> handleThresholdChange(event, GULAG_THRESHOLD);
            case SET_GULAG_REMOVAL_THRESHOLD -> handleThresholdChange(event, GULAG_REMOVAL_THRESHOLD);
            case SET_LORE_CHANNEL_ID -> handleChannelIdChange(event, LORE);
            case SET_SUGGESTION_CHANNEL_ID -> handleChannelIdChange(event, SUGGESTIONS);
            case CONVERT_UNITS -> handleUnitConversion(event);
            case SUGGEST -> handleSuggestion(event);
        }
    }

    private void handlePing(SlashCommandInteractionEvent event) {
        slashCommandsService.handlePing(event);
    }

    private void handleSuggestion(SlashCommandInteractionEvent event) {
        slashCommandsService.handleSuggestion(event);
    }

    private void handleUnitConversion(SlashCommandInteractionEvent event) {
        ConversionUnit from = ConversionUnit.match(Objects.requireNonNull(event.getOption("unit")).getAsString());
        ConversionUnit to = ConversionUnit.match(Objects.requireNonNull(event.getOption("to")).getAsString());
        double value = Objects.requireNonNull(event.getOption("value")).getAsDouble();
        String result = conversionService.convert(from, to, value);
        event.getHook().sendMessage(result).setEphemeral(true).queue();
    }

    private void handleChannelIdChange(SlashCommandInteractionEvent event, ChannelType channelType) {
        slashCommandsService.handleChannelIdChange(event, channelType);
    }

    private void handleThresholdChange(SlashCommandInteractionEvent event, ServerThreshold serverThreshold) {
        slashCommandsService.handleThresholdChange(event, serverThreshold);

    }
}
