-- Task 1.7: resume metadata. Source: docs/database/database-design.md section 3.7.
-- Only metadata lives here; the file bytes go to private object storage in Phase 4 (D-17).
-- No Resume entity yet: the applications table (V9) needs this table for its foreign key.
CREATE TABLE resumes (
    id                UUID         NOT NULL,
    candidate_id      UUID         NOT NULL,
    label             VARCHAR(100),
    original_filename VARCHAR(255) NOT NULL,
    size_bytes        BIGINT       NOT NULL,
    storage_key       VARCHAR(255) NOT NULL,
    hidden_at         TIMESTAMPTZ,
    created_at        TIMESTAMPTZ  NOT NULL,

    CONSTRAINT pk_resumes              PRIMARY KEY (id),
    CONSTRAINT fk_resumes_candidate    FOREIGN KEY (candidate_id) REFERENCES candidate_profiles (id) ON DELETE RESTRICT,
    CONSTRAINT uq_resumes_storage_key  UNIQUE (storage_key),
    -- Target of the composite foreign key from applications (resume_id, candidate_id), design DD-07.
    CONSTRAINT uq_resumes_id_candidate UNIQUE (id, candidate_id),
    CONSTRAINT ck_resumes_size_bytes   CHECK (size_bytes > 0 AND size_bytes <= 5242880)
);

CREATE INDEX ix_resumes_candidate_id ON resumes (candidate_id);
