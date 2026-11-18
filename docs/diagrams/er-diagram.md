# HireSense – ER Diagram (initial design)

> **Version:** 0.3 (Task 0.6; **final sign-off given by the owner**; verification limitations below remain open)
> **Detail:** column types, constraints, indexes and the reasoning are in `docs/database/database-design.md`.
> **Status:** design only. **No tables exist yet**; migrations are written in Phase 1.
> **Source:** this file is the editable source (Mermaid text), which GitHub renders as a picture and which shows readable diffs in Git.
>
> **Verification status**
> - **Syntax: verified.** Mermaid's own parser (`mermaid` 11.4.1) accepted this diagram as an ER diagram, and a deliberately broken copy was rejected.
> - **Visual rendering: UNVERIFIED.** The diagram was not drawn in a browser or viewer in this session. Check how it looks on GitHub or in a Mermaid preview.
> - **Schema logic: preliminary check on PostgreSQL 16.4** (scratch, in memory, 48 of 48 checks; see section 14 of the database design). **Preliminary:** it must be repeated against the PostgreSQL version chosen in Phase 1.
> - **Flyway migrations and JPA mappings: not yet written or tested.**

---

## 1. Diagram

```mermaid
erDiagram
    USERS ||--o| CANDIDATE_PROFILES : "is a (role CANDIDATE)"
    USERS ||--o| RECRUITER_PROFILES : "is a (role RECRUITER)"
    COMPANIES |o--o{ RECRUITER_PROFILES : "employs"
    COMPANIES ||--o{ JOBS : "posts"
    JOBS ||--o{ APPLICATIONS : "receives"
    CANDIDATE_PROFILES ||--o{ APPLICATIONS : "submits"
    CANDIDATE_PROFILES ||--o{ RESUMES : "owns"
    RESUMES ||--o{ APPLICATIONS : "is attached to"
    APPLICATIONS ||--|{ APPLICATION_STATUS_HISTORY : "is recorded in"
    USERS ||--o{ APPLICATION_STATUS_HISTORY : "changed by"
    USERS ||--o{ AUDIT_LOG : "performed by"
    JOBS ||--o{ JOB_SKILLS : "requires"
    SKILLS ||--o{ JOB_SKILLS : "is required by"
    CANDIDATE_PROFILES ||--o{ CANDIDATE_SKILLS : "has"
    SKILLS ||--o{ CANDIDATE_SKILLS : "is held by"

    USERS {
        uuid id PK
        varchar email UK "stored lowercase"
        varchar password_hash "BCrypt hash only"
        varchar full_name
        varchar role "CANDIDATE, RECRUITER, ADMIN"
        boolean enabled
        timestamptz created_at
        timestamptz updated_at
        bigint version
    }

    CANDIDATE_PROFILES {
        uuid id PK
        uuid user_id FK, UK
        varchar phone "optional"
        varchar location "optional"
        varchar headline "optional"
        text summary "optional"
        text experience_summary "optional"
        text education_summary "optional"
        timestamptz created_at
        timestamptz updated_at
        bigint version
    }

    RECRUITER_PROFILES {
        uuid id PK
        uuid user_id FK, UK
        uuid company_id FK "null until company created"
        timestamptz created_at
        timestamptz updated_at
    }

    COMPANIES {
        uuid id PK
        varchar name
        text description
        varchar website
        varchar location
        varchar industry
        timestamptz created_at
        timestamptz updated_at
        bigint version
    }

    JOBS {
        uuid id PK
        uuid company_id FK
        varchar title
        text description
        varchar location
        varchar employment_type
        varchar status "OPEN or CLOSED"
        timestamptz closed_at "set when CLOSED"
        timestamptz created_at
        timestamptz updated_at
        bigint version
    }

    APPLICATIONS {
        uuid id PK
        uuid job_id FK
        uuid candidate_id FK
        uuid resume_id FK "mandatory"
        varchar status "7 values"
        timestamptz created_at
        timestamptz updated_at
        bigint version
    }

    RESUMES {
        uuid id PK
        uuid candidate_id FK
        varchar label "optional"
        varchar original_filename
        bigint size_bytes "max 5 MB"
        varchar storage_key UK "opaque object name, never a URL"
        timestamptz hidden_at "null means active"
        timestamptz created_at
    }

    APPLICATION_STATUS_HISTORY {
        bigint id PK
        uuid application_id FK
        varchar from_status "null for first row"
        varchar to_status
        uuid changed_by_user_id FK
        text reason "mandatory for reject and reopen"
        timestamptz created_at
    }

    AUDIT_LOG {
        bigint id PK
        uuid actor_user_id FK
        varchar action "e.g. JOB_DELETED"
        varchar entity_type
        uuid entity_id "plain value, no FK"
        varchar entity_label "plain value, no FK"
        timestamptz created_at
    }

    SKILLS {
        bigint id PK
        varchar name UK "case-insensitive unique"
    }

    JOB_SKILLS {
        uuid job_id PK, FK
        bigint skill_id PK, FK
    }

    CANDIDATE_SKILLS {
        uuid candidate_id PK, FK
        bigint skill_id PK, FK
    }
```

---

## 2. How to read it

| Symbol | Meaning |
|--------|---------|
| `\|\|` | exactly one |
| `o\|` or `\|o` | zero or one |
| `o{` | zero or many |
| `\|{` | one or many |
| `PK` / `FK` / `UK` | primary key / foreign key / unique key |

Examples:
- `USERS ||--o| CANDIDATE_PROFILES`: every candidate profile belongs to exactly one user; a user has zero or one candidate profile (only candidates have one).
- `JOBS ||--o{ APPLICATIONS`: a job has zero or many applications; an application belongs to exactly one job.
- `APPLICATIONS ||--|{ APPLICATION_STATUS_HISTORY`: every application has at least one history row (the creation row).
- `COMPANIES |o--o{ RECRUITER_PROFILES`: a company has zero or many recruiters; a recruiter has zero or one company (none until they create it).

## 3. The six roadmap entities and what was added

| Roadmap entity | Table |
|----------------|-------|
| `User` | `users` |
| `CandidateProfile` | `candidate_profiles` |
| `Company` | `companies` |
| `Job` | `jobs` |
| `Application` | `applications` |
| `Resume` | `resumes` |

**Added** (needed by approved requirements): `recruiter_profiles`, `application_status_history` (D-02, D-12), `audit_log` (D-12), and `skills` with the junction tables `job_skills` and `candidate_skills` (approved DD-04). 12 tables in total.

**MVP rule vs schema:** the diagram allows many recruiters per company (`COMPANIES |o--o{ RECRUITER_PROFILES`), but the **MVP business rule is one recruiter per company**, enforced in the service layer. The diagram shows what the schema can hold, not what the product allows today.

## 4. What the diagram cannot show

Rules enforced in the service layer or by constraints, listed in `docs/database/database-design.md`: the allowed status transitions, the limit of 5 active resumes per candidate, one role per user, and "a recruiter cannot post a job before a company exists".
