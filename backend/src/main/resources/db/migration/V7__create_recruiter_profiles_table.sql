-- Task 1.5: recruiter profiles. Source: docs/database/database-design.md section 3.3.
-- A recruiter's link to a company. company_id stays NULL until the recruiter creates the company.
-- No UNIQUE on company_id: "one recruiter per company" is a service rule (DD-03, section 9).
-- version is an addition to the design document (owner decision 2026-11-24) so the entity can extend AuditableEntity.
CREATE TABLE recruiter_profiles (
    id          UUID        NOT NULL,
    user_id     UUID        NOT NULL,
    company_id  UUID,
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ NOT NULL,
    version     BIGINT      NOT NULL DEFAULT 0,

    CONSTRAINT pk_recruiter_profiles         PRIMARY KEY (id),
    CONSTRAINT uq_recruiter_profiles_user    UNIQUE (user_id),
    CONSTRAINT fk_recruiter_profiles_user    FOREIGN KEY (user_id)    REFERENCES users (id)     ON DELETE RESTRICT,
    CONSTRAINT fk_recruiter_profiles_company FOREIGN KEY (company_id) REFERENCES companies (id) ON DELETE RESTRICT
);

CREATE INDEX ix_recruiter_profiles_company_id ON recruiter_profiles (company_id);
