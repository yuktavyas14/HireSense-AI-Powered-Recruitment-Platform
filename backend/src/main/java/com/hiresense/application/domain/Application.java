package com.hiresense.application.domain;

import com.hiresense.common.persistence.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;

/**
 * One candidate's application to one job, with the resume chosen for it.
 * Maps to the applications table created by migration V9.
 * Holds only the current status; the history of changes is a separate table (Task 1.9).
 * The database guarantees: no second application by the same candidate to the same job,
 * and the resume belongs to the applicant (composite foreign key).
 */
@Entity
@Table(name = "applications")
public class Application extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /** Only the ids of other modules' entities, never the entities themselves (ADR-0002). */
    @Column(name = "job_id", nullable = false, updatable = false)
    private UUID jobId;

    @Column(name = "candidate_id", nullable = false, updatable = false)
    private UUID candidateId;

    @Column(name = "resume_id", nullable = false, updatable = false)
    private UUID resumeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 12)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    /** Required by JPA/Hibernate; not for application code. */
    protected Application() {
    }

    /** A new application always starts as APPLIED. */
    public Application(UUID jobId, UUID candidateId, UUID resumeId) {
        this.id = UUID.randomUUID();
        this.jobId = Objects.requireNonNull(jobId, "jobId");
        this.candidateId = Objects.requireNonNull(candidateId, "candidateId");
        this.resumeId = Objects.requireNonNull(resumeId, "resumeId");
    }

    public UUID getId() { return id; }
    public UUID getJobId() { return jobId; }
    public UUID getCandidateId() { return candidateId; }
    public UUID getResumeId() { return resumeId; }
    public ApplicationStatus getStatus() { return status; }
}
