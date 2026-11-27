-- Task 1.7: applications. Source: docs/database/database-design.md section 3.6.
-- One row per candidate per job. status is the CURRENT status only; the story of changes goes
-- into application_status_history (added in Task 1.9). Transition rules live in the service layer.
CREATE TABLE applications (
    id            UUID        NOT NULL,
    job_id        UUID        NOT NULL,
    candidate_id  UUID        NOT NULL,
    resume_id     UUID        NOT NULL,
    status        VARCHAR(12) NOT NULL DEFAULT 'APPLIED',
    created_at    TIMESTAMPTZ NOT NULL,
    updated_at    TIMESTAMPTZ NOT NULL,
    version       BIGINT      NOT NULL DEFAULT 0,

    CONSTRAINT pk_applications               PRIMARY KEY (id),
    -- FR-032 AC3: no duplicate applications, also under concurrent requests.
    CONSTRAINT uq_applications_job_candidate UNIQUE (job_id, candidate_id),
    -- D-16: a job that has applications cannot be deleted.
    CONSTRAINT fk_applications_job           FOREIGN KEY (job_id)       REFERENCES jobs (id)               ON DELETE RESTRICT,
    CONSTRAINT fk_applications_candidate     FOREIGN KEY (candidate_id) REFERENCES candidate_profiles (id) ON DELETE RESTRICT,
    -- FR-032 AC6 / DD-07: the resume must belong to the applicant. Both columns are NOT NULL on
    -- purpose: with a NULL, PostgreSQL (MATCH SIMPLE) would skip this check. D-15: resume is mandatory.
    CONSTRAINT fk_applications_resume        FOREIGN KEY (resume_id, candidate_id) REFERENCES resumes (id, candidate_id) ON DELETE RESTRICT,
    CONSTRAINT ck_applications_status        CHECK (status IN ('APPLIED', 'SHORTLISTED', 'INTERVIEW', 'OFFER', 'ACCEPTED', 'DECLINED', 'REJECTED'))
);

-- "My applications" (candidate) and "applicants of a job, by status" (recruiter).
-- UNIQUE (job_id, candidate_id) already serves lookups that start with job_id alone.
CREATE INDEX ix_applications_candidate_id ON applications (candidate_id);
CREATE INDEX ix_applications_job_status   ON applications (job_id, status);
