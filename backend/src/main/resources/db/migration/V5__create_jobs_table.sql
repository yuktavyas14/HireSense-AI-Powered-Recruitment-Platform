-- Task 1.6: jobs table. Source: docs/database/database-design.md section 3.5.
CREATE TABLE jobs (
    id              UUID         NOT NULL,
    company_id      UUID         NOT NULL,
    title           VARCHAR(200) NOT NULL,
    description     TEXT         NOT NULL,
    location        VARCHAR(150),
    employment_type VARCHAR(20)  NOT NULL,
    status          VARCHAR(10)  NOT NULL DEFAULT 'OPEN',
    closed_at       TIMESTAMPTZ,
    created_at      TIMESTAMPTZ  NOT NULL,
    updated_at      TIMESTAMPTZ  NOT NULL,
    version         BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT pk_jobs PRIMARY KEY (id),
    CONSTRAINT fk_jobs_company FOREIGN KEY (company_id) REFERENCES companies (id) ON DELETE RESTRICT,
    CONSTRAINT ck_jobs_employment_type CHECK (employment_type IN ('FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERNSHIP')),
    CONSTRAINT ck_jobs_status CHECK (status IN ('OPEN', 'CLOSED')),
    CONSTRAINT ck_jobs_closed_at CHECK ((status = 'CLOSED') = (closed_at IS NOT NULL))
);

CREATE INDEX ix_jobs_company_id ON jobs (company_id);

CREATE INDEX ix_jobs_open_created_at ON jobs (created_at DESC) WHERE status = 'OPEN';
