package dev.altairac.lorenaredux.controller;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class LorenaController {
    private JDA jda;
    @Value( "${lorena.token}")
    private String token;

    @PostConstruct
    public void init() {
        jda = JDABuilder.create(token, EnumSet.allOf(GatewayIntent.class)).build();
    }
}
