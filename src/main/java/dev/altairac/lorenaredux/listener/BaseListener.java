package dev.altairac.lorenaredux.listener;

import dev.altairac.lorenaredux.service.ServerConfigService;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseListener extends ListenerAdapter {
    private final ServerConfigService serverConfigService;

    @Autowired
    public BaseListener(ServerConfigService serverConfigService) {
        this.serverConfigService = serverConfigService;
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        serverConfigService.updateServer(event.getGuild());
    }
}
