package dev.altairac.lorenaredux.service;

import dev.altairac.lorenaredux.enums.ChannelType;
import dev.altairac.lorenaredux.enums.ServerThreshold;
import dev.altairac.lorenaredux.model.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SlashCommandsService {
    private final ServerService serverService;

    @Autowired
    public SlashCommandsService(ServerService serverService) {
        this.serverService = serverService;
    }
    public void handleChannelIdChange(SlashCommandInteractionEvent event, ChannelType channelType) {
        Server server = serverService.findServerById(Objects.requireNonNull(event.getGuild()).getIdLong()).orElseThrow();
        Objects.requireNonNull(server.getChannelIds().put(channelType, Objects.requireNonNull(event.getOption("channel")).getAsLong()));
        serverService.updateServer(server);
        event.getHook().sendMessage("Channel ID changed").setEphemeral(true).queue();
    }

    public void handleThresholdChange(SlashCommandInteractionEvent event, ServerThreshold serverThreshold) {
        Server server = serverService.findServerById(Objects.requireNonNull(event.getGuild()).getIdLong()).orElseThrow();
        Objects.requireNonNull(server.getServerThresholds().put(serverThreshold, Objects.requireNonNull(event.getOption("threshold")).getAsInt()));
        serverService.updateServer(server);
        event.getHook().sendMessage("Threshold changed").setEphemeral(true).queue();
    }

    public void handleSuggestion(SlashCommandInteractionEvent event) {
        Server server = serverService.findServerById(event.getGuild().getIdLong()).orElseThrow();
        Long suggestionChannel = server.getChannelIds().get(ChannelType.SUGGESTIONS);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Suggestion");
        embedBuilder.setDescription(event.getOption("suggestion").getAsString());
        Message message = event.getGuild().getTextChannelById(suggestionChannel)
                .sendMessageEmbeds(embedBuilder.build())
                .complete();
        message.addReaction(Emoji.fromUnicode("👍")).queue();
        message.addReaction(Emoji.fromUnicode("👎")).queue();

    }

    public void handlePing(SlashCommandInteractionEvent event) {
        event.getHook().sendMessage("Pong!").setEphemeral(true).queue();
    }
}
