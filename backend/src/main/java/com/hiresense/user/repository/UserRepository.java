package com.hiresense.user.repository;

import com.hiresense.user.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access for user accounts.
 * Emails are stored in lowercase, so callers must pass a lowercase email.
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
