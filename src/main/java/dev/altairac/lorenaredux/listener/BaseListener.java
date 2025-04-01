package dev.altairac.lorenaredux.listener;

import dev.altairac.lorenaredux.service.ServerService;
import dev.altairac.lorenaredux.service.UserService;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseListener extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ServerService serverService;
    private final UserService userService;

    @Autowired
    public BaseListener(ServerService serverService, UserService userService) {
        this.serverService = serverService;
        this.userService = userService;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        serverService.updateServer(event.getGuild());
        userService.updateUser(event.getMember());
        logger.info("Message received: Author: {} | Content: {} {}",
                event.getAuthor().getName(),
                event.getMessage().getContentRaw(),
                !event.getMessage().getAttachments().isEmpty() ? "| Has attachments": ""
        );
    }
}
