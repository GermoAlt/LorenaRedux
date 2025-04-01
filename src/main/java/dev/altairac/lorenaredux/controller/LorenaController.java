package dev.altairac.lorenaredux.controller;

import dev.altairac.lorenaredux.listener.BaseListener;
import dev.altairac.lorenaredux.listener.MessageListener;
import dev.altairac.lorenaredux.listener.ReactionListener;
import dev.altairac.lorenaredux.listener.SlashCommandsListener;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
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
    private final SlashCommandsListener slashCommandsListener;

    @Autowired
    public LorenaController(MessageListener messageListener, ReactionListener reactionListener, BaseListener baseListener, SlashCommandsListener slashCommandsListener) {
        this.messageListener = messageListener;
        this.reactionListener = reactionListener;
        this.baseListener = baseListener;
        this.slashCommandsListener = slashCommandsListener;
        this.jda = null;
    }

    @PostConstruct
    public void init() throws InterruptedException {
        jda = JDABuilder.create(token, EnumSet.allOf(GatewayIntent.class))
                .addEventListeners(messageListener, reactionListener, baseListener, slashCommandsListener)
                .build();
        jda.awaitReady();
        slashCommandsListener.registerSlashCommands(jda);
    }

    public JDA getJda() {
        return jda;
    }

    @PreDestroy
    public void destroy() {
        jda.shutdown();
    }
}
