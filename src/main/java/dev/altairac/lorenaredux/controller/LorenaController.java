package dev.altairac.lorenaredux.controller;

import dev.altairac.lorenaredux.listener.BaseListener;
import dev.altairac.lorenaredux.listener.MessageListener;
import dev.altairac.lorenaredux.listener.ReactionListener;
import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class LorenaController {
    private JDA jda;
    @Value( "${lorena.token}")
    private String token;

    private final MessageListener messageListener;
    private final ReactionListener reactionListener;
    private final BaseListener baseListener;

    @Autowired
    public LorenaController(MessageListener messageListener, ReactionListener reactionListener, BaseListener baseListener) {
        this.messageListener = messageListener;
        this.reactionListener = reactionListener;
        this.baseListener = baseListener;
        this.jda = null;
    }

    @PostConstruct
    public void init() {
        jda = JDABuilder.create(token, EnumSet.allOf(GatewayIntent.class))
                .addEventListeners(messageListener, reactionListener, baseListener)
                .build();
    }
}
