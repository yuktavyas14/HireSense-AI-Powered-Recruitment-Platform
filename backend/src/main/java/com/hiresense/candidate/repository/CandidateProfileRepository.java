package com.hiresense.candidate.repository;

import com.hiresense.candidate.domain.CandidateProfile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access for candidate profiles.
 * A user has at most one profile (UNIQUE user_id), so the lookup by user returns an Optional.
 */
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, UUID> {

    Optional<CandidateProfile> findByUserId(UUID userId);
}
