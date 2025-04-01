package dev.altairac.lorenaredux.service;

import dev.altairac.lorenaredux.enums.ServerThreshold;
import dev.altairac.lorenaredux.model.Server;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static dev.altairac.lorenaredux.enums.Role.GULAG;

@Service
public class ReactionService {
    private final LoreService loreService;
    private final ServerService serverService;

    @Autowired
    public ReactionService(LoreService loreService, ServerService serverService) {
        this.loreService = loreService;
        this.serverService = serverService;
    }

    public void handleReaction(ServerThreshold threshold, MessageReactionAddEvent event) {
        Optional<Server> server = serverService.findServerById(event.getGuild().getIdLong());
        if(server.isEmpty()) return;
        switch(threshold){
            case LORE_THRESHOLD -> loreService.handleLore(event, server.get());
            case GULAG_THRESHOLD -> sendUserToGulag(event, server.get());
            case GULAG_REMOVAL_THRESHOLD -> removeUserFromGulag(event, server.get());
        }
    }

    private void sendUserToGulag(MessageReactionAddEvent event, Server server) {
        User member = event.getUser();
        Role gulagRole = event.getGuild().getRoleById(server.getManagedRoles().get(GULAG));
        if(gulagRole == null || member == null) return;
        event.getGuild().addRoleToMember(member, gulagRole).queue();
    }

    private void removeUserFromGulag(MessageReactionAddEvent event, Server server) {
        User member = event.getUser();
        Role gulagRole = event.getGuild().getRoleById(server.getManagedRoles().get(GULAG));
        if(gulagRole == null || member == null) return;
        event.getGuild().removeRoleFromMember(member, gulagRole).queue();

    }
}
