package dev.altairac.lorenaredux.listener;

import dev.altairac.lorenaredux.enums.ServerThreshold;
import dev.altairac.lorenaredux.model.Server;
import dev.altairac.lorenaredux.repository.ServerRepository;
import dev.altairac.lorenaredux.service.ReactionService;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class ReactionListener extends ListenerAdapter {
    private final ReactionService reactionService;
    private final ServerRepository serverRepository;

    public ReactionListener(ReactionService reactionService, ServerRepository serverRepository) {
        this.reactionService = reactionService;
        this.serverRepository = serverRepository;
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        Optional<ServerThreshold> threshold = Arrays.stream(ServerThreshold.values())
                .filter(t -> t.getEmoji().equals(event.getEmoji().getAsReactionCode()))
                .findFirst();
        if (threshold.isEmpty()) return;
        if (thresholdMet(event, threshold.get())) reactionService.handleReaction(threshold.get(), event);
    }

    private boolean thresholdMet(MessageReactionAddEvent event, ServerThreshold threshold) {
        Server server = serverRepository.findById(event.getGuild().getIdLong()).orElseThrow();
        return event.getReaction().getCount() >= server.getServerThresholds().get(threshold);
    }
}
