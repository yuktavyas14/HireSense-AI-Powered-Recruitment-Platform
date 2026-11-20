-- Task 1.6: companies table. Source: docs/database/database-design.md section 3.4.
CREATE TABLE companies (
    id          UUID         NOT NULL,
    name        VARCHAR(150) NOT NULL,
    description TEXT,
    website     VARCHAR(255),
    location    VARCHAR(150),
    industry    VARCHAR(100),
    created_at  TIMESTAMPTZ  NOT NULL,
    updated_at  TIMESTAMPTZ  NOT NULL,
    version     BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT pk_companies PRIMARY KEY (id)
);
