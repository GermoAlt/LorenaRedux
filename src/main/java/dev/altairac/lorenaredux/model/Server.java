package dev.altairac.lorenaredux.model;

import dev.altairac.lorenaredux.enums.ChannelType;
import dev.altairac.lorenaredux.enums.Role;
import dev.altairac.lorenaredux.enums.ServerThreshold;
import jakarta.persistence.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

@Entity
public class Server {
    @Id
    private Long id;
    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "server_channels", joinColumns = @JoinColumn(name = "server_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "channel_type")
    @Column(name = "channel_id")
    private Map<ChannelType, Long> channelIds = new EnumMap<>(ChannelType.class);

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "server_thresholds", joinColumns = @JoinColumn(name = "server_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "threshold_label")
    @Column(name = "threshold_value")
    private Map<ServerThreshold, Integer> serverThresholds = new EnumMap<>(ServerThreshold.class);


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "server_roles", joinColumns = @JoinColumn(name = "server_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "role_name")
    @Column(name = "role_id")
    private Map<Role, Long> managedRoles = new EnumMap<>(Role.class);

    public Server initializeFromGuild(Guild guild) {
        this.id = guild.getIdLong();
        this.name = guild.getName();
        Arrays.stream(ChannelType.values()).forEach(v -> addChannelIdToMap(guild, v));
        Arrays.stream(ServerThreshold.values()).forEach(this::addThresholdToMap);
        Arrays.stream(Role.values()).forEach(this::addRoleToMap);
        return this;
    }

    private void addChannelIdToMap(Guild guild, ChannelType channelName) {
        if (channelIds.get(channelName) != null) return;

        Long id = guild.getTextChannelsByName(channelName.label, true).stream().findFirst()
                .map(TextChannel::getIdLong)
                .orElse(null);
        channelIds.put(channelName, id);
    }

    private void addThresholdToMap(ServerThreshold thresholdName) {
        if (serverThresholds.get(thresholdName) != null) return;

        serverThresholds.put(thresholdName, 0);
    }

    private void addRoleToMap(Role role) {
        if (managedRoles.get(role) != null) return;
        managedRoles.put(role, 0L);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<ChannelType, Long> getChannelIds() {
        return channelIds;
    }

    public Map<ServerThreshold, Integer> getServerThresholds() {
        return serverThresholds;
    }

    public Map<Role, Long> getManagedRoles() {
        return managedRoles;
    }
}