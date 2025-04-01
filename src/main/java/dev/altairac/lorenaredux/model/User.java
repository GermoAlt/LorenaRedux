package dev.altairac.lorenaredux.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import net.dv8tion.jda.api.entities.Member;

import java.time.LocalDateTime;

@Entity(name = "member")
public class User {
    @Id
    private Long id;
    private String username;
    private LocalDateTime lastAtEveryoneTime;

    public User initFromMember(Member member) {
        this.id = member.getIdLong();
        this.username = member.getUser().getName();
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getLastAtEveryoneTime() {
        return lastAtEveryoneTime;
    }

    public void setLastAtEveryoneTime(LocalDateTime lastAtEveryoneTime) {
        this.lastAtEveryoneTime = lastAtEveryoneTime;
    }
}
