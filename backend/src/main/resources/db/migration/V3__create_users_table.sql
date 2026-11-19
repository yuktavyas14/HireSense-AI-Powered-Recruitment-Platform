-- Task 1.5: accounts table. Source: docs/database/database-design.md section 3.1.
CREATE TABLE users (
    id            UUID         NOT NULL,
    email         VARCHAR(254) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(150) NOT NULL,
    role          VARCHAR(20)  NOT NULL,
    enabled       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ  NOT NULL,
    updated_at    TIMESTAMPTZ  NOT NULL,
    version       BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT pk_users                 PRIMARY KEY (id),
    CONSTRAINT uq_users_email           UNIQUE (email),
    CONSTRAINT ck_users_email_lowercase CHECK (email = lower(email)),
    CONSTRAINT ck_users_role            CHECK (role IN ('CANDIDATE', 'RECRUITER', 'ADMIN'))
);
