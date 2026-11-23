package com.hiresense.job.domain;

import com.hiresense.common.persistence.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A job posting. Maps to the jobs table created by migration V5.
 * status and closedAt are changed only through close() and reopen(),
 * because the database CHECK ck_jobs_closed_at requires them to agree.
 */
@Entity
@Table(name = "jobs")
public class Job extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /** Only the id of the company: modules do not hold each other's entities (ADR-0002). */
    @Column(name = "company_id", nullable = false, updatable = false)
    private UUID companyId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "location", length = 150)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false, length = 20)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private JobStatus status = JobStatus.OPEN;

    @Column(name = "closed_at")
    private Instant closedAt;

    /** Required by JPA/Hibernate; not for application code. */
    protected Job() {
    }

    public Job(UUID companyId, String title, String description,
               String location, EmploymentType employmentType) {
        this.id = UUID.randomUUID();
        this.companyId = companyId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.employmentType = employmentType;
    }

    /**
     * Stops accepting applications. Sets status and closedAt together.
     * Idempotent: if the job is already CLOSED, nothing changes and the
     * original closedAt is kept.
     *
     * @throws NullPointerException if at is null (checked first, even when already closed)
     */
    public void close(Instant at) {
        Objects.requireNonNull(at, "Close timestamp must not be null");
        if (status == JobStatus.CLOSED) {
            return;
        }
        this.status = JobStatus.CLOSED;
        this.closedAt = at;
    }

    /**
     * Accepts applications again. Clears status and closedAt together.
     * Idempotent: if the job is already OPEN, nothing changes.
     */
    public void reopen() {
        if (status == JobStatus.OPEN) {
            return;
        }
        this.status = JobStatus.OPEN;
        this.closedAt = null;
    }

    public UUID getId() { return id; }
    public UUID getCompanyId() { return companyId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public EmploymentType getEmploymentType() { return employmentType; }
    public JobStatus getStatus() { return status; }
    public Instant getClosedAt() { return closedAt; }
}
