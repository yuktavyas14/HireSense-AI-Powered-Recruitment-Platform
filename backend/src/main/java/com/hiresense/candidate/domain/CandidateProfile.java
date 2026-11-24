package com.hiresense.candidate.domain;

import com.hiresense.common.persistence.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 * Extra details about a candidate. Maps to the candidate_profiles table created by migration V6.
 * Name and email live on the user account; everything here is optional (D-13, FR-010).
 * The profile belongs to exactly one user, enforced by the UNIQUE constraint in the database.
 */
@Entity
@Table(name = "candidate_profiles")
public class CandidateProfile extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /** Only the id of the user account: modules do not hold each other's entities (ADR-0002). */
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "location", length = 150)
    private String location;

    @Column(name = "headline", length = 200)
    private String headline;

    @Column(name = "summary", columnDefinition = "text")
    private String summary;

    @Column(name = "experience_summary", columnDefinition = "text")
    private String experienceSummary;

    @Column(name = "education_summary", columnDefinition = "text")
    private String educationSummary;

    /** Required by JPA/Hibernate; not for application code. */
    protected CandidateProfile() {
    }

    /** Creates an empty profile for a user; the optional details are filled in later. */
    public CandidateProfile(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getPhone() { return phone; }
    public String getLocation() { return location; }
    public String getHeadline() { return headline; }
    public String getSummary() { return summary; }
    public String getExperienceSummary() { return experienceSummary; }
    public String getEducationSummary() { return educationSummary; }
}
