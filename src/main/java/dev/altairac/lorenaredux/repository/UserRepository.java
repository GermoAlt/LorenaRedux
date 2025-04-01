package dev.altairac.lorenaredux.repository;

import dev.altairac.lorenaredux.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
