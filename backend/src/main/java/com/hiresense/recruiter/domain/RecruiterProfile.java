package com.hiresense.recruiter.domain;

import com.hiresense.common.persistence.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;

/**
 * Links a recruiter's user account to their company. Maps to the recruiter_profiles table
 * created by migration V7. "Owning recruiter" of a job means companyId equals the job's company id.
 * companyId is null until the recruiter creates their company (FR-001 AC6, FR-020).
 * "One recruiter per company" is a service rule, not a database limit (DD-03).
 */
@Entity
@Table(name = "recruiter_profiles")
public class RecruiterProfile extends AuditableEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /** Only the id of the user account: modules do not hold each other's entities (ADR-0002). */
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    /** Only the id of the company, same reason. Null until the company exists. */
    @Column(name = "company_id")
    private UUID companyId;

    /** Required by JPA/Hibernate; not for application code. */
    protected RecruiterProfile() {
    }

    /** Creates a profile without a company; the company is linked later with assignCompany. */
    public RecruiterProfile(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = Objects.requireNonNull(userId, "userId");
    }

    /**
     * Links this recruiter to a company. The id of an existing company is required: there is no way
     * to "unassign", because the database foreign key and the service rules never need it in the MVP.
     * Whether the company may take another recruiter is decided by the service, not here.
     */
    public void assignCompany(UUID companyId) {
        this.companyId = Objects.requireNonNull(companyId, "companyId");
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public UUID getCompanyId() { return companyId; }
}
