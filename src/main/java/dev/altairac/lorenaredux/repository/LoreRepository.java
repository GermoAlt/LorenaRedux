package dev.altairac.lorenaredux.repository;

import dev.altairac.lorenaredux.model.Lore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoreRepository extends JpaRepository<Lore, Long> {
    @Query("SELECT l FROM Lore l ORDER BY RAND() LIMIT 1")
    Lore findRandom();
}
