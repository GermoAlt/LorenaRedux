package dev.altairac.lorenaredux.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Lore {
    @Id
    private Long messageId;
}
