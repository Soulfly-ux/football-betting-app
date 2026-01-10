package _bg.footballbettingapp.user.repository;

import _bg.footballbettingapp.user.model.Role;
import _bg.footballbettingapp.user.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {


    Optional<User> findByUsername(@NotNull String username);

    Optional<User> findByEmail(@NotNull String email);

    boolean existsByRole(Role role);

    long countByRole(Role role);

    Role role(Role role);

    long countByIsActive(boolean b);


}
