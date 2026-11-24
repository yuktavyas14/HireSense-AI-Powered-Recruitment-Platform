-- Task 1.5: candidate profiles. Source: docs/database/database-design.md section 3.2.
CREATE TABLE candidate_profiles (
    id                 UUID         NOT NULL,
    user_id            UUID         NOT NULL,
    phone              VARCHAR(30),
    location           VARCHAR(150),
    headline           VARCHAR(200),
    summary            TEXT,
    experience_summary TEXT,
    education_summary  TEXT,
    created_at         TIMESTAMPTZ  NOT NULL,
    updated_at         TIMESTAMPTZ  NOT NULL,
    version            BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT pk_candidate_profiles      PRIMARY KEY (id),
    CONSTRAINT uq_candidate_profiles_user UNIQUE (user_id),
    CONSTRAINT fk_candidate_profiles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT
);
