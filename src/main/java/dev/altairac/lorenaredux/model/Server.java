package dev.altairac.lorenaredux.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

@Entity
public class Server {
    @Id
    private Long id;
    private String name;
    private Long suggestionChannelId;
    private Long loreChannelId;

    public Server initializeFromGuild(Guild guild) {
        this.id = guild.getIdLong();
        this.name = guild.getName();
        this.suggestionChannelId = findChannelId(guild, "suggestions");
        this.loreChannelId = findChannelId(guild, "lore");
        return this;
    }

    private Long findChannelId(Guild guild, String channelName) {
        return guild.getTextChannelsByName(channelName, true).stream().findFirst()
                .map(TextChannel::getIdLong)
                .orElse(null);
    }
}