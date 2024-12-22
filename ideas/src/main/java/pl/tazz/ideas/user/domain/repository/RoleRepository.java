package pl.tazz.ideas.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import pl.tazz.ideas.user.domain.model.Role;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId)", nativeQuery = true)
        void assignRoleToUser(@Param("userId") UUID userId, @Param("roleId") UUID roleId);

}