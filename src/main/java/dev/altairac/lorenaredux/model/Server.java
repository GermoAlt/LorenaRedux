package dev.altairac.lorenaredux.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Server {
    @Id
    private Long id;
    private String name;
    private Long suggestionChannelId;
}
