package com.hiresense.recruiter.repository;

import com.hiresense.recruiter.domain.RecruiterProfile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access for recruiter profiles.
 * A user has at most one profile (UNIQUE user_id), so the lookup by user returns an Optional.
 * No lookup by company yet: no caller needs it (queries come with the tasks that use them).
 */
public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile, UUID> {

    Optional<RecruiterProfile> findByUserId(UUID userId);
}
