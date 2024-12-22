package pl.tazz.ideas.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.tazz.ideas.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByVerifyEmailToken(String token);

    boolean existsByUsername(String username); // Umożliwia szybkie sprawdzenie nazwy użytkownika
    boolean existsByEmail(String email);      // Umożliwia szybkie sprawdzenie e-maila
}


