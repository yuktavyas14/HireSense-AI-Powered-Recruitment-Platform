# HireSense – Project Progress Tracker

> **Project:** HireSense – AI-Powered Recruitment Platform
> **Development Status:** `IN_PROGRESS`
> **Current Phase:** Phase 1 – Core Backend (started by owner decision); Phase 0 remains `IN_PROGRESS` alongside it
> **Current Task:** Task 1.5 – `COMPLETED` (2026-11-27; not `VERIFIED`): User, candidate profile and recruiter profile parts are all implemented and tested (76 tests passing). Next: Task 1.7. Task 1.6 `COMPLETED` (not `VERIFIED`); Task 1.4 `COMPLETED`; Task 1.3 stays `VERIFIED`; Task 1.2 stays `COMPLETED` (owner has not yet run `contextLoads` in IntelliJ)
> **Last Updated:** 2026-11-27 (Task 1.8a started: `JobResponse` and `PageResponse` with 5 unit tests; 103 tests passing)

> **Important:** AI must read this file before starting development and update it after meaningful milestones.
> **Project principle:** Always know what is completed, what is in progress, what is blocked and what comes next.

**Status legend:** `NOT_STARTED` · `IN_PROGRESS` · `BLOCKED` · `COMPLETED` (implemented) · `VERIFIED` (implemented, tested and checked in the running app)

---

## 1. Overall Project Progress

| Phase | Description | Status |
|-------|-------------|--------|
| Phase 0 | Planning and Setup | `IN_PROGRESS` |
| Phase 1 | Core Backend | `IN_PROGRESS` (started 2026-11-18 by owner decision while Phase 0 is open) |
| Phase 2 | Security | `NOT_STARTED` |
| Phase 3 | Angular Frontend | `NOT_STARTED` |
| Phase 4 | Docker and File Uploads | `NOT_STARTED` |
| Phase 5 | Microservices | `NOT_STARTED` |
| Phase 6 | Kafka and Event-Driven Architecture | `NOT_STARTED` |
| Phase 7 | AI Integration | `NOT_STARTED` |
| Phase 8 | Caching and Search | `NOT_STARTED` |
| Phase 9 | Testing and Code Quality | `NOT_STARTED` |
| Phase 10 | Kubernetes | `NOT_STARTED` |
| Phase 11 | CI/CD and Observability | `NOT_STARTED` |
| Phase 12 | Final Polish and Showcase | `NOT_STARTED` |

**Overall Completion:** 0% (0 of 13 phases verified)

---

## 2. Current Development Status

| Item | Value |
|------|-------|
| **Current Phase** | Phase 1 – Core Backend (Phase 0 stays `IN_PROGRESS`; no `phase-0` tag yet) |
| **Current Task** | Task 1.7 – Application entity `COMPLETED` (2026-11-27, not `VERIFIED`): V8 `resumes`, V9 `applications`, `ApplicationStatus`, `Application`, `ApplicationRepository` and `ApplicationRepositoryTest` (21 runs); 98 tests pass. Task 1.5 is `COMPLETED` (not `VERIFIED`). **Task 1.8 `IN_PROGRESS` (2026-11-27), slice 1.8a (public read):** `JobResponse` and `PageResponse` done (5 unit tests, 103 tests pass); next the `JobRepository` search query. Slice 1.8b (recruiter write) still needs C1, C2 and the validation dependency decided first. |
| **Current Objective** | Task 1.5 is finished. Start Task 1.7 (Application entity): explain the concepts, list the files, propose the V8 `applications` migration (and the status history table question), one file at a time with owner approval before each. C1 and C2 must be finalized before Task 1.8. |
| **Current Activity** | Task 1.5 finished 2026-11-27. Schema: Flyway migrations V1–V7 are applied to `hiresense_test` (V1–V2 also to `hiresense`; V3–V7 not yet applied to `hiresense`): baseline, revoke app access to the history table, `users`, `companies`, `jobs`, `candidate_profiles`, `recruiter_profiles`. Code: `Role`, `AuditableEntity`, `JpaAuditingConfig`, `User`, `UserRepository`, `EmploymentType`, `JobStatus`, `Job`, `JobRepository`, `CandidateProfile`, `CandidateProfileRepository`, `RecruiterProfile` (`assignCompany`), `RecruiterProfileRepository` (`findByUserId`). Tests: **76 pass, 0 failures, 0 errors, 0 skipped** (Maven, JDK 21.0.13, 2026-11-27): 13 `FlywayMigrationTest` (expects versions 1 to 7) + 1 application context + 28 `JobRepositoryTest` + 11 `UserRepositoryTest` + 10 `CandidateProfileRepositoryTest` + 13 `RecruiterProfileRepositoryTest`. Uncommitted: `RecruiterProfileRepository`, `RecruiterProfileRepositoryTest`, `database-design.md` and this file (V7, `RecruiterProfile` and the earlier test changes are in `c129f53`). |
| **Blockers** | None for the next step. Owner items (not blocking): check the Maven runner JRE and importer JDK in IntelliJ; run `contextLoads` once in IntelliJ; the two identical run configurations are kept by owner decision (the stale `com.example` configuration no longer exists). This shell's PATH still resolves `java` to 17; Maven runs from the shell need `JAVA_HOME` set to JDK 21 (IntelliJ uses `temurin-21`). |
| **Next Task** | Task 1.8a – the `JobRepository` search query (filters, paging, sort whitelist), then `JobService`, the minimal exception handler and `JobController`. Slice 1.8b needs the owner to finalize C1 (acting user before Phase 2), C2 (endpoint list versus D-03, D-16) and approve `spring-boot-starter-validation` (C5) first. Phase 0 leftovers (0.8 architecture docs + ADR-0001, 0.9 documentation) run alongside Phase 1. |

---

## 3. Phase-Wise Task Tracking

### Phase 0 – Planning and Setup
**Status:** `IN_PROGRESS`

| Task | Description | Status |
|------|-------------|--------|
| 0.1 | Install development tools | `COMPLETED` (Docker deferred to Phase 4; see Known Issues) |
| 0.2 | Create GitHub repository | `VERIFIED` (repo pre-existed; Git email change deferred) |
| 0.3 | Create project folder structure | `VERIFIED` (Option A: kept `backend/` and `ui/`; added `docs/*` and `infra/`) |
| 0.4 | Write project requirements | `COMPLETED` (`docs/requirements.md` v0.6, decisions D-01..D-31 approved; only open question: Q7 retention policy. **Owner committed and pushed the docs in `8162842`**, so the "committed and pushed" condition is met; GitHub issue #17 is still open and awaits the owner's OK to close it) |
| 0.5 | Define user roles and user stories | `VERIFIED` (`docs/user-stories.md` v0.3, approved by owner; traceability checked) |
| 0.6 | Design ER diagram | `COMPLETED` (owner final sign-off 2026-11-18: 12 tables, DD-01..DD-15, DO-1..DO-6 decided. Limitations: diagram not visually rendered; Flyway/JPA untested; PGlite 48/48 results preliminary and to be repeated on the PostgreSQL version chosen in Phase 1) |
| 0.7 | Create GitHub Projects board | `COMPLETED` (**reduced scope**, owner decision 2026-11-18). Done and verified: 13 labels, 13 milestones, 37 issues (#1–#37; #15, #16, #18 closed). **Deferred, must not block development:** GitHub Project, custom fields, views, automation, sub-issue links |
| 0.8 | Create architecture documentation | `NOT_STARTED` (runs alongside Phase 1; Phase 0 stays open until 0.8 and 0.9 are addressed) |
| 0.9 | Prepare project documentation | `NOT_STARTED` (runs alongside Phase 1) |

**Completion Criteria:**
- [ ] Development environment ready.
- [x] GitHub repository created (pre-existing, verified public).
- [x] Requirements documented (`docs/requirements.md` v0.6, `docs/user-stories.md` v0.3).
- [x] ER diagram completed (design signed off; see verification limitations).
- [x] Project structure prepared (`docs/*`, `infra/`).
- [x] Documentation committed after owner approval: requirements, user stories, database design, ER diagram and the `HIRESENSE.md` workflow fix were committed and pushed by the owner in `8162842` (origin/main = HEAD). `PROJECT_PROGRESS.md` itself is not yet committed.
- [ ] Tasks 0.8 and 0.9 done; remaining verification gaps addressed (rendered diagram, Flyway/JPA tests, PostgreSQL re-check); `phase-0` tag only after owner approval.

### Phase 1 – Core Backend
**Status:** `IN_PROGRESS` (started by explicit owner decision while Phase 0 is open)

| Task | Description | Status |
|------|-------------|--------|
| 1.1 | Generate Spring Boot project (**scope: review the existing project; never generate a second one**) | `COMPLETED` (existing project reviewed; base package changed to `com.hiresense`, Maven `groupId` `com.hiresense`, `artifactId` kept `HireSense`; **compiled on JDK 21**; not `VERIFIED` because the app cannot start and its test fails until Task 1.2) |
| 1.2 | Configure PostgreSQL | `COMPLETED` (PostgreSQL 17.12, roles `hiresense_owner`/`hiresense_app` with SCRAM passwords, databases `hiresense`/`hiresense_test`, `PUBLIC` has no database access, default privileges set in both databases; driver 42.7.14, datasource without password via `pgpass.conf`, separate test database; `contextLoads` passes against `hiresense_test` with Maven on JDK 21. **Not `VERIFIED`:** not yet run in IntelliJ; no tables exist, so the app role's table rights are untested until Task 1.3. Correction 2026-11-19: default privileges in `hiresense` were missing until fixed on 2026-11-19; now present in both databases (verified)) |
| 1.3 | Configure Flyway | `VERIFIED` (2026-11-19). Implemented: Spring Boot 4.1.2 with Flyway 12.4.0 (`spring-boot-starter-flyway`, `flyway-database-postgresql`); `ddl-auto=none`; Flyway runs as `hiresense_owner` with the password read from `pgpass.conf` (none in project files); `V1__baseline.sql` and `V2__revoke_app_access_to_flyway_history.sql`; no business tables. Tested (Maven, JDK 21): 14 tests, 0 failures = 13 Flyway migration test cases (`FlywayMigrationTest`) + 1 application context test. Checked with read-only `psql` in `hiresense_test` and `hiresense`: V1 and V2 recorded as successful, history table owned by `hiresense_owner`, `hiresense_app` has none of SELECT/INSERT/UPDATE/DELETE, no PUBLIC grant. Manually verified by the owner in IntelliJ against `hiresense`: first start applied V1 and V2 together, second start "up to date". Owner reported that /actuator/health was working; exact HTTP status and response body were not captured. **Open verification item:** V1 and V2 have not been tested on a completely new, isolated PostgreSQL database |
| 1.4 | Design backend package structure | `COMPLETED` (2026-11-19, documentation only). **Deliverable:** architecture decision record `docs/adr/0002-backend-package-structure.md`, approved by the owner (status Accepted). No Java packages or classes were created. **No implementation, compilation or tests were performed for this task**, so it is not `VERIFIED`. ADR-0002 is uncommitted. C1 and C2 stay provisional until Task 1.8 |
| 1.5 | Implement User entity | `COMPLETED` (2026-11-27; not `VERIFIED`). **2026-11-27:** the User, candidate profile and recruiter profile parts all exist and are tested (76 tests in the full suite). Recruiter part: `V7__create_recruiter_profiles_table.sql`, `RecruiterProfile` (nullable `companyId`, `assignCompany`), `RecruiterProfileRepository` (`findByUserId`), `RecruiterProfileRepositoryTest` (13 tests: both foreign keys, the unique user, RESTRICT on user and company deletion, two recruiters in one company allowed, optimistic locking, null guards). Not verified: V3–V7 on `hiresense`; the app running against `hiresense`; real concurrent transactions. **History of this task:** **Scope note 2026-11-20 (owner decision (a)):** the original criteria (issue #27) also list candidate and recruiter profile tables and tests (optional phone, one profile per user). The User part came first. Not `VERIFIED`. **Implemented and committed (`8634d49`):** `V3__create_users_table.sql` (UUID pk, unique lowercase email, role CHECK, `enabled`, `created_at`/`updated_at`/`version`), `user/domain/Role.java`, `common/persistence/AuditableEntity.java`, `config/JpaAuditingConfig.java`; `FlywayMigrationTest` now expects versions 1, 2, 3. **Tested (Maven, JDK 21.0.13, 2026-11-20):** 14 tests, 0 failures (13 Flyway cases + `contextLoads`); V3 applied to `hiresense_test`. **Added 2026-11-20 (uncommitted):** `user/domain/User.java`, `user/repository/UserRepository.java` (`findByEmail`, `existsByEmail`), `src/test/.../user/repository/UserRepositoryTest.java` (11 tests). **Tested (Maven, JDK 21.0.13):** 25 tests, 0 failures (13 Flyway + 1 context + 11 `UserRepositoryTest`), real PostgreSQL `hiresense_test` as `hiresense_app`, rollback per test; covers read-back, email normalization, audit fields, unique email, role CHECK, lowercase-email CHECK, optimistic locking (merge-time and SQL-level), version 0 to 1, no-change, `updated_at`. **Not verified:** V3 on the development database `hiresense`; leftover-row count in `hiresense_test` (expected 0); the app running with the entity against `hiresense`; Task 1.5 stays `IN_PROGRESS` until you accept it **Update 2026-11-24 (candidate profile migration):** `V6__create_candidate_profiles_table.sql` created (11 columns; `pk_candidate_profiles`, `uq_candidate_profiles_user` UNIQUE on `user_id`, `fk_candidate_profiles_user` to `users(id)` ON DELETE RESTRICT; phone, location, headline and the three summaries nullable; no role CHECK and no phone-format CHECK by design). Applied to `hiresense_test` by running only `HireSenseApplicationTests` ("now at version v6"). Checked read-only as `hiresense_owner`: history ranks 1–6 successful; columns, types, lengths and nullability as approved; `version` default 0; the three constraints and the two automatic unique indexes; `hiresense_app` has SELECT, INSERT, UPDATE and DELETE (catalog check); 0 rows. `FlywayMigrationTest` now expects versions 1 to 6. **Update 2026-11-26 (candidate profile done):** `CandidateProfile.java` and `CandidateProfileRepository.java` (`findByUserId`) were already committed (`7b5c036`, `f5c3d7a`); `CandidateProfileRepositoryTest` added (10 tests, real PostgreSQL `hiresense_test` as `hiresense_app`, rollback per test, guard in `@BeforeEach`, a control statement before each constraint test). Covered: empty profile and `findByUserId`, empty result for a user without a profile, optional-field round trip including 5000-character TEXT, audit fields and version, `uq_candidate_profiles_user` (23505), `fk_candidate_profiles_user` for a nonexistent user (23503), `ON DELETE RESTRICT` when deleting a user with a profile (23503), headline over 200 characters (22001), optimistic locking, version 0 to 1. **Tested:** 10 tests passed alone; full suite 63 tests, 0 failures. **Not done:** everything for `recruiter_profiles`. **Not verified:** V6 on `hiresense`; real concurrent transactions; leftover rows in `hiresense_test` (expected 0). |
| 1.6 | Implement Job entity | `COMPLETED` (2026-11-24; not `VERIFIED`). **Implemented:** `V4__create_companies_table.sql` (table only; no Company entity or repository by owner decision), `V5__create_jobs_table.sql` (11 columns; `pk_jobs`, `fk_jobs_company` ON DELETE RESTRICT, `ck_jobs_employment_type`, `ck_jobs_status`, `ck_jobs_closed_at`; indexes `ix_jobs_company_id` and partial `ix_jobs_open_created_at`), `job/domain/EmploymentType.java`, `job/domain/JobStatus.java`, `job/domain/Job.java` (extends `AuditableEntity`; `companyId` is a plain UUID per ADR-0002; no setters; `close(Instant)` throws `NullPointerException` on null even when already closed and is idempotent, keeping the original `closedAt`; `reopen()` is idempotent), `job/repository/JobRepository.java` (extends `JpaRepository<Job, UUID>`, no custom methods; queries come with Task 1.8), `JobRepositoryTest` (28 cases). **Tested (Maven, JDK 21.0.13, 2026-11-24):** `JobRepositoryTest` 28 tests, 0 failures; full suite 53 tests, 0 failures, 0 errors, BUILD SUCCESS. Real PostgreSQL `hiresense_test` as `hiresense_app`, rollback per test; no H2; a guard checks database, role and the rollback flag before each test. Covered: save and read-back, audit fields and version, all four employment types, `OPEN` and `CLOSED` storage, both invalid-value CHECKs, both invalid `closed_at` combinations, the foreign key to a nonexistent company, company deletion restricted while jobs exist (SQLState 23503), `close()` and `reopen()` including null and repeated calls, and real INSERT, SELECT and UPDATE as `hiresense_app`. **Not verified:** V4 and V5 on the development database `hiresense`; the app running with `Job` against `hiresense`; leftover rows in `hiresense_test` (expected 0); NOT NULL violations on title and description (no validation until Task 1.11); real concurrent transactions and the partial index (Phase 9). |
| 1.7 | Implement Application entity | `COMPLETED` (2026-11-27; not `VERIFIED`; all items below are done, `ApplicationRepository` and `ApplicationRepositoryTest` with 21 runs were added afterwards and the full suite has 98 tests passing; see the changelog entry of 2026-11-27 for the details). Earlier status text: Done: V8 `resumes` table (no entity; Option A) and V9 `applications` (composite FK to `resumes`, `UNIQUE (job_id, candidate_id)`, status CHECK), both applied to `hiresense_test` and checked read-only; 77 tests pass. `ApplicationStatus` and `Application` created and compiled (`mvnw -q compile`, exit 0), not yet tested. Not done: `ApplicationRepository`, `ApplicationRepositoryTest`. `application_status_history` is deferred to Task 1.9. |
| 1.8 | Implement Job CRUD APIs | `IN_PROGRESS` (2026-11-27). Split by owner decision: **1.8a** public read (`GET /api/jobs`, `GET /api/jobs/{id}`) – `JobResponse` and `PageResponse` done with 5 unit tests; repository query, service, exception handler, controller and their tests not done. **1.8b** recruiter write (create, update, status) – not started, needs C1, C2 and the validation dependency. **1.8c** company basics on public jobs – not started. |
| 1.9 | Implement Job Application APIs | `NOT_STARTED` |
| 1.10 | Implement Application Status Management | `NOT_STARTED` |
| 1.11 | Add validation and exception handling | `NOT_STARTED` |
| 1.12 | Configure Swagger/OpenAPI | `NOT_STARTED` |
| 1.13 | Write unit and integration tests | `NOT_STARTED` |

### Phase 2 – Security
**Status:** `NOT_STARTED`

| Task | Description | Status |
|------|-------------|--------|
| 2.1 | Configure Spring Security | `NOT_STARTED` |
| 2.2 | Implement registration | `NOT_STARTED` |
| 2.3 | Implement login | `NOT_STARTED` |
| 2.4 | Implement BCrypt password hashing | `NOT_STARTED` |
| 2.5 | Implement JWT authentication | `NOT_STARTED` |
| 2.6 | Implement refresh tokens | `NOT_STARTED` |
| 2.7 | Implement role-based authorization | `NOT_STARTED` |
| 2.8 | Implement ownership checks | `NOT_STARTED` |
| 2.9 | Configure CORS | `NOT_STARTED` |
| 2.10 | Write security tests | `NOT_STARTED` |

### Phase 3 – Angular Frontend
**Status:** `NOT_STARTED`

| Task | Description | Status |
|------|-------------|--------|
| 3.1 | Create Angular application | `NOT_STARTED` |
| 3.2 | Configure Angular Material and Tailwind | `NOT_STARTED` |
| 3.3 | Create application structure | `NOT_STARTED` |
| 3.4 | Implement login and registration | `NOT_STARTED` |
| 3.5 | Configure routing and guards | `NOT_STARTED` |
| 3.6 | Configure HTTP interceptors | `NOT_STARTED` |
| 3.7 | Implement Candidate Dashboard | `NOT_STARTED` |
| 3.8 | Implement Recruiter Dashboard | `NOT_STARTED` |
| 3.9 | Implement Job Search | `NOT_STARTED` |
| 3.10 | Implement Job Application Flow | `NOT_STARTED` |
| 3.11 | Implement Application Tracking | `NOT_STARTED` |
| 3.12 | Implement state management | `NOT_STARTED` |
| 3.13 | Implement responsive design | `NOT_STARTED` |
| 3.14 | Write frontend tests | `NOT_STARTED` |

### Phase 4 – Docker and File Uploads
**Status:** `NOT_STARTED`

| Task | Description | Status |
|------|-------------|--------|
| 4.1 | Create backend Dockerfile | `NOT_STARTED` |
| 4.2 | Create frontend Dockerfile | `NOT_STARTED` |
| 4.3 | Configure Docker Compose | `NOT_STARTED` |
| 4.4 | Configure PostgreSQL container | `NOT_STARTED` |
| 4.5 | Configure MinIO | `NOT_STARTED` |
| 4.6 | Implement resume upload | `NOT_STARTED` |
| 4.7 | Store file metadata | `NOT_STARTED` |
| 4.8 | Implement secure file download | `NOT_STARTED` |
| 4.9 | Verify container persistence | `NOT_STARTED` |
| 4.10 | Test complete Docker environment | `NOT_STARTED` |

### Phase 5 – Microservices
**Status:** `NOT_STARTED`

| Task | Description | Status |
|------|-------------|--------|
| 5.1 | Define service boundaries | `NOT_STARTED` |
| 5.2 | Extract User Service | `NOT_STARTED` |
| 5.3 | Extract Job Service | `NOT_STARTED` |
| 5.4 | Extract Application Service | `NOT_STARTED` |
| 5.5 | Configure API Gateway | `NOT_STARTED` |
| 5.6 | Configure Keycloak | `NOT_STARTED` |
| 5.7 | Configure service databases | `NOT_STARTED` |
| 5.8 | Implement service communication | `NOT_STARTED` |
| 5.9 | Configure Resilience4j | `NOT_STARTED` |
| 5.10 | Update Docker Compose | `NOT_STARTED` |
| 5.11 | Test microservices communication | `NOT_STARTED` |

### Phase 6 – Kafka and Event-Driven Architecture
**Status:** `NOT_STARTED`

| Task | Description | Status |
|------|-------------|--------|
| 6.1 | Configure Kafka | `NOT_STARTED` |
| 6.2 | Configure Kafka UI | `NOT_STARTED` |
| 6.3 | Implement Kafka producers | `NOT_STARTED` |
| 6.4 | Implement Kafka consumers | `NOT_STARTED` |
| 6.5 | Define event schemas | `NOT_STARTED` |
| 6.6 | Implement application events | `NOT_STARTED` |
| 6.7 | Implement retry and DLQ | `NOT_STARTED` |
| 6.8 | Implement idempotent consumers | `NOT_STARTED` |
| 6.9 | Implement Outbox Pattern | `NOT_STARTED` |
| 6.10 | Create Notification Service | `NOT_STARTED` |
| 6.11 | Implement email notifications | `NOT_STARTED` |
| 6.12 | Implement WebSocket notifications | `NOT_STARTED` |
| 6.13 | Test event-driven workflows | `NOT_STARTED` |

### Phase 7 – AI Integration
**Status:** `NOT_STARTED`

| Task | Description | Status |
|------|-------------|--------|
| 7.1 | Configure Spring AI | `NOT_STARTED` |
| 7.2 | Configure Ollama | `NOT_STARTED` |
| 7.3 | Create AI Service | `NOT_STARTED` |
| 7.4 | Extract PDF resume text | `NOT_STARTED` |
| 7.5 | Implement LLM resume parsing | `NOT_STARTED` |
| 7.6 | Generate resume embeddings | `NOT_STARTED` |
| 7.7 | Generate job embeddings | `NOT_STARTED` |
| 7.8 | Configure pgvector | `NOT_STARTED` |
| 7.9 | Implement semantic matching | `NOT_STARTED` |
| 7.10 | Generate candidate match explanations | `NOT_STARTED` |
| 7.11 | Implement job recommendations | `NOT_STARTED` |
| 7.12 | Generate interview questions | `NOT_STARTED` |
| 7.13 | Implement RAG chatbot | `NOT_STARTED` |
| 7.14 | Integrate chatbot with Angular | `NOT_STARTED` |
| 7.15 | Implement AI safety and evaluation tests | `NOT_STARTED` |

### Phase 8 – Caching and Search
**Status:** `NOT_STARTED`

| Task | Description | Status |
|------|-------------|--------|
| 8.1 | Configure Redis | `NOT_STARTED` |
| 8.2 | Implement caching | `NOT_STARTED` |
| 8.3 | Implement cache invalidation | `NOT_STARTED` |
| 8.4 | Configure Elasticsearch | `NOT_STARTED` |
| 8.5 | Implement job indexing | `NOT_STARTED` |
| 8.6 | Implement full-text search | `NOT_STARTED` |
| 8.7 | Implement autocomplete | `NOT_STARTED` |
| 8.8 | Implement typo tolerance | `NOT_STARTED` |
| 8.9 | Optimize database queries | `NOT_STARTED` |
| 8.10 | Measure performance improvements | `NOT_STARTED` |

### Phase 9 – Testing and Code Quality
**Status:** `NOT_STARTED`

| Task | Description | Status |
|------|-------------|--------|
| 9.1 | Configure Testcontainers | `NOT_STARTED` |
| 9.2 | Integration tests for PostgreSQL | `NOT_STARTED` |
| 9.3 | Integration tests for Kafka | `NOT_STARTED` |
| 9.4 | Integration tests for Redis | `NOT_STARTED` |
| 9.5 | Configure Playwright | `NOT_STARTED` |
| 9.6 | Implement end-to-end tests | `NOT_STARTED` |
| 9.7 | Configure JaCoCo | `NOT_STARTED` |
| 9.8 | Configure SonarQube | `NOT_STARTED` |
| 9.9 | Configure formatting checks | `NOT_STARTED` |
| 9.10 | Verify complete test suite | `NOT_STARTED` |

### Phase 10 – Kubernetes
**Status:** `NOT_STARTED`

| Task | Description | Status |
|------|-------------|--------|
| 10.1 | Configure kind or minikube | `NOT_STARTED` |
| 10.2 | Create Kubernetes Deployments | `NOT_STARTED` |
| 10.3 | Create Kubernetes Services | `NOT_STARTED` |
| 10.4 | Configure Ingress | `NOT_STARTED` |
| 10.5 | Configure ConfigMaps and Secrets | `NOT_STARTED` |
| 10.6 | Configure persistent storage | `NOT_STARTED` |
| 10.7 | Configure health probes | `NOT_STARTED` |
| 10.8 | Configure HPA | `NOT_STARTED` |
| 10.9 | Create Helm charts | `NOT_STARTED` |
| 10.10 | Deploy HireSense | `NOT_STARTED` |
| 10.11 | Test scaling and persistence | `NOT_STARTED` |

### Phase 11 – CI/CD and Observability
**Status:** `NOT_STARTED`

| Task | Description | Status |
|------|-------------|--------|
| 11.1 | Configure GitHub Actions | `NOT_STARTED` |
| 11.2 | Automate backend tests | `NOT_STARTED` |
| 11.3 | Automate frontend tests | `NOT_STARTED` |
| 11.4 | Build Docker images | `NOT_STARTED` |
| 11.5 | Configure GHCR | `NOT_STARTED` |
| 11.6 | Configure deployment pipeline | `NOT_STARTED` |
| 11.7 | Configure Prometheus | `NOT_STARTED` |
| 11.8 | Configure Grafana | `NOT_STARTED` |
| 11.9 | Configure OpenTelemetry | `NOT_STARTED` |
| 11.10 | Implement distributed tracing | `NOT_STARTED` |
| 11.11 | Configure centralized logging | `NOT_STARTED` |
| 11.12 | Create monitoring dashboards | `NOT_STARTED` |

### Phase 12 – Final Polish and Showcase
**Status:** `NOT_STARTED`

| Task | Description | Status |
|------|-------------|--------|
| 12.1 | Finalize README | `NOT_STARTED` |
| 12.2 | Create architecture diagram | `NOT_STARTED` |
| 12.3 | Add application screenshots | `NOT_STARTED` |
| 12.4 | Create demo video or GIF | `NOT_STARTED` |
| 12.5 | Finalize API documentation | `NOT_STARTED` |
| 12.6 | Document architecture decisions | `NOT_STARTED` |
| 12.7 | Prepare demo data | `NOT_STARTED` |
| 12.8 | Prepare deployment | `NOT_STARTED` |
| 12.9 | Prepare resume description | `NOT_STARTED` |
| 12.10 | Prepare LinkedIn project post | `NOT_STARTED` |
| 12.11 | Prepare interview explanations | `NOT_STARTED` |

---

## 4. Completed Features

Keep a record of successfully completed features.

| Feature | Phase | Completion Date | Verification |
|---------|-------|-----------------|--------------|
| None yet | - | - | - |

---

## 5. Learning Tracker

Track concepts that I have studied and understood.

| Concept | Phase | Status |
|---------|-------|--------|
| Java 21 | Phase 0 | `NOT_STARTED` |
| Maven | Phase 0 | `NOT_STARTED` |
| Git and GitHub | Phase 0 | `NOT_STARTED` |
| Spring Boot | Phase 1 | `NOT_STARTED` |
| REST APIs | Phase 1 | `NOT_STARTED` |
| JPA and Hibernate | Phase 1 | `NOT_STARTED` |
| PostgreSQL | Phase 1 | `NOT_STARTED` |
| Spring Security | Phase 2 | `NOT_STARTED` |
| JWT | Phase 2 | `NOT_STARTED` |
| Angular | Phase 3 | `NOT_STARTED` |
| Docker | Phase 4 | `NOT_STARTED` |
| Microservices | Phase 5 | `NOT_STARTED` |
| Keycloak | Phase 5 | `NOT_STARTED` |
| Kafka | Phase 6 | `NOT_STARTED` |
| Spring AI | Phase 7 | `NOT_STARTED` |
| Embeddings and RAG | Phase 7 | `NOT_STARTED` |
| Redis | Phase 8 | `NOT_STARTED` |
| Elasticsearch | Phase 8 | `NOT_STARTED` |
| Testcontainers | Phase 9 | `NOT_STARTED` |
| Kubernetes | Phase 10 | `NOT_STARTED` |
| CI/CD | Phase 11 | `NOT_STARTED` |
| Prometheus and Grafana | Phase 11 | `NOT_STARTED` |

---

## 6. Known Issues and Blockers

| Issue | Priority | Status | Notes |
|-------|----------|--------|-------|
| `HireSenseApplicationTests.contextLoads` fails: "Failed to determine a suitable driver class" | Low | **Resolved 2026-11-18 (Task 1.2)** | Added PostgreSQL driver and datasource configuration; test passes against `hiresense_test` (Maven, JDK 21). |
| `pgpass.conf` passwords: contents became visible to the AI and were first mismatched | Medium | Mitigated, owner decision | The owner typed the `hiresense_app` password into `%APPDATA%\postgresql\pgpass.conf`; a file-change notice showed the AI the file content (never repeated). The two lines differed and the first was wrong; the owner then changed the password and the AI made line 2 equal to line 1 with a script that printed no password (24 characters, identical, no BOM). The owner declined to rotate the app password; the passwords looked guessable and were visible in this session, so rotating them is still recommended. |
| Database collation is the Windows locale `English_India.1252` | Low | Open (note) | Text sorting may differ from Linux/Docker databases in Phase 4. Changing it means recreating the databases. |
| `java` on PATH resolved to JDK 17 | Low | Resolved | Machine PATH already lists JDK 21 before 17 and `JAVA_HOME` is JDK 21; the 17 seen earlier was a stale session environment. Verified with a fresh environment. Restart VS Code/terminals to pick it up. |
| `ui/.angular/` (Angular build cache) shows as untracked in git | Low | Resolved | Added `ui/.angular/` to `.gitignore` (approved); verified with `git check-ignore`. |
| Local Git email is the work address (`yukta.v@ascentbusiness.com`) | Medium | Deferred (user decision) | Keep current identity for now. Before publishing the final portfolio version (Phase 12), configure a personal or GitHub noreply email locally (`git config user.email`, never `--global`) with approval. Existing 4 commits already show the work email; rewriting them is not planned. |
| ER diagram has not been visually rendered | Low | Open (documented limitation) | Mermaid syntax was verified with the real parser, but nobody has looked at the drawn diagram. Open `docs/diagrams/er-diagram.md` on GitHub or in a Mermaid preview. |
| Flyway migrations and JPA mappings not written or tested | Info | Open (Phase 1) | The database design exists only as documents. |
| PGlite constraint results (48/48) are preliminary | Medium | Open (Phase 1) | Ran on PostgreSQL 16.4 in WebAssembly. Must be repeated against the PostgreSQL version selected in Phase 1 (real server, Flyway, Testcontainers). |
| Q7 data-retention policy undecided | Medium | Open (owner: later task) | Interim rule D-16 applies (no deleting jobs that have applications; no account deletion in the MVP). |
| DO-6 database-level immutability of history/audit tables | Low | Deferred to Phase 1 | Decide when database roles are configured. Until then application code only inserts. |
| `HIRESENSE.md` Phase 1 still lists statuses as `APPLIED → SHORTLISTED → INTERVIEW → OFFER` with `REJECTED` as terminal | Medium | Resolved | Updated lines 491–501 of `HIRESENSE.md` with the text you approved (statuses `ACCEPTED`/`DECLINED`, reopen, reasons, audit). `docs/requirements.md` remains the source of truth; the roadmap only points to FR-035..FR-037. Edit is uncommitted. |
| Backend cannot start; `HireSenseApplicationTests.contextLoads` fails | High (until 1.2) | **Resolved 2026-11-18 (Task 1.2)** | `mvnw clean verify` on JDK 21: 1 test, 0 failures, BUILD SUCCESS. The app connects to PostgreSQL as `hiresense_app`. |
| Shell `java` resolves to JDK 17, so `mvnw test` forks Java 17 and fails on Java 21 classes | Low | Open (workaround) | Set `JAVA_HOME` and `PATH` to JDK 21 (`C:\Program Files\Eclipse Adoptium\jdk-21.0.13.9-hotspot`) for shell runs. IntelliJ's project SDK is now `temurin-21` (verified in `.idea/misc.xml`). |
| PostgreSQL 17.12 server listened on all network interfaces (`listen_addresses = '*'`) | Medium | **Resolved 2026-11-18** | Owner set `listen_addresses = 'localhost'` and restarted the service. Verified by the AI: `postgresql.conf` and `postgresql.auto.conf` both say `localhost`; only `127.0.0.1:5432` and `::1:5432` listen; no `0.0.0.0` or `::`. |
| `psql`, `pg_dump`, `pg_restore` and a "SQL Shell (psql)" shortcut exist on disk | Low | Open – owner decision | Came with the installation (owner asked for no psql). Not on PATH, never used; IntelliJ remains the only database interface. |
| PostgreSQL service starts automatically with Windows | Info | Open – owner may change | Start type Auto; changing it is a Windows setting and needs approval. |
| Phase 1 started while Phase 0 is open | Info | Accepted by owner | 0.8, 0.9, committing the docs and the verification gaps remain; no `phase-0` tag until addressed. |
| Docker Desktop not installed | Low | Deferred | Needed from Phase 4. Owner: do not install it. |
| `pgpass.conf` was moved into OneDrive `Documents` (company-synced cloud folder) and the app could not connect | Medium | Mitigated 2026-11-19 | The file was moved back to `%APPDATA%\postgresql\pgpass.conf` (the only place the driver looks). A copy may remain in OneDrive's online recycle bin: owner to check and empty it. Changing the app password is optional and not yet decided. |
| Default privileges for `hiresense_app` were missing in `hiresense` (present only in `hiresense_test`) | High (for Task 1.3) | **Resolved 2026-11-19** | Found with read-only `psql` catalog queries. Fixed with two `ALTER DEFAULT PRIVILEGES FOR ROLE hiresense_owner IN SCHEMA public GRANT …` statements run as `hiresense_owner` in `hiresense`. Verified in both databases: tables `hiresense_app=arwd/hiresense_owner`, sequences `hiresense_app=rU/hiresense_owner`. The earlier changelog line saying they existed in both was wrong for `hiresense`. |
| `pgpass.conf` was overwritten: the two `hiresense_app` lines were replaced by owner lines with mismatched passwords | Medium | **Resolved 2026-11-19** | Rebuilt by the AI at the owner's request (5 lines, UTF-8 without BOM) after testing the layout in a temporary file. Verified: all 5 logins work (app and owner on `hiresense` and `hiresense_test`, owner on `postgres`). While diagnosing, the AI printed password lengths and short hashes (never the passwords). Optional: rotate the passwords. |
| IntelliJ console on `hiresense_owner@hiresense` opened on database `postgres` | Low | Open (cause unknown) | `SELECT current_database()` returned `postgres` although the data source URL is `…/hiresense`. Always run `SELECT current_database(), current_user;` before any change. |
| Two identical Spring Boot run configurations named `HireSenseApplication` | Info | Open (owner decision: keep) | Both use `com.hiresense.HireSenseApplication`; no arguments or variables. |
| Maven runner JRE and importer JDK in IntelliJ not confirmed | Low | Open (owner) | Check Settings > Build Tools > Maven > Runner and Importing; both should be Project JDK (`temurin-21`). |
| Flyway running inside the application is not yet tested with `pgpass.conf` | Low | **Resolved 2026-11-19 (Task 1.3)** | The pgJDBC mechanism was verified in isolation first. Flyway then authenticated as `hiresense_owner` through `pgpass.conf` in the Maven test run (`hiresense_test`) and in the owner's two IntelliJ starts (`hiresense`). |
| V1 and V2 have not been tested on a completely new, isolated PostgreSQL database | Low | Open (verification item) | The first IntelliJ start applied V1 and V2 together to `hiresense` (which had no history table); the tests applied V1 and then V2 to `hiresense_test`. Neither is a new, isolated PostgreSQL server or database created for the purpose. To be covered by Testcontainers (Phase 9) or the Docker setup (Phase 4). |
| `spring.jpa.open-in-view` warning at startup | Info | Open (note) | Spring Boot default; decide when entities and controllers exist. |
| Migrations run in the application process at startup, as `hiresense_owner`, in local development | Info | Open (note) | Production should run migrations as a separate step (Phase 4 and Phase 10). |
| `V2` names the role `hiresense_app`, so any new environment must create that role before Flyway runs | Info | Open (note) | Relevant to Docker (Phase 4) and Kubernetes (Phase 10). |
| IntelliJ run configuration launches with JRebel (evaluation license), a profiler agent and JMX flags | Info | Open (note) | Not part of the project; visible in the owner's run logs. Makes startup slower (22.7 s first start, 5.7 s second start). |
| With the default settings the app fails to start when PostgreSQL is unreachable (Hibernate needs JDBC metadata at startup) | Info | Open (note) | Seen as "Unable to determine Dialect without JDBC metadata". The real cause (no password / no connection) is hidden in that message; look for `HikariPool` lines above it. |
| Angular CLI not installed globally | Info | Accepted | Use `npx ng` (project has a local CLI). |
| **C1** – how the acting user is identified before Phase 2 security | High (blocks 1.8–1.10) | **Provisional – finalize before Task 1.8** | Current leaning, not a decision: a development-only header read through one `CurrentActor` provider, with an `Actor` (id and role) passed into services; replaced by the authenticated principal in Phase 2. Alternatives: user id in URL or body, bringing Spring Security forward, no actor until Phase 2. See ADR-0002 §4.1. |
| **C2** – roadmap API list versus decisions D-03, D-11, D-16, D-18, D-25 | High (blocks 1.8–1.10) | **Provisional – finalize before Task 1.8** | Leanings, not decisions: close jobs with `PATCH /api/jobs/{id}/status` instead of `DELETE`; keep the application status PATCH for forward moves and reject, and add `POST …/reopen` and a candidate `POST …/offer-response`. Also open: the body of `POST /api/jobs/{id}/apply` (`resumeId`), and an endpoint for a recruiter to list a job's applicants. See ADR-0002 §4.2. |
| **C3** – PostgreSQL server and version | High | **Resolved 2026-11-18 (Task 1.2)** | Local PostgreSQL 17.12, installed by the owner. Spring Boot 4.1.2 manages driver 42.7.14 and Flyway 12.4.0. Verified in Tasks 1.2 and 1.3. |
| **C4** – repository tests against a separate local PostgreSQL test database, no H2 | High | **Resolved 2026-11-18 (Task 1.2)** | Test database `hiresense_test` via `src/test/resources/application.properties`. 14 tests ran against it. Testcontainers is planned for Phase 9. |
| **C5** – dependencies (Lombok, MapStruct, springdoc, etc.) | Medium | Open – decide per dependency, before first use | Each is explained, checked against Spring Boot 4.1.2 and Java 21, and approved by the owner before being added. The first decision is due in Task 1.5. |
| ADR-0001 (modular monolith first) is not written, although ADR-0002 refers to it | Low | Open (Task 0.8) | Planned as part of the architecture documentation task. |
| `FlywayMigrationTest` method name `migrationsOneAndTwoAreAppliedSuccessfully` is outdated (it covers versions 1 to 6) | Low | Open (rename needs owner approval) | Suggested name: `allMigrationsAreAppliedSuccessfully`. Left unchanged on purpose. |
| Task 1.5 acceptance criteria include candidate and recruiter profile tables and tests, not yet built | Medium | Open (owner decision (a): keep Task 1.5 open until done) | `candidate_profiles` (optional phone, one profile per user) and `recruiter_profiles` (nullable `company_id`; the `companies` table now exists from V4). |
| `mvnw.cmd` run from PowerShell printed `'Resilience' is not recognized` and returned exit code 0 although the build output was unclear | Low | Open (note) | Seen once on 2026-11-23. The same command from the Bash tool compiled correctly and produced class files. Cause not investigated; for now run Maven from Bash with `JAVA_HOME` set to JDK 21. |
| First full `mvnw test` run after V6 reported 40 tests: `FlywayMigrationTest` (13) was missing from the output and its surefire report was stale | Low | Open (not reproduced) | The test passed alone (13) and in the immediate full re-run (53 total, log saved). Cause unknown. If the total is ever below 53 again, compare the surefire reports' timestamps before trusting the result. |

---

## 7. Important Development Decisions

Record important technical decisions here. Larger decisions also get an ADR in `docs/adr/`.

| Decision | Reason | Phase |
|----------|--------|-------|
| Start with a modular monolith | Avoid premature microservice complexity | Phase 1 |
| Use Flyway | Version-controlled database migrations | Phase 1 |
| Use PostgreSQL | Relational data storage | Phase 1 |
| Introduce Keycloak later | Learn JWT and Spring Security first | Phase 2/5 |
| Introduce Kafka after microservices | Learn asynchronous communication | Phase 6 |
| Use Ollama initially | Local AI development | Phase 7 |
| Anonymous job browsing, login required to apply (D-01) | Lower sign-up barrier; public data minimal and rate-limited | Phase 1/2 |
| Controlled application workflow with `ACCEPTED`/`DECLINED` and recruiter-only reopen with reason (D-02, D-10, D-11, D-14) | Truthful hiring record, human oversight | Phase 1 |
| Close jobs instead of deleting; no hard delete while applications exist (D-03, D-16) | Protect candidate history until retention policy is defined | Phase 1 |
| Password 8–64 chars, blocklist, BCrypt (D-04, D-08) | Length over complexity rules; BCrypt 72-byte limit | Phase 2 |
| Multiple resumes (max 5, 5 MB), mandatory when applying, private storage (D-06, D-09, D-15, D-17) | Candidate flexibility and privacy | Phase 4 |
| AI is advisory only (D-07) | Human oversight of hiring decisions | Phase 7 |
| Candidate answers offers; recruiter cannot (D-18) | The decision to accept stays with the candidate | Phase 1 |
| Password byte-length check before BCrypt, never truncate (D-19) | BCrypt ignores bytes after 72 | Phase 2 |
| Mandatory reason for rejection and reopen; admins cannot reopen or download resumes (D-20, D-24, D-25) | Auditability and least privilege | Phase 1/4 |
| Normalized skills tables, `recruiter_profiles`, UUID for API entities and BIGINT for logs (DD-01..DD-04) | Teachable relational design; skill filtering; unguessable ids | Phase 1 |
| Composite FK for resume ownership, `RESTRICT` on history-bearing FKs, DB CHECKs as safety net; workflow in the service layer (DD-07, DD-08, DD-14) | The database refuses invalid data even if code has a bug | Phase 1 |
| Status change and history row in one transaction, optimistic locking (409 on conflict) | Current state and audit trail can never disagree | Phase 1 |
| Start Phase 1 while Phase 0 stays open; no `phase-0` tag yet | Owner priority is to begin development; Phase 0 leftovers continue alongside | Phase 0/1 |
| Base package `com.hiresense` instead of `com.example.hiresense` | Remove the placeholder before any code exists | Phase 1 |
| PostgreSQL used through IntelliJ IDEA Database Tools; no H2 for integration tests; no extra database clients or Docker | Owner preference and fidelity to the real database constraints | Phase 1 |
| One recruiter per company is a service rule, not a schema limit | Business rules may change; the schema should not need a migration | Phase 1 |
| Database password kept in `%APPDATA%\postgresql\pgpass.conf` (outside the project), not in run configurations or properties | The repo is public; the JDBC driver reads the file itself (verified) | Phase 1 |
| Application runs as `hiresense_app` (no DDL); `hiresense_owner` is for Flyway only | Least privilege | Phase 1 |
| Tests use a separate database `hiresense_test` through `src/test/resources/application.properties` | Tests must never touch development data; no H2 | Phase 1 |
| `psql` allowed only for the read-only verification queries described to the owner, plus the one approved permission fix of 2026-11-19; IntelliJ remains the normal database interface | An IntelliJ console session problem made it unreliable for one fix; `psql` reads `pgpass.conf` so the AI never sees passwords | Phase 1 |
| Restrict `hiresense_app` from `flyway_schema_history` (owner preference; approach `V2__revoke_app_access_to_flyway_history.sql`, approved in principle, not yet implemented) | Least privilege; the app never needs the migration history. **Implemented and verified 2026-11-19 (Task 1.3).** | Phase 1 |
| Flyway owns the database schema; `spring.jpa.hibernate.ddl-auto=none` stays; no business tables until Task 1.5 onward | Reviewable, versioned changes; Hibernate must not alter the schema | Phase 1 |
| Flyway connects as `hiresense_owner` with no password in project files (read from `pgpass.conf`); `hiresense_app` stays the runtime role | Least privilege | Phase 1 |
| The migration history table is owned by `hiresense_owner` and nothing is granted to `PUBLIC` | Protect migration history from the application role | Phase 1 |
| Package-by-feature under `com.hiresense`, layers (`web` → `service` → `repository`, plus `domain`) inside each module (ADR-0002) | Visible module boundaries; prepares the Phase 5 split | Phase 1 |
| Across modules an entity stores only the other module's id; JPA associations only inside a module (D1, option B) | Avoids glued modules and entities leaking across boundaries; the database schema and foreign keys are unchanged | Phase 1 |
| Modules communicate only through the target module's public `api` package; no cross-module repository access, no cycles | Keeps dependencies one-directional and testable with fakes | Phase 1 |
| `skill` and `recruiter` remain separate modules | `skill` inside `common` or `job` would give the wrong dependency direction. Merging `recruiter` into `company` would make `company` depend on `user`. | Phase 1 |
| Rules enforced by convention and code review now; ArchUnit planned for Phase 9 | No dependency before there are classes to protect. Risk accepted: drift is caught only by review. | Phase 1/9 |
| No empty packages or placeholder classes; a package is created when its first real class is added | Avoids dead structure | Phase 1 |
| Create the `companies` table in Task 1.6, but defer the Company entity and repository to the first feature that needs them | The jobs foreign key needs the table; no code uses the entity yet | Phase 1 |
| `Job` gets `close()` and `reopen()` so `status` and `closed_at` always change together | The database CHECK ties the two columns; setters could break it | Phase 1 |
| Task 1.6 creates only the `jobs(company_id)` and partial `OPEN` indexes; `employment_type` and `lower(location)` indexes wait for Task 1.8 | Indexes are tied to real queries, which exist from Task 1.8 | Phase 1 |
| `skills` and `job_skills` are deferred; a job has no skills in Task 1.6 | Separate module, only needed for skill filtering | Phase 1 |
| Task 1.5 stays open until the candidate and recruiter profile tables and tests exist (owner decision (a)) | The original acceptance criteria are not fully met by the User part alone | Phase 1 |
| `recruiter_profiles` gets a `version BIGINT NOT NULL DEFAULT 0` column, a deviation from `database-design.md` section 3.3 (owner decision 2026-11-24) | Both profile entities extend `AuditableEntity` (which needs `version`); a second base class would add complexity. The design document is to be updated when V7 is written | Phase 1 |
| `candidate_profiles.user_id` is `UNIQUE` with `ON DELETE RESTRICT` toward `users` (owner decision 2026-11-24) | The database itself enforces one profile per user, also under concurrent requests and direct access; accounts are not deleted in the MVP. The candidate module stores only the user id, not a `User` entity (ADR-0002) | Phase 1 |
| The candidate profile table is migrated first; the recruiter table starts only after the candidate profile work is completed and verified (owner decision 2026-11-24) | One small step at a time | Phase 1 |

---

## 8. Git History and Phase Tags

> All Git changes require my explicit approval.

| Phase | Commit | Git Tag | Status |
|-------|--------|---------|--------|
| Phase 0 | Pending approval | Pending approval | `NOT_STARTED` |
| Phase 1 | Pending approval | Pending approval | `NOT_STARTED` |
| Phase 2 | Pending approval | Pending approval | `NOT_STARTED` |
| Phase 3 | Pending approval | Pending approval | `NOT_STARTED` |
| Phase 4 | Pending approval | Pending approval | `NOT_STARTED` |
| Phase 5 | Pending approval | Pending approval | `NOT_STARTED` |
| Phase 6 | Pending approval | Pending approval | `NOT_STARTED` |
| Phase 7 | Pending approval | Pending approval | `NOT_STARTED` |
| Phase 8 | Pending approval | Pending approval | `NOT_STARTED` |
| Phase 9 | Pending approval | Pending approval | `NOT_STARTED` |
| Phase 10 | Pending approval | Pending approval | `NOT_STARTED` |
| Phase 11 | Pending approval | Pending approval | `NOT_STARTED` |
| Phase 12 | Pending approval | Pending approval | `NOT_STARTED` |

---

## 9. Development Changelog

Record meaningful development milestones (newest at the top).

### Task 1.8a – public job read API: response DTOs (2026-11-27)
- **Owner decisions (all five approved):** split Task 1.8 into 1.8a (public read: `GET /api/jobs`, `GET /api/jobs/{id}`) and 1.8b (recruiter write: create, update, `PATCH /api/jobs/{id}/status`); C1 and C2 are decided only before 1.8b. The detail endpoint returns open **and** closed jobs (list: open only). 1.8a returns `companyId` only; company basics come in a later slice (1.8c: read-only `Company` entity and `company.api`). A minimal `@RestControllerAdvice` (404, 400) arrives in 1.8a and is extended in Task 1.11. Page size: default 20, maximum 50, default sort `createdAt` descending. No new dependency for 1.8a; MapStruct and Lombok not used for now; `spring-boot-starter-validation` is decided in 1.8b; springdoc in Task 1.12.
- **Implemented (exact code shown and approved first):** `job/web/JobResponse.java` (record, 9 fields, hand-written `from(Job)` mapping; `version` and `updatedAt` deliberately not exposed), `common/web/PageResponse.java` (own JSON shape `content/page/size/totalElements/totalPages`, `from(Page, mapper)`), `JobResponseTest` (3 tests, including an exact-field-list check) and `PageResponseTest` (2 tests). Plain unit tests, no Spring context, no database.
- **Tested (Maven, JDK 21.0.13):** the two new classes: 5 tests, 0 failures. Full suite: **103 tests, 0 failures, 0 errors, 0 skipped**, BUILD SUCCESS; all nine surefire reports carry the same timestamp (17:29).
- **Not done yet in 1.8a:** the `JobRepository` search query (and whether a V10 index migration is justified), `JobService`, `JobNotFoundException`, the minimal exception handler, `JobController`, repository/service/controller tests. **Not verified:** nothing has been called over HTTP; V3–V9 on `hiresense`.
- Git: nothing staged, committed or pushed by the AI.

### Task 1.7 – Application entity, repository and tests; task completed (2026-11-27)
- **Owner approvals:** the plan and Option A (resumes table without an entity); the exact code of `ApplicationStatus` and `Application` (compiled with `mvnw -q compile`, exit 0); then the repository and the test code (shown in full first).
- **Implemented:** `application/domain/ApplicationStatus.java` (7 values, matching `ck_applications_status`), `application/domain/Application.java` (extends `AuditableEntity`; plain-UUID `jobId`, `candidateId`, `resumeId`, all `updatable = false`; status defaults to `APPLIED`; null ids rejected in the constructor; **no status setter by design**, the transition rules come with Task 1.10), `application/repository/ApplicationRepository.java` (no custom methods yet), `ApplicationRepositoryTest.java` (15 test methods, 21 runs).
- **Tested (Maven, JDK 21.0.13):** `-Dtest=ApplicationRepositoryTest`: 21 tests, 0 failures, passed on the first run. Full suite: **98 tests, 0 failures, 0 errors, 0 skipped**, BUILD SUCCESS; all seven surefire reports carry the same timestamp (17:23).
- **Covered with bad data, each with a valid control first:** unknown status (`ck_applications_status`, 23514); duplicate job and candidate (`uq_applications_job_candidate`, 23505); nonexistent job (`fk_applications_job`); nonexistent resume (`fk_applications_resume`); **another candidate's resume rejected by the composite foreign key** (`fk_applications_resume`, 23503); nonexistent candidate (23503, either `fk_applications_candidate` or `fk_applications_resume`, since PostgreSQL does not say which is reported first); `ON DELETE RESTRICT` for a job and for a resume that an application uses. Also: read-back with default `APPLIED`, audit fields, all 7 statuses stored, valid combinations allowed, optimistic locking, version 0 to 1, null ids.
- **Finding:** `fk_applications_candidate` is logically implied by the composite foreign key (every resume row points at an existing candidate profile). It is harmless and the design lists it explicitly, so it was kept; removing it would need a new migration. For the same reason there is no isolated "profile with an application cannot be deleted" test: the foreign key from `resumes` would fail too, and the reported constraint is not guaranteed.
- **Task 1.7 is `COMPLETED`, not `VERIFIED`.** **Not verified:** V3–V9 on the development database `hiresense`; the app running against `hiresense`; real concurrent transactions (simulated in one transaction); leftover rows in `hiresense_test` (expected 0); the `resumes` table has no entity or repository of its own yet (Phase 4).
- **Deferred:** `application_status_history` to Task 1.9; the index on `applications.resume_id` until a resume-delete query exists.
- Git: nothing staged, committed or pushed by the AI. Uncommitted: `application/` main and test sources, `JobRepositoryTest.java` and this file (V9 and the `FlywayMigrationTest` change are staged by the owner).

### Task 1.6 – stale-update test added to JobRepositoryTest (2026-11-27)
- **Owner approvals:** the test strategy, then the exact code (shown in full first, with `entityManager.detach(stale)` placed before `current` is updated).
- **Implemented:** one import (`ObjectOptimisticLockingFailureException`) and one test, `staleUpdateIsRejectedByOptimisticLocking`, in `JobRepositoryTest`. It uses the real `close(Instant)` method as the change. The other 28 runs and the production code are untouched.
- **Tested (Maven, JDK 21.0.13):** `-Dtest=JobRepositoryTest`: **29 tests, 0 failures**, passed on the first run, including the two assertions after the exception (the row stays at version 1 with the first editor's `closed_at`). Full suite: **77 tests, 0 failures, 0 errors, 0 skipped**, BUILD SUCCESS; all six surefire reports carry the same timestamp.
- **Limit (unchanged):** the second editor is simulated with a detached copy inside one rolled-back transaction; real concurrent transactions remain a Phase 9 item. This closes the optimistic-locking gap that was listed against `JobRepositoryTest`.
- Git: nothing staged, committed or pushed by the AI.

### Task 1.7 – V9 applications migration (2026-11-27)
- **Owner approvals:** the exact V9 SQL (shown in full first, with the decisions: both indexes now, `DEFAULT 'APPLIED'` kept); creating only that file and applying it; the one-line `FlywayMigrationTest` change; the read-only `psql` checks.
- **Implemented:** `backend/src/main/resources/db/migration/V9__create_applications_table.sql`: 8 columns; `pk_applications`, `uq_applications_job_candidate`, `fk_applications_job` and `fk_applications_candidate` (both `ON DELETE RESTRICT`), the composite `fk_applications_resume` (`(resume_id, candidate_id)` to `resumes (id, candidate_id)`, `ON DELETE RESTRICT`), `ck_applications_status` (7 values); indexes `ix_applications_candidate_id` and `ix_applications_job_status (job_id, status)`. No index on `resume_id` yet (deferred until the resume delete query exists). `FlywayMigrationTest` line 51 now expects `"1"` to `"9"`.
- **Tested (Maven, JDK 21.0.13):** `-Dtest=HireSenseApplicationTests`: 1 test passed, "Successfully validated 9 migrations", "now at version v9". Full suite: **76 tests, 0 failures, 0 errors, 0 skipped**, BUILD SUCCESS; all six surefire reports carry the same timestamp.
- **Checked read-only (`psql` as `hiresense_owner`, `hiresense_test`, database and user confirmed first; SELECTs only):** history ranks 1–9 all `success = t`; 8 columns with the approved types and nullability, `status` default `'APPLIED'`, `version` default 0; the six constraints (the composite foreign key reads `FOREIGN KEY (resume_id, candidate_id) REFERENCES resumes(id, candidate_id) ON DELETE RESTRICT`) and four indexes as approved; owner `hiresense_owner`, ACL `hiresense_app=arwd`; `has_table_privilege` true for SELECT, INSERT, UPDATE and DELETE (catalog check); 0 rows.
- **Not verified:** all `applications` and `resumes` constraints with invalid data (next: the `Application` entity and its tests); a real insert as `hiresense_app`; V8 and V9 on `hiresense`.
- **Still to do in Task 1.7:** `ApplicationStatus`, `Application`, `ApplicationRepository`, `ApplicationRepositoryTest`.
- Git: nothing staged, committed or pushed by the AI. Uncommitted: V8, V9, `FlywayMigrationTest.java`, `RecruiterProfileRepository`, `RecruiterProfileRepositoryTest`, `database-design.md` and this file.

### Task 1.7 – V8 resumes migration (2026-11-27)
- **Status:** Task 1.7 is `IN_PROGRESS` (was `NOT_STARTED`). Plan agreed with the owner: **Option A** – create the `resumes` table now without an entity (like `companies`), then V9 `applications`, then `ApplicationStatus`, `Application`, `ApplicationRepository` and tests. `application_status_history` is deferred to Task 1.9, where it is first written.
- **Owner approvals:** the plan and Option A; the exact V8 SQL (shown in full first); creating only that file and applying it; the one-line `FlywayMigrationTest` change; the read-only `psql` checks.
- **Implemented:** `backend/src/main/resources/db/migration/V8__create_resumes_table.sql`: 8 columns; `pk_resumes`, `fk_resumes_candidate` (to `candidate_profiles`, `ON DELETE RESTRICT`), `uq_resumes_storage_key`, `uq_resumes_id_candidate` (target of the composite foreign key from `applications`), `ck_resumes_size_bytes` (1 to 5242880); index `ix_resumes_candidate_id`. Only `created_at`, no `updated_at` or `version`, exactly as in the design. `FlywayMigrationTest` line 51 now expects `"1"` to `"8"`.
- **Tested (Maven, JDK 21.0.13):** `-Dtest=HireSenseApplicationTests`: 1 test passed, "Successfully validated 8 migrations", "now at version v8". Full suite: **76 tests, 0 failures, 0 errors, 0 skipped**, BUILD SUCCESS (the run crossed a minute boundary, so surefire timestamps show 17:05 for one report and 17:06 for five).
- **Checked read-only (`psql` as `hiresense_owner`, `hiresense_test`, database and user confirmed first; SELECTs only):** history ranks 1–8 all `success = t`; 8 columns with the approved types, lengths and nullability; the five constraints and four indexes as approved; owner `hiresense_owner`, ACL `hiresense_app=arwd`; `has_table_privilege` true for SELECT, INSERT, UPDATE and DELETE (catalog check); 0 rows.
- **Not verified:** the constraints with invalid data (to be exercised once `applications` and its tests exist); a real insert as `hiresense_app`; V8 on `hiresense`.
- Git: nothing staged, committed or pushed by the AI. Uncommitted: V8, `FlywayMigrationTest.java`, the two files from the recruiter step, `database-design.md` and this file.

### Task 1.5 – recruiter profile entity, repository and tests; task completed (2026-11-27)
- **Found (read-only):** the owner had committed V7 and `RecruiterProfile` in `c129f53`; only `database-design.md` was uncommitted.
- **Owner approvals:** the exact code of `RecruiterProfile` (compiled with `mvnw -q compile`, exit 0), then the repository and the test code together.
- **Implemented:** `recruiter/repository/RecruiterProfileRepository.java` (`findByUserId`; no lookup by company until a caller needs it) and `RecruiterProfileRepositoryTest.java` (13 tests; the unused `ReflectionTestUtils` import from the shown draft was left out). Companies are inserted with SQL because there is no `Company` entity.
- **Tested (Maven, JDK 21.0.13):** `-Dtest=RecruiterProfileRepositoryTest`: 13 tests, 0 failures, passed on the first run. Full suite: **76 tests, 0 failures, 0 errors, 0 skipped**, BUILD SUCCESS; all six surefire reports carry the same timestamp.
- **Task 1.5 is now `COMPLETED`, not `VERIFIED`.** **Not verified:** V3–V7 on the development database `hiresense`; the app running against `hiresense`; real concurrent transactions (simulated in one transaction); leftover rows in `hiresense_test` (expected 0).
- **Pending:** rename the outdated `FlywayMigrationTest` method name (needs approval); the service rule "role must be `RECRUITER`" and "one recruiter per company" come with the services.
- Git: nothing staged, committed or pushed by the AI. Uncommitted: the two new files, `database-design.md` and this file.

### Task 1.5 – V7 recruiter profiles migration (2026-11-26)
- **Owner approvals, step by step:** the exact V7 SQL (shown in full before creating the file); creating only that file and applying it; the one-line `FlywayMigrationTest` change; the read-only `psql` checks.
- **Implemented:** `backend/src/main/resources/db/migration/V7__create_recruiter_profiles_table.sql`: 6 columns (`company_id` nullable; `version BIGINT NOT NULL DEFAULT 0`); `pk_recruiter_profiles`, `uq_recruiter_profiles_user`, `fk_recruiter_profiles_user` and `fk_recruiter_profiles_company` (both `ON DELETE RESTRICT`); index `ix_recruiter_profiles_company_id`. No UNIQUE on `company_id` (one recruiter per company is a service rule) and no role check (a CHECK cannot read `users`). `FlywayMigrationTest` line 51 now expects `"1"` to `"7"`; the method name was not changed. `docs/database/database-design.md` section 3.3 now lists the `version` column.
- **Tested (Maven, JDK 21.0.13):** `-Dtest=HireSenseApplicationTests`: 1 test passed, "Successfully validated 7 migrations", "now at version v7". Full suite: **63 tests, 0 failures, 0 errors, 0 skipped**, BUILD SUCCESS; all five surefire reports carry the same timestamp.
- **Checked read-only (`psql` as `hiresense_owner`, `hiresense_test`, database and user confirmed first; SELECTs only):** history ranks 1–7 all `success = t`; 6 columns with the approved types and nullability, `version` default 0; the four constraints and three indexes as approved; owner `hiresense_owner`, ACL `hiresense_app=arwd`; `has_table_privilege` true for SELECT, INSERT, UPDATE and DELETE (catalog check); 0 rows.
- **Not verified:** the constraints with invalid data (the repository test will do that); a real insert as `hiresense_app`; V7 on `hiresense`.
- Git: nothing staged, committed or pushed by the AI. Uncommitted: V7, `FlywayMigrationTest.java`, `CandidateProfileRepositoryTest.java`, `database-design.md`, this file.

### Task 1.5 – CandidateProfileRepositoryTest added; candidate profile part done (2026-11-26)
- **Found (read-only):** this file was stale. `CandidateProfile` and `CandidateProfileRepository` were already committed by the owner in `7b5c036` and `f5c3d7a`.
- **Owner approvals:** the plan, then the exact test code (shown in full before the file was created).
- **Implemented:** `backend/src/test/java/com/hiresense/candidate/repository/CandidateProfileRepositoryTest.java` (10 tests; one more than planned: the headline length check, with a control at exactly 200 characters).
- **Tested (Maven, JDK 21.0.13):** `-Dtest=CandidateProfileRepositoryTest`: 10 tests, 0 failures, passed on the first run. Full suite: **63 tests, 0 failures, 0 errors, 0 skipped**, BUILD SUCCESS; all five surefire reports carry the same timestamp, so none is stale.
- **Not verified:** V6 on `hiresense`; the app running with the entity; real concurrent transactions (simulated inside one transaction); leftover rows in `hiresense_test`.
- Git: nothing staged, committed or pushed by the AI. Uncommitted: the new test file and this file.

### Task 1.5 – V6 candidate profiles migration (2026-11-24)
- **Owner approvals, step by step:** the plan and three decisions (add `version BIGINT NOT NULL DEFAULT 0` to `recruiter_profiles`; `ON DELETE RESTRICT` for `candidate_profiles.user_id`; candidate profile first, recruiter table later); the exact V6 SQL (shown twice, approved without changes); creating only that file; running only `HireSenseApplicationTests`; the read-only `psql` checks; the one-line `FlywayMigrationTest` change; this progress update.
- **Verified against the requirements before writing SQL:** design section 3.2 against FR-010 (AC2 name and email mandatory, they live on `users`; AC3 optional phone, location, headline/summary, experience and education summaries) and D-13 (phone optional). The role check and the phone-format check are not in the database: the service enforces the role (a CHECK cannot read another table) and validation covers the format (Task 1.11).
- **Implemented:** `backend/src/main/resources/db/migration/V6__create_candidate_profiles_table.sql`. `FlywayMigrationTest` line 51 now expects `"1"` to `"6"`; the method name was not changed.
- **Tested (Maven, JDK 21.0.13):** `mvnw test -Dtest=HireSenseApplicationTests`: 1 test passed; Flyway "Successfully applied 1 migration … now at version v6". `FlywayMigrationTest` alone: 13 tests, 0 failures. **Authoritative full-suite result (re-run, log saved): 53 tests, 0 failures, 0 errors, 0 skipped, BUILD SUCCESS** (13 Flyway + 1 context + 28 `JobRepositoryTest` + 11 `UserRepositoryTest`). **The first full run reported only 40 tests: `FlywayMigrationTest` did not appear in its output and its surefire report was stale (10:17). The cause was not found and could not be reproduced; the 53-test re-run is the result to rely on.**
- **Checked read-only (`psql` as `hiresense_owner`, `hiresense_test`, database and user confirmed first; SELECTs only):** `flyway_schema_history` ranks 1–6 all `success = t`; 11 columns as approved; `version` default 0; `pk_candidate_profiles`, `uq_candidate_profiles_user`, `fk_candidate_profiles_user` (`ON DELETE RESTRICT`); two unique indexes; owner `hiresense_owner`; ACL `hiresense_app=arwd`; `has_table_privilege` true for SELECT, INSERT, UPDATE and DELETE; 0 rows.
- **Limitations / not verified:** the constraints were inspected, not tested with invalid data (the repository test will do that once `CandidateProfile` exists); the application privileges were catalog-checked, not verified with a real insert; V6 (and V3 to V5) have not been applied to the development database `hiresense`; the app has not been run with the new table.
- **Pending documentation:** `docs/database/database-design.md` section 3.3 still lists `recruiter_profiles` without a `version` column; update it when V7 is designed (owner decision: add the column so both profile entities extend `AuditableEntity`).
- Git: nothing staged, committed or pushed by the AI. Uncommitted: V6, `FlywayMigrationTest.java`, `JobRepositoryTest.java`, this file.

### Task 1.6 – Job entity, repository and repository tests completed (2026-11-24)
- **Owner approvals, step by step:** the exact code of `Job.java` (revised: null check on `close`, idempotent `close` and `reopen`); `JobRepository.java`; `JobRepositoryTest.java`; the full test run; this progress update.
- **Implemented:** `Job.java`, `JobRepository.java` (no custom methods, because no caller exists yet), `JobRepositoryTest.java` (28 cases: 24 plain tests plus 4 runs of the parameterized employment-type test). `EmploymentType` and `JobStatus` already existed (commit `872113b`) and were checked against the V5 CHECK constraints.
- **Compiled:** `mvnw -q compile` on JDK 21.0.13 after `Job.java` and after `JobRepository.java`: exit code 0, class files present.
- **Tested (Maven, JDK 21.0.13):** `mvnw test -Dtest=JobRepositoryTest`: 28 tests, 0 failures, 0 errors; passed on the first run, including the assumption that `ON DELETE RESTRICT` reports SQLState 23503. Full suite: **53 tests, 0 failures, 0 errors, 0 skipped**, BUILD SUCCESS (13 Flyway + 1 context + 28 `JobRepositoryTest` + 11 `UserRepositoryTest`).
- **Not verified:** V4 and V5 on `hiresense`; the app running with the entities; leftover rows in `hiresense_test`; NOT NULL violations; concurrent transactions; the partial index. Task 1.6 is `COMPLETED`, not `VERIFIED`; Task 1.5 stays `IN_PROGRESS`.
- **Note:** the first compile attempt from PowerShell printed `'Resilience' is not recognized as an internal or external command` from the Windows `mvnw.cmd` wrapper and returned an unreliable exit code; the same command from the Bash tool worked. Not investigated.
- Git: nothing staged, committed or pushed by the AI. Uncommitted: `JobRepositoryTest.java` and this file (`Job.java`, `JobRepository.java` and the enums are in `c5237bc` and earlier).

### Task 1.6 – V5 jobs migration (2026-11-20)
- **Owner approvals, step by step:** the exact V5 SQL (shown in full twice before approval); creating only that file; running only `HireSenseApplicationTests`; the read-only `psql` checks; the one-line `FlywayMigrationTest` change; this progress update.
- **Implemented:** `backend/src/main/resources/db/migration/V5__create_jobs_table.sql`: 11 columns; `pk_jobs`, `fk_jobs_company` (`ON DELETE RESTRICT`), `ck_jobs_employment_type`, `ck_jobs_status`, `ck_jobs_closed_at` (`(status = 'CLOSED') = (closed_at IS NOT NULL)`); indexes `ix_jobs_company_id` and partial `ix_jobs_open_created_at` (`created_at DESC WHERE status = 'OPEN'`). `FlywayMigrationTest` line 51 now expects versions `"1"` to `"5"`; the method name was not changed.
- **Tested (Maven, JDK 21):** `mvnw test -Dtest=HireSenseApplicationTests`: 1 test passed, "Successfully validated 5 migrations", "Successfully applied 1 migration … now at version v5". Full suite afterwards: 25 tests (13 Flyway + 1 context + 11 `UserRepositoryTest`), 0 failures, 0 errors, 0 skipped, BUILD SUCCESS.
- **Checked read-only (`psql` as `hiresense_owner`, `hiresense_test`, database and user confirmed first; SELECTs only):** `flyway_schema_history` ranks 1–5 all `success = t`; the 11 `jobs` columns match the approved types, lengths, nullability and defaults; the 5 constraint definitions and 2 index definitions match the approved SQL (plus the automatic primary-key index); `has_table_privilege` for `hiresense_app` is true for SELECT, INSERT, UPDATE and DELETE (catalog check, not a real insert).
- **Not verified:** the constraints and the foreign key have not been exercised with invalid data; no real insert as `hiresense_app` into `jobs`; V4 and V5 not applied to the development database `hiresense`. Task 1.6 stays `IN_PROGRESS`; Task 1.5 stays `IN_PROGRESS`; nothing is `VERIFIED`.
- **Also done:** the stale "Current Activity" row in section 2 was rewritten.
- Git: nothing staged, committed or pushed by the AI. GitHub not modified. Uncommitted: this file, `V4__create_companies_table.sql`, `V5__create_jobs_table.sql`, `FlywayMigrationTest.java` (`User.java`, `UserRepository.java` and `UserRepositoryTest.java` were committed by the owner in `50fb525`).

### Task 1.6 – V4 companies migration; Task 1.5 scope decision (2026-11-20)
- **Owner approvals, step by step:** the Task 1.6 plan and four decisions (companies table now without entity; `close()` and `reopen()` in `Job`; only the `company_id` and partial `OPEN` indexes; skills deferred); the exact V4 SQL; applying V4 to `hiresense_test` by running only `HireSenseApplicationTests`; the read-only `psql` checks; the one-line `FlywayMigrationTest` change; these progress changes.
- **Implemented:** `backend/src/main/resources/db/migration/V4__create_companies_table.sql` (9 columns, `pk_companies`, no other constraints or indexes). `FlywayMigrationTest` line 51 now expects versions `"1", "2", "3", "4"`; the method name was not changed.
- **Tested (Maven, JDK 21.0.13):** `mvnw test -Dtest=HireSenseApplicationTests`: 1 test passed; Flyway "Successfully applied 1 migration … now at version v4". Full suite afterwards: 25 tests (13 Flyway + 1 context + 11 `UserRepositoryTest`), 0 failures, 0 errors, BUILD SUCCESS.
- **Checked read-only (`psql` as `hiresense_owner`, `hiresense_test`, database and user confirmed first):** `flyway_schema_history` ranks 1–4 all `success = t`; `companies` has the 9 approved columns with the approved types and nullability; one constraint (`pk_companies`); `has_table_privilege` for `hiresense_app` is true for SELECT, INSERT, UPDATE and DELETE (catalog check, not a real insert).
- **Task 1.5 decision (owner, option (a)):** Task 1.5 stays `IN_PROGRESS`. GitHub issue #27 lists candidate and recruiter profile tables, optional phone and one profile per user; only the User part (`users`, `User`, `UserRepository`, 11 tests) is done.
- **Not verified:** V4 on `hiresense` (development); the leftover-row count in `hiresense_test` (expected 0); the app run against `hiresense`; real inserts into `companies` as `hiresense_app`.
- Git: nothing staged, committed or pushed by the AI. GitHub issues not modified. Uncommitted at the time: this file, `V4__create_companies_table.sql`, `FlywayMigrationTest.java` (`User.java`, `UserRepository.java` and `UserRepositoryTest.java` were committed by the owner in `50fb525`).

### Task 1.5 – User entity, repository and repository tests (2026-11-20)
- **Implemented (owner approved each file's exact code first):** `User.java` (no Lombok, no setters, email trimmed and lowercased in the constructor), `UserRepository.java`, `UserRepositoryTest.java` (11 tests plus a `@BeforeEach` guard: database must be `hiresense_test`, role `hiresense_app`, test transaction active and flagged for rollback).
- **Tested (Maven, JDK 21.0.13):** 25 tests, 0 failures, 0 errors, 0 skipped. The first run of the new tests passed without changes.
- **Findings from the review:** Spring Boot 4 package moves (`org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest`, `org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase`); Flyway runs inside `@DataJpaTest` in Boot 4.1.2; entities without setters need `ReflectionTestUtils` in tests until real methods exist.
- **Limitations:** optimistic-locking tests simulate a second editor inside one transaction (no real concurrent transactions; Testcontainers in Phase 9); the guard runs after context start, so a wrongly overridden datasource could still be migrated by Flyway before it fires.
- **Not verified:** leftover rows in `hiresense_test` (expected 0, needs a read-only count); V3 on `hiresense`.
- Git: nothing staged, committed or pushed by the AI.

### Task 1.5 – progress synchronized and tests re-run (2026-11-20)
- **Found (read-only):** commit `8634d49` ("added jpaAuditConfig and roles audit entity") already contained V3, `Role`, `AuditableEntity` and `JpaAuditingConfig`, but this file still said Task 1.5 had not started. Corrected now. The commit also contains `docs/adr/0002-backend-package-structure.md`, an earlier version of this file and a small `ui/angular.json` change.
- **Tested (Maven, JDK 21.0.13, shell `JAVA_HOME` set):** `mvnw test` = 13 `FlywayMigrationTest` cases + 1 `HireSenseApplicationTests`, 0 failures, 0 errors, 0 skipped (surefire reports). Flyway log: "Successfully validated 3 migrations", schema version 3 on `hiresense_test`.
- **Not verified:** V3 on `hiresense`; app-role rights on `users`; no entity maps `users` yet, so Hibernate has not been checked against the table.
- Git: nothing staged, committed or pushed by the AI. This file is the only change.

### Task 1.4 – Backend package structure designed and approved (2026-11-19)
- **Deliverable:** `docs/adr/0002-backend-package-structure.md` (architecture decision record). Documentation only.
- **Approved by the owner:** package-by-feature with layers inside each module; ids across module boundaries and JPA associations only within a module (D1, option B); `skill` and `recruiter` stay separate modules; modules communicate only through public `api` packages; convention and code review now, ArchUnit in Phase 9; no empty packages or placeholder classes. The ADR status is Accepted.
- **Provisional, not decided:** C1 (acting user before Phase 2) and C2 (roadmap APIs versus decisions D-03, D-11, D-16, D-18, D-25). Both must be finalized before Task 1.8. C5 (dependencies) is decided per dependency.
- **Resolved:** C3 (PostgreSQL 17.12) and C4 (separate `hiresense_test` database, no H2), both through Task 1.2.
- **Not done / not verified:** no Java packages or classes created; no code, configuration or dependency changed; no compilation or tests run for this task. ADR-0001 (Task 0.8) is still unwritten, although ADR-0002 refers to it.
- Git: nothing staged, committed or pushed by the AI. Uncommitted: `PROJECT_PROGRESS.md`, `docs/adr/0002-backend-package-structure.md`, plus the Task 1.3 files already listed.

### Task 1.3 – Flyway migrations implemented and verified (2026-11-19)
- **Implemented (owner-approved, step by step):** `spring-boot-starter-flyway` and `flyway-database-postgresql` in `backend/pom.xml` (no versions written; Spring Boot 4.1.2 manages Flyway 12.4.0); `spring.jpa.hibernate.ddl-auto=none`, `spring.flyway.url` and `spring.flyway.user=hiresense_owner` (no password) in the main and the test `application.properties`; `backend/src/main/resources/db/migration/V1__baseline.sql` (`COMMENT ON SCHEMA public …`); `V2__revoke_app_access_to_flyway_history.sql` (`REVOKE ALL ON TABLE public.flyway_schema_history FROM hiresense_app`); `backend/src/test/java/com/hiresense/FlywayMigrationTest.java`. No business tables were created.
- **Tested (Maven, JDK 21.0.13):** step 1 (V1 only): 1 test, 0 failures; V1 applied to `hiresense_test`. Step 3 (with V2 and the new test): **14 tests, 0 failures, 0 errors, 0 skipped = 13 Flyway migration test cases in `FlywayMigrationTest` + 1 application context test (`contextLoads`)**. The 13 test cases covered: versions 1 and 2 applied with state SUCCESS; no pending migrations; no failed migrations; the owner can read the history table; the owner has SELECT, INSERT, UPDATE and DELETE (4 cases); the app role has none of the four (4 cases); a real SELECT as the app role fails with SQLState 42501. Every connection test first asserts its role and database. No test writes to the history table.
- **Checked with read-only `psql`:** after V1 on `hiresense_test`: history table present, version 1 success with checksum, owner `hiresense_owner`, but `hiresense_app` had SELECT, INSERT, UPDATE and DELETE (the evidence for V2). After V2, on `hiresense_test` and on `hiresense`: versions 1 and 2 recorded with `success = t` and checksums, table owned by `hiresense_owner`, `hiresense_app` has none of the four rights, no PUBLIC grant (ACL `{hiresense_owner=arwdDxtm/hiresense_owner}`), default privileges for `hiresense_app` intact, V1's schema comment present. The history rows were read as `hiresense_owner` because the app role can no longer read that table; the other queries ran as `hiresense_app`. Connections were checked against the database name first.
- **Manually verified (owner, IntelliJ, database `hiresense`):** first start: Flyway created the history table and applied V1 and V2 together ("Successfully applied 2 migrations", now at v2), application started in 22.753 s (with profiler and JRebel). Second start: "Successfully validated 2 migrations", "Schema public is up to date. No migration necessary.", started in 5.722 s. Flyway authenticated as `hiresense_owner` through `pgpass.conf` inside Spring Boot. **Owner reported that /actuator/health was working; exact HTTP status and response body were not captured.**
- **Warnings seen (harmless):** `spring.jpa.open-in-view` default; Mockito and JDK agent notices; IntelliJ launch with JRebel, profiler and JMX flags (see Known Issues).
- **Not verified:** V1 and V2 on a completely new, isolated PostgreSQL database (open item; the `hiresense` first start applied both together but is the existing development database); IntelliJ Maven runner JRE and importer JDK; `contextLoads` in IntelliJ; the HTTP status and body of /actuator/health after Flyway.
- Git: nothing staged, committed or pushed by the AI. Uncommitted: this file, `backend/pom.xml`, both `application.properties`, `backend/src/main/resources/db/`, `FlywayMigrationTest.java`. `ui/angular.json` also shows as modified and was not changed by the AI.

### Task 1.3 preparation: IntelliJ checks, database permissions, pgpass repair, dependency and JDBC authentication checks (2026-11-19)
- **IntelliJ (read-only, nothing changed):** `.idea/misc.xml` has `temurin-21` / `JDK_21`; IntelliJ's SDK table maps `temurin-21` to Temurin 21.0.13. The stale `com.example` run configuration is no longer present; two identical valid Spring Boot configurations remain (owner chose to keep both). Maven runner JRE and importer JDK: no explicit value stored, believed to be Project JDK, NOT VERIFIED (owner will check). No cleanup performed.
- **Permission checks (AI, `psql` as `hiresense_app`, read-only SELECTs of system catalogs, owner-approved):** both databases owned by `hiresense_owner`; `hiresense_app` can connect, has schema USAGE and cannot create in the database or in `public`; `hiresense_owner` can create in `public`; `flyway_schema_history` does not exist yet in either database. **Default privileges were missing in `hiresense`** and present in `hiresense_test`.
- **Fix (owner-approved; AI, `psql` as `hiresense_owner`, connection checked first):** two `ALTER DEFAULT PRIVILEGES … GRANT` statements applied in `hiresense`. An earlier attempt through IntelliJ had run on the wrong database (`postgres`); a precautionary `REVOKE` there found 0 default-privilege rows before and after, so nothing had been applied in `postgres`. Verified in all three databases afterwards.
- **`pgpass.conf` repair and five logins:** see Known Issues.
- **Dependency check (read-only, throwaway project in the scratchpad, JDK 21):** Spring Boot 4.1.2 manages Flyway 12.4.0 and PostgreSQL driver 42.7.14. `spring-boot-starter-flyway` does not itself bring in `flyway-core`; it arrives through `flyway-database-postgresql`, so both are needed. Flyway and Boot jars are compiled for Java 17, the driver for Java 8. Support of Flyway 12.4.0 for PostgreSQL 17.12 is NOT VERIFIED until Flyway migrates.
- **JDBC authentication test (owner-approved; throwaway Java program outside the repository, JDK 21.0.13, driver 42.7.14; never read or printed a password; read-only `SELECT current_user, current_database()`):** using Spring's `SimpleDriverDataSource` with pgJDBC, a user name and no password, the connection succeeded through `%APPDATA%\postgresql\pgpass.conf` for `hiresense_owner` on `hiresense` and `hiresense_test` and for `hiresense_app` on `hiresense`. Negative control with `PGPASSFILE` pointing at an empty file: all three failed with "no password was provided". The driver jar contains `PgPassParser` with Windows handling. The libpq documentation describes the file format. Test files were deleted afterwards. NOT VERIFIED: that Spring Boot's Flyway startup path behaves the same; this is only confirmed when Flyway runs in Task 1.3. The test is not claimed to use Flyway's exact connection factory.
- **Tested:** `mvnw test` on JDK 21.0.13 after all changes: 1 test, 0 failures, BUILD SUCCESS (against `hiresense_test`).
- **Not verified:** Flyway inside the application; V1 and V2 migrations; the app role's rights on real tables and on `flyway_schema_history`; IntelliJ Maven runner JRE and importer JDK; the application's start-up against `hiresense` after the permission change.
- Git: nothing staged, committed or pushed by the AI.

### Actuator health endpoint verified (2026-11-19)
- **Implemented earlier** (commit `11f2006`): `spring-boot-starter-actuator`; `management.endpoints.web.exposure.include=health`; `management.endpoint.health.show-details=never` (main and test properties).
- **Incident:** the first IntelliJ run failed with "Unable to determine Dialect without JDBC metadata". Cause found: `pgpass.conf` was no longer in `%APPDATA%\postgresql` (the owner had moved it into OneDrive `Documents`, where the driver does not look). The owner moved it back; the file was never read by the AI.
- **Tested (AI, Maven, JDK 21.0.13):** `mvnw test` = 1 test, 0 failures, BUILD SUCCESS (against `hiresense_test`).
- **Manually verified, V1 (owner, IntelliJ, development database `hiresense`):** `/actuator/health` works. The owner reported "working fine"; the exact status code and body were not reported.
- **Verified, V2 and V3 (AI, `spring-boot:run` on port 8081, temporary command-line arguments only, no project file changed):** with the database port set to an unreachable one (5999) and Hibernate's startup metadata lookup disabled, the app started and `/actuator/health` returned **503**. The response body was not captured, so `DOWN` itself is not confirmed in writing. `/actuator/env` and `/actuator/beans` returned **404**, so only `health` is exposed. The test app was stopped; nothing listens on 8081.
- **Not verified:** V3 in the owner's IntelliJ run; the health body text; IntelliJ test run, stale run configuration and Maven runner JRE (Part 2 of the session, still pending).
- Git: nothing staged, committed or pushed by the AI.

### Task 1.2 completed: database, credentials, driver, datasource, test database (2026-11-18)
- **Correction of earlier records:** the roles `hiresense_owner` and `hiresense_app` and the databases `hiresense` and `hiresense_test` **already existed** when queried in this session (owned by `hiresense_owner`, `login=true`, no superuser/createdb/createrole, no passwords). Earlier entries saying they did not exist were out of date. Who created them is not recorded. `PUBLIC` already had no access to either database (only the two roles have `CONNECT`; the app role has nothing else), and the default privileges for tables (`arwd`) and sequences (`rU`) for `hiresense_app` already existed in both databases. **No SQL was needed for Steps B and C**; my `GRANT CONNECT` changed nothing.
- **Verified in IntelliJ by the owner (query results):** server encoding UTF8; `password_encryption = scram-sha-256`; database collation `English_India.1252`; Tx mode Auto; `current_user`/`current_database` correct for both `hiresense_owner@hiresense` and `hiresense_app@hiresense` (logins work); default-privilege rows in `hiresense` and `hiresense_test` (the `public`-only scope check in `hiresense` and the `current_database()` check for the test database were requested but not confirmed in writing).
- **Done (owner):** set SCRAM passwords for both roles through the local verifier utility (no plaintext in chat; verification query shows `scram` for both). The admin data source `postgres@localhost` was re-created; the owner data source `hiresense_owner@hiresense` exists. Note: an IntelliJ console kept an old session on `postgres` until disconnected.
- **Done (AI, owner-approved):** `backend/pom.xml` got `org.postgresql:postgresql` (runtime; version 42.7.14 from Spring Boot 4.1.2, resolved with `dependency:tree`); `application.properties` got `spring.datasource.url=jdbc:postgresql://localhost:5432/hiresense` and `username=hiresense_app` (no password); new `backend/src/test/resources/application.properties` points tests at `hiresense_test` (it replaces the main file during tests). `%APPDATA%\postgresql\pgpass.conf` was created with placeholders and the owner filled in the passwords.
- **Tested (Maven, JDK 21.0.13):** `mvnw clean verify` = 1 test, 0 failures, BUILD SUCCESS. Hikari's debug log shows the connection to `jdbc:postgresql://localhost:5432/hiresense_test`. First test run failed with `password authentication failed` for each line of `pgpass.conf` in turn, proving the driver does read `pgpass.conf` and uses the per-database line. Frontend re-checked: `npm run build` OK (214.33 kB initial), `npm test`: 2 of 2 passed; not run in a browser.
- **IntelliJ:** project SDK is now `temurin-21` / language level 21 (`.idea/misc.xml` verified). Two Spring Boot run configurations exist; the stale one (`com.example.hiresense.HireSenseApplication`) is still to be removed by the owner in the IDE.
- **Not verified:** run of the test in IntelliJ; Maven runner JRE setting in IntelliJ; the app role's table privileges (no tables yet); Flyway with `pgpass`; `current_database()` of the console that showed the test-database default privileges.
- Git: nothing staged, committed or pushed by the AI.

### Task 1.2: state check after the owner's changes (2026-11-18)
- **PASSED – loopback-only listening.** Owner changed `listen_addresses` to `localhost` in `postgresql.conf` (13:22) and `postgresql.auto.conf` (13:15) and restarted the service. AI verification (read-only): service Running (Auto); only `127.0.0.1:5432` and `[::1]:5432` listen; no wildcard binding; `pg_hba.conf` unchanged (scram-sha-256, loopback only). Findings 1 below is therefore closed.
- **PASSED – IntelliJ administrator connection exists and worked.** IntelliJ's project data-source file `backend/.idea/dataSources.xml` defines `postgres@localhost` with `jdbc:postgresql://localhost:5432/postgres`; the local file records user `postgres`, a Password Safe `secret-storage` entry (value not read), and that the IDE connected: product PostgreSQL, **version 17.12**, JDBC driver 42.7.13 (IDE-only driver; the application's driver comes from Spring Boot's BOM, 42.7.14), and introspected schemas `information_schema`, `pg_catalog`, `public` of database `postgres`. The owner approved the IDE downloading the JDBC driver; it is in place (`jdbc-drivers\PostgreSQL\42.7.13`). IntelliJ data-source files live in the git-ignored `backend/.idea`.
- **NOT VERIFIED (needs one IntelliJ query by the owner; the AI cannot run queries: it has no credentials and no database client):** server encoding / database encodings and collation, effective `password_encryption`, the list of existing roles. Everything else in the nine-query list was verified from files and the network instead (version via IntelliJ metadata, port, listen address, `pg_hba.conf` rules).
- **Not done yet:** no roles, no `hiresense`/`hiresense_test` databases, no passwords/verifiers. SQL proposal to be approved first. Owner has not chosen an option for the on-disk `psql` tools; default stays "leave them, never use them".

### Task 1.2: PostgreSQL 17.12 installed by the owner; read-only post-install review (2026-11-18)
- **Installed by the owner (not by the AI):** PostgreSQL 17.12-1. Service `postgresql-x64-17` is **Running**, start type **Auto**, runs as `NT AUTHORITY\NetworkService`; install directory `C:\Program Files\PostgreSQL\17`, data directory `…\17\data`, port **5432**. `postgres.exe --version` reports 17.12. `pg_hba.conf` uses `scram-sha-256` on every line and only `127.0.0.1/32` and `::1/128` for TCP. The installer added nothing to PATH and set no `PG*` environment variables. **pgAdmin 4 is absent; the Stack Builder application and shortcut are absent.** The installer file is kept (not deleted).
- **Finding 1 – listening on all interfaces (NOT yet changed):** `listen_addresses = '*'`, so the server is bound to `0.0.0.0:5432` and `:::5432`. Mitigations observed: `pg_hba.conf` accepts only loopback, Windows Firewall is enabled on all profiles, no inbound allow rule names the PostgreSQL program. **NOT VERIFIED:** port-based firewall rules (access denied without elevation; not escalated). Proposed fix, awaiting owner approval: `ALTER SYSTEM SET listen_addresses = 'localhost';` run through IntelliJ as `postgres` on an auto-commit connection, then a service restart by the owner (needs administrator rights), then a read-only re-check of the bound addresses.
- **Finding 2 – client tools present (NOT removed):** `psql.exe`, `pg_dump.exe`, `pg_restore.exe` (and `stackbuilder.exe`, with no Stack Builder shortcut) exist in `bin`, and a Start Menu folder "PostgreSQL 17" holds a "SQL Shell (psql)" shortcut and `scripts\runpsql.bat`, so the Command Line Tools component evidently came with the install. The AI's plan assumed this component could be excluded cleanly; that assumption was wrong or the wizard choice differed (asked the owner). Not on PATH. Options offered: leave (recommended; never used, IntelliJ stays the only database interface), remove only the shortcut, or re-run the installer to change components. Nothing changed without owner approval.
- **Next (owner actions):** create the IntelliJ administrator data source (auto-commit, Password Safe, approve the IDE's JDBC driver download), then run the read-only verification queries and share the results (no secrets). No roles, databases or credentials exist yet.

### Task 1.2: installer verified, credential investigation, SCRAM script prepared (2026-11-18)
- **PostgreSQL 17.12-1 installer downloaded (owner-approved) and verified; NOT installed.** File: `C:\Users\YuktaVyas\Downloads\postgresql-17.12-installer\postgresql-17.12-1-windows-x64.exe` (416,659,176 bytes, matches the announced size). **SHA-256 PASSED** (`23bc416a9fee380f0d9ba1969647e374b43c4f37ce8ae5762f2ea59350f49fbc`, identical to the winget manifest). **Authenticode PASSED** (Status Valid; signer EnterpriseDB Corporation; issuer DigiCert; valid 2026-01-30 to 2029-02-01; timestamped). The installer was never executed by the AI. **The owner installs it personally:** PostgreSQL Server only (no pgAdmin, Stack Builder, Command Line Tools/psql, Docker). The owner will say when installation is complete. The installer file is kept for now and is deleted only after asking the owner.
- **Credential approach investigated (nothing implemented, no credentials created).** Owner approved investigating `%APPDATA%\postgresql\pgpass.conf` as the main method and the properties-file fallback **outside the project**, used only with approval. Verified facts: the PostgreSQL docs define the Windows file as `%APPDATA%\postgresql\pgpass.conf`, one `hostname:port:database:username:password` line per entry, and say no permission check is made on Windows (the directory is assumed secure); `%APPDATA%` is inherited by SYSTEM, Administrators and the owner only (verified with `icacls`); the `postgresql` folder does not exist yet. The PostgreSQL JDBC driver source (REL42.7.7 and master) falls back to the password file when no password property is set; IntelliJ Database Tools include a `PgPassAuthProvider`. **Flyway 12.4.0 open-source does not authenticate with pgpass itself** (its reader only logs an upgrade message), so Flyway must not be assumed to read it. Spring Boot 4.1.2 builds Flyway's migration connection as a `SimpleDriverDataSource` from `spring.flyway.*`; with no password it passes none, so the JDBC driver's own fallback should apply. **NOT VERIFIED** until a real server exists: this is tested in Task 1.3 (and the application connection in Task 1.2). Residual risks: plain text at rest outside the project; the device is Azure AD joined (managed device, backups/scanners may see user-profile files).
- **Local password-hashing utility (SCRAM verifier method approved by the owner).** By owner decision the utility is **kept outside the repository, outside the project directory and out of every committed document**; this file records only that fact, never its name, location or source. It uses Node built-in modules only, has no file, network or process APIs, never prints or stores a password and has no password-writing function. **Disposable-password correctness test: PASSED 14 of 14** against in-memory PostgreSQL (the verifier is byte-for-byte identical to PostgreSQL's own for the same salt, and PostgreSQL stores a verifier we create unchanged). The test **can fail**: three deliberately broken copies of the algorithm were each caught. The utility has **not** been run with any real password; no role exists; no verifier or password has been written anywhere. A real login as a role from IntelliJ remains the final proof (NOT VERIFIED until the server exists).
- **Owner decisions:** execution order for Task 1.2 is installer (owner) → SCRAM script review → explain and validate the credential approach → approval before creating roles or databases → implementation. IntelliJ Database Tools only; connection structure approved: administrator, `hiresense_owner`, `hiresense_app`, with separate development and test databases; Password Safe for IDE credentials; manual transaction mode where appropriate.

### Task 1.2 preparation: owner decisions and IntelliJ review (2026-11-18)
- **Owner decisions for Task 1.2:** PostgreSQL **17.12**; IntelliJ IDEA Ultimate is the **only** database tool (no Docker, pgAdmin, DBeaver, Stack Builder, psql or other client); **two roles**: `hiresense_owner` (Flyway migrations) and `hiresense_app` (normal operation, restricted); database credentials via **IntelliJ run configurations only** (no Windows environment variables, no passwords in project files); **Spring Boot Actuator health** added during Phase 1 exposing only the health endpoint; no download, install or configuration without showing exact details and getting approval.
- **GitHub:** issue #17 closed as completed by the owner's approval (`gh issue close 17 --reason completed`; evidence of commit and push shown beforehand). Issues now: 33 open, 4 closed (#15, #16, #17, #18).
- **IntelliJ review (read-only):** no data source exists yet in the backend project or globally. Credential-storage provider is not set explicitly (`security.xml` absent), so the IDE default applies; the owner should check it in the Passwords settings. **The project SDK is `ms-25` (JDK 25, language level 25), not JDK 21**, so runs from the IDE would use a different Java than everything verified so far; the owner is asked to switch the project SDK and the Maven runner JRE to the installed JDK 21.0.13. Two Spring Boot run configurations exist; the older one still points at the removed class `com.example.hiresense.HireSenseApplication` and is stale, the newer one `HireSenseApplication (1)` points at `com.hiresense.HireSenseApplication`. `backend/.idea/` is git-ignored (confirmed for `workspace.xml` and data-source files).
- **Risk accepted by choosing Option B (to be confirmed with the owner):** environment variables set in IntelliJ run configurations are stored **in plain text inside `backend/.idea/workspace.xml`** (git-ignored, never committed unless `.gitignore` changes, but on disk in the project folder). Mitigation: unique, limited, localhost-only passwords; never share the `.idea` folder. Consequence: the AI cannot run database-dependent tests (it never sees the passwords); the owner runs them in IntelliJ and reports results.
- **Drafted, not run:** role and database SQL (no passwords inside), configuration plan (`spring.datasource.*` from `HIRESENSE_DB_URL`, `HIRESENSE_DB_APP_USER`, `HIRESENSE_DB_APP_PASSWORD`; separate `hiresense_test` database for tests; Flyway owner credentials added in Task 1.3), `.env.example`, `docs/database/local-setup.md`, Actuator health step after the DB connection works.

### Task 1.1 applied and verified; PostgreSQL install plan; issue #17 evidence (2026-11-18)
- **Changes applied (owner-approved, nothing staged or committed):** `backend/pom.xml` `groupId` → `com.hiresense` (`artifactId` kept `HireSense`, dependencies unchanged); the two Java classes moved by plain filesystem move to `com/hiresense/` with their `package` lines changed; the four empty `com/example` folders removed (checked empty first); `CLAUDE.md` backend run and test commands filled in, each with the honest note that they fail until Task 1.2. A search found no remaining reference to `com.example` outside build output and the IDE cache.
- **Verification on JDK 21.0.13 / Maven 3.10.1 after the change:** clean compile and package = **BUILD SUCCESS** (only `com/hiresense/HireSenseApplication.class` in `target/classes`; no stale old-package classes). `mvnw test`: 1 test, 1 error (`Failed to determine a suitable driver class`), identical to before the rename = expected, not green. Start-up: logger now `com.hiresense.HireSenseApplication`; 0 JPA repositories found; Tomcat initialises; stops at the missing datasource, identical to before = app **cannot run** until Task 1.2.
- **Owner choice: PostgreSQL via Option A (official Windows installer).** Nothing was downloaded or installed. Plan prepared from official sources: PostgreSQL 17.12-1 recommended (18.7-1 viable); EDB installer from `get.enterprisedb.com` (the packager the postgresql.org Windows page points to); SHA-256 and size taken from the winget manifest and response headers (checksums verified after any download); components to select: PostgreSQL Server only (exclude pgAdmin 4, Stack Builder and Command Line Tools); default directory `C:\Program Files\PostgreSQL\<major>`; port 5432 (confirmed free); superuser password typed only by the owner in the wizard; verification through IntelliJ's Database tools. No Docker, pgAdmin, DBeaver or other client. Support facts (postgresql.org versioning policy): 18 supported until Nov 2030, 17 until Nov 2029, 16 until Nov 2028. Spring Boot 4.1.2 manages Flyway 12.4.0 and PostgreSQL JDBC driver 42.7.14; support of Flyway/driver for PostgreSQL 18 could **not** be confirmed from official sources in this check, which is why 17 is recommended.
- **GitHub issue #17 evidence (read-only), issue left OPEN as instructed:** `docs/requirements.md` was introduced by commit `8162842`; `origin/main` = `HEAD` = `8162842`; GitHub's API shows the same commit as the latest to touch the file; blob id `fdd97938…` is identical locally, on `origin/main` and on GitHub; the working-tree file equals `HEAD`; GitHub shows `Version: 0.6`. Closing the issue is a GitHub write and waits for the owner.
- **Task 1.2 prepared** (plan only, see the owner message): add the PostgreSQL JDBC driver (version managed by Boot), datasource configuration via environment variables with no secrets, separate test database, `.env.example`, local setup document; blocked until a PostgreSQL server exists.

### Phase 1 start and Task 1.1 – Review existing Spring Boot project (2026-11-18)
- **Owner decision (explicit): start Phase 1 while Phase 0 remains `IN_PROGRESS`.** Phase 0 stays open until Tasks 0.8 and 0.9 and the remaining verification requirements are addressed; **no `phase-0` Git tag** yet. Phase 0 documentation work continues alongside Phase 1. This overrides `HIRESENSE.md` principle 14 / section 5 by the owner's choice.
- **Owner decision:** Task 0.7 is `COMPLETED` with reduced scope (labels, milestones, issues done; Project, fields, views, automation and sub-issue links deferred and must not block development).
- **Owner decision (C6):** change the placeholder base package `com.example.hiresense` to `com.hiresense` before any implementation. Affected files shown to the owner first; not applied yet.
- **Owner safeguards for Phase 1:** no PostgreSQL install without approval; no system configuration changes without approval; **no H2** as a substitute for PostgreSQL integration tests; approved database constraints are never weakened; no stage, commit or push without approval; resolve open points C1–C5 before implementing the tasks they affect; one task at a time with teaching.
- **Owner requirement (database tooling):** use PostgreSQL through IntelliJ IDEA's built-in Database tools (Database Explorer for connections and browsing, SQL console for queries). Preferred setup: IntelliJ Database Tools → PostgreSQL server → HireSense database. **Do not install** Docker Desktop, pgAdmin, DBeaver or any other database client or additional tool without explicit approval; PostgreSQL server installation also needs approval.
- **Finding – IntelliJ:** IntelliJ IDEA Ultimate 2026.2 (build 262.10968.63) is installed and running. The Database Tools plugin (`DatabaseTools`, with PostgreSQL dialect modules) is bundled and not disabled. The PostgreSQL JDBC driver is not bundled; IntelliJ normally downloads it on first connection (needs the owner's approval). Licence state was not inspected (the owner confirms in the IDE).
- **Finding – PostgreSQL server:** none available. No `psql`/`postgres` on PATH, no PostgreSQL service, nothing listening on 5432–5434, no installation folder, no WSL, no Docker. IntelliJ is only a client, so a server is needed before Task 1.2. Options were presented to the owner; nothing was installed.
- **Verification on JDK 21.0.13 / Maven 3.10.1 (existing project, unchanged):** compiled and packaged (47.7 MB jar) = **Compiled**. `mvnw test`: 1 test, 1 error ("Failed to determine a suitable driver class"; no datasource/driver) = not green, expected until Task 1.2. Starting the jar: Spring Boot starts, scans 0 repositories, initialises Tomcat on 8080, then **fails to start** for the same reason = **not manually verified as running**. Dependencies resolved (from the built jar): Spring Framework 7.0.10, Hibernate 7.4.13, Spring Data JPA 4.1.2, HikariCP 7.0.2, Jackson 3.1.7, Tomcat 11.0.27, Logback 1.5.38, Jakarta Persistence 3.2.0; **0 JDBC drivers**.
- **Honest status:** the generated skeleton currently cannot start. This predates Phase 1 (rule 6 "keep the app working" is knowingly violated until Task 1.2) and is recorded as a known issue.
- **Open conflicts to resolve before the affected tasks:** C1 acting-user identification without authentication (before 1.8–1.10); C2 roadmap API table vs decisions D-03/D-16/D-11/D-18 (before 1.8, 1.10); C3 PostgreSQL server and version (before 1.2); C4 repository tests against a separate local PostgreSQL test database, no H2 (before 1.2/1.13); C5 missing dependencies incl. Actuator health for NFR-019, each checked for Spring Boot 4.1.2 / Java 21 compatibility (before the task that adds them).
- Git (owner actions, observed read-only): commit `8162842` "Added ER Diagrams databases design and user stories" contains `HIRESENSE.md` and the four docs and was pushed (`origin/main` = `HEAD`). The staging area is empty. `PROJECT_PROGRESS.md` is modified but uncommitted. No Git command that changes state was run by the AI.
- Proposed Task 1.1 changes (not applied): rename package to `com.hiresense` (2 Java files moved by plain filesystem move, 2 `package` lines, `groupId` in `pom.xml`) and fill the `CLAUDE.md` commands table. `artifactId` `HireSense` left as is unless the owner decides otherwise.

### Task 0.7 – GitHub Projects board: planning (2026-11-18)
- Status `IN_PROGRESS`. **Nothing has been created or changed on GitHub**, and no Git command that changes state was run.
- Owner approved (2026-11-18): proposed structure (6 Status columns, fields Tier / Requirements / Verification, 13 labels, 13 milestones without due dates, 3 views); just-in-time scope of 37 issues (13 epics, Phase 0 tasks 0.1–0.9, Phase 1 tasks 1.1–1.13, 2 decision issues); Phase 0 history issues reflecting real status; **public** Project; creation with the GitHub CLI; issue text uses requirement IDs (no links to uncommitted documents); `PROJECT_PROGRESS.md` stays authoritative; a card is Done only when acceptance criteria and verification are met.
- Done so far: installed GitHub CLI 2.104.0 with `winget install GitHub.cli` (approved system change). Owner performs `gh auth login` personally; no token is ever requested or shown. Existing repository labels and settings are not to be deleted or changed. No commit, push or history rewrite.
- Prepared (scratch folder, not in the repo): the exact label, milestone and issue definitions (13 / 13 / 37) for the owner's review.
- Next: owner authenticates, I verify repository access and permissions (read-only), the owner confirms the definitions, then batches run in this order with the commands shown first: labels, milestones, issues, then Project, fields and items. Project views and built-in automation may need the browser.
- Owner clarifications (2026-11-18): (1) "Not applicable (docs)" added as a Verification field value for documentation-only tasks; (2) for Phase 1, investigate a **local PostgreSQL installation** instead of moving Docker Desktop forward, and ask before installing PostgreSQL or making any system change; (3) Phase 1 uses development seed/test resume records until upload exists in Phase 4, without bypassing the mandatory-resume rule (D-15). Reflected in the drafts of tasks 1.2 and 1.7.
- Authentication completed by the owner. Read-only checks (2026-11-18): account `yuktavyas14`, scopes `gist, project, read:org, repo`, permission `ADMIN` on the repository (PUBLIC, issues and projects enabled). Before any write: 10 default labels, 0 milestones, 0 issues/PRs, no Projects.
- **Batch 1 (labels) executed with owner approval:** created the 13 new labels only (`epic`, `task`, `user-story`, `decision`, `area:backend`, `area:frontend`, `area:database`, `area:security`, `area:infra`, `area:ai`, `area:testing`, `blocked`, `needs-decision`). Verified by listing GitHub afterwards: 23 labels total; the 10 existing labels are present and unchanged; all 13 new labels match the approved name, colour and description; milestones, issues and Projects are still 0. Nothing was edited or deleted. Cosmetic note: `area:ai` and the default `good first issue` share the same purple (7057FF).
- Issue definitions updated per owner clarifications: Task 0.4 stays In review until the documentation is **committed and pushed** with approval; Task 0.6 stays In review until the visual-rendering and integration verification gaps are addressed; Task 1.1 **reviews** the existing Spring Boot project and must not create a duplicate.
- **Batch 2 (milestones) executed with owner approval:** created the 13 milestones `Phase 0 – Planning and Setup` … `Phase 12 – Final Polish and Showcase` (GitHub milestone numbers 1–13, i.e. phase number + 1), open, **no due dates**. Verified by fetching GitHub's raw UTF-8 response and comparing with the approved text: 13 of 13 exact title matches, exactly one en dash (U+2013) in each title, descriptions equal, no due date, no issues attached, no extra milestones. Existing milestones: none (nothing to skip, nothing edited or deleted). Labels still 23, issues 0, Projects none.
- Batch 3 (37 issues) prepared in dry-run mode only (not executed): creation order epics #1–#13, Phase 0 tasks #14–#22, Phase 1 tasks #23–#35, decisions #36–#37; after creation, tasks 0.2, 0.3 and 0.5 are closed as completed. Payloads were built exactly as the script sends them and compared with the approved definitions: 37 of 37 identical, en dashes preserved. Sub-issue links (epic → tasks) are a separate follow-up step to be shown first. Waiting for the owner's approval of Batch 3.
- **Batch 3 (issues) executed with owner approval:** created the 37 approved issues (#1–#13 epics, #14–#22 Phase 0 tasks, #23–#35 Phase 1 tasks, #36 Q7, #37 DO-6) with no errors, then closed only #15 (Task 0.2), #16 (Task 0.3) and #18 (Task 0.5) as completed. Verified afterwards by fetching GitHub's raw UTF-8 response and comparing with the approved definitions: 37 of 37 match on title, body, labels, milestone and state; 34 open, 3 closed; no assignees, no comments, no pull requests, no extra issues. Milestone counts: Phase 0 = 7 open / 3 closed, Phase 1 = 15 open, Phase 2 = 2 open, Phases 3–12 = 1 open (their epic). Labels in use: epic 13, task 22, decision 2, needs-decision 2. A local receipt of created issue numbers was saved in the scratch folder. Nothing else was created or modified.
- Sub-issues support confirmed read-only (the sub-issues endpoint returns 200 for the epics). Proposed links (NOT created, awaiting owner approval): #1 ← #14–#22 (9 tasks), #2 ← #23–#35 (13 tasks); #36 and #37 stay standalone.
- Remaining after Batch 3: sub-issue links, public Project with fields and item statuses, views and automation (possibly via the browser). Project statuses (In review, Ready, etc.) do not exist yet because the Project has not been created; issue state alone shows Done as closed.
- Git note: the working-tree files are edited as work proceeds, but the Git index (staging area) is never touched by the AI. It was observed to change between checks without any AI Git command (staged → empty); the owner manages staging. Before any future commit the owner wants `git status`, `git diff`, `git diff --cached` and the staged-versus-unstaged difference of this file.

### Task 0.6 – FINAL SIGN-OFF (2026-11-18)
- Owner approved: the 12-table design, all DD decisions, normalized skills, UUID/BIGINT strategy, composite FK for resume ownership, RESTRICT/CASCADE rules, CHECK constraints, transactional status+history updates, private resume storage with opaque keys, and the PostgreSQL 16.4 (PGlite) 48/48 results as **preliminary**.
- Remaining decisions: **D-31 / Q9** hidden resumes do not count toward the limit of 5; used resumes are always retained; unused hidden resumes may be permanently deleted. **Q7** stays open for a later task. **DO-6** deferred until database roles are configured in Phase 1.
- Docs updated: `requirements.md` v0.6 (D-31, Q9 resolved, only Q7 open), `user-stories.md` v0.3 (US-C02/US-C03 reflect active-resume counting; 95 acceptance criteria), `database-design.md` v0.3, `er-diagram.md` v0.3.
- **Verification limitations kept in the documents:** (1) visual Mermaid rendering not verified; (2) Flyway migrations and JPA mappings not written or tested; (3) PGlite results are preliminary and must be repeated against the PostgreSQL version selected in Phase 1. Scratch files are not retained (owner confirmed).
- Re-checked after the final edits: every id cited in the stories, design and diagram exists in `requirements.md`; Mermaid parser still accepts the diagram.

### Task 0.6 – revision v0.2: approvals and verification (2026-11-18)
- Approved by owner: normalized skills (`skills`, `job_skills`, `candidate_skills`); `recruiter_profiles` (MVP business rule stays **one recruiter per company**, enforced in the service layer; no `UNIQUE` on `company_id` so the schema stays flexible); employment types `FULL_TIME`/`PART_TIME`/`CONTRACT`/`INTERNSHIP`; UUID for API-facing entities and BIGINT for logs and suitable lookup tables; database CHECK constraints, foreign keys and `ON DELETE` actions, with status transitions kept in the Spring Boot service layer. All DD-01..DD-15 are now tagged [A].
- Documentation added to `docs/database/database-design.md` (now v0.2): section 8 transaction requirements (status change + history in one transaction, optimistic lock answering 409, apply/register/delete flows, tests Phase 1 must include); section 9 MVP business rules vs future schema flexibility; history row contents mapped to columns; explanation of the composite foreign key and the `MATCH SIMPLE` NULL trap; resume storage stays private (opaque `storage_key`, no URL).
- **Verification actually run** (all in a temporary scratch folder outside the repo; nothing added to the project, no project database touched, no migration run):
  - Mermaid: parsed with the real parser (`mermaid` 11.4.1 + `jsdom`, installed with `--ignore-scripts` in the scratch folder): diagram accepted as type `er`; a deliberately broken copy was rejected. Re-run after the final edits: still OK.
  - PostgreSQL logic: throwaway DDL written from the design ran on PostgreSQL 16.4 (PGlite, in memory). **48/48 checks passed**, covering the composite foreign key (another candidate's resume rejected), mandatory resume, `RESTRICT` on job and used resume, unique and CHECK rules, mandatory reject/reopen reason, `job_skills` cascade, audit row surviving a job deletion, transaction rollback of a failed history insert, optimistic-lock stale update, and two recruiters allowed in one company.
  - Mutation check: three deliberately weakened schemas (plain FK instead of composite, `CASCADE` instead of `RESTRICT`, reason CHECK removed) were each detected, so the passes are meaningful.
- **Not verified:** the diagram has **not been visually rendered** (needs a browser); not a Flyway migration; no JPA/Hibernate mapping; no performance or `EXPLAIN` on real data; service-layer rules not implemented; PostgreSQL 16.4 in PGlite may differ from the version chosen in Phase 1/4.
- Open: Q7 (retention, deferred by owner), Q9 (do hidden resumes count toward 5; unused resumes physically deleted), DO-6 (should the database itself prevent edits to history and audit tables; decide with database roles in Phase 4).

### Task 0.6 – ER diagram and database design (2026-11-18)
- Created `docs/diagrams/er-diagram.md` (Mermaid ER diagram: 12 entities, 15 relationships) and `docs/database/database-design.md` (tables, columns, FK actions, constraints, indexes, normalization review, 15 design proposals DD-01..DD-15, traceability, future tables, open design questions DO-1..DO-5). Design only: no SQL migration, no entity code, no database touched.
- The six roadmap entities map to `users`, `candidate_profiles`, `companies`, `jobs`, `applications`, `resumes`. Added: `recruiter_profiles`, `application_status_history`, `audit_log`, `skills`, `job_skills`, `candidate_skills`.
- Approved decisions expressed in the schema: D-15 (`applications.resume_id` NOT NULL), D-16 (`ON DELETE RESTRICT` job→applications), D-21 (RESTRICT + `resumes.hidden_at`), D-13 (nullable phone), D-12 (history and audit tables), D-17 (`storage_key`, never a URL), D-06 (composite FK so a resume belongs to the applicant).
- Checks done: every FR/NFR/D id cited exists in `requirements.md`; a structural lint of the Mermaid block passed (12 entities, 15 relationships, 0 problems; this is my own script, not the real Mermaid parser).
- **Not verified:** diagram not rendered in a viewer; no SQL ever run against PostgreSQL. Real verification starts in Phase 1 (Flyway + repository tests that try to violate each constraint).
- Small edit to `docs/requirements.md` section 6 to point to proposal DD-13 (`full_name` on `users`).
- Awaiting your approval of: DD-01..DD-15 (especially DO-1 normalized skills, DO-2 `recruiter_profiles`, DO-3 employment types).

### Task 0.5 – Roles and user stories, finalized (2026-11-18)
- Owner approved with decisions D-26..D-30 (Q18, Q19 resolved; admins cannot see applicants or resumes; role change at next login; offer decisions final). Requirements v0.4 → v0.5, user stories v0.1 → v0.2.
- Traceability re-checked by script: every FR/NFR/D id cited in the stories exists in the requirements; 31 MVP stories, 94 Given/When/Then criteria; no remaining references to Q18/Q19.
- Remaining open questions: Q7 (retention policy, deferred by owner) and Q9 (do hidden resumes count toward 5; are unused resumes physically deleted).

### Task 0.5 – Roles and user stories (2026-11-18)
- Created `docs/user-stories.md` v0.1 (documentation only; no code, no Git changes).
- Contents: roles (Visitor, Candidate, Recruiter, Admin), a permission matrix (who may do what, with 401/403 rules), a table of who may perform each application status transition, 31 MVP user stories (5 visitor, 3 registered-user, 8 candidate, 11 recruiter, 4 admin) with 94 Given/When/Then acceptance criteria, 8 Planned stories, a traceability table to FR IDs and open points.
- Checked: every FR/NFR/D id cited in the stories exists in `requirements.md` (script check).
- Not verified: planning document, awaiting your review.

### Task 0.4 – Requirements, revision v0.4 (2026-11-18)
- Updated `docs/requirements.md` v0.3 → v0.4 after your review (documentation only).
- New decisions: D-18 candidate answers offers (recruiter issues them); D-19 UTF-8 byte-length check before BCrypt, clear error, never truncate; D-20 only the owning recruiter reopens; D-21 removing a used resume only hides it; D-22 anonymous visitors see job details and basic company info only; D-23 no reopening once the job is closed; D-24 admins cannot download resumes; D-25 rejection reason mandatory.
- Resolved questions: Q8, Q12, Q13, Q14, Q15, Q16, Q17. New questions: Q18 (what can still happen to applications of a closed job; proposed: pipeline continues, only reopening blocked), Q19 (are reasons visible to candidates; proposed: no).
- Still open: Q7 (retention policy, owner will finalize later), Q9 (do hidden resumes count toward 5; unused resumes physically deleted), Q18, Q19. Tag counts: 84 [A], 17 [P], 15 [S]. 25 decisions, 21 functional requirements with ACs, 40 NFRs.
- Checked by search: no stale references to resolved questions or "OFFER is final".

### Task 0.4 – Requirements, revision v0.3 (2026-11-18)
- Updated `docs/requirements.md` v0.2 → v0.3 after your review (documentation only; no code, no Git changes).
- Approved proposals now tagged [A]: password 8–64 chars + common-password blocklist + no special-character rules (D-08); 5 MB and 5 resumes (D-09); forward-only workflow, rejection from `APPLIED`/`SHORTLISTED`/`INTERVIEW` (D-10); reopen needs authorized recruiter + mandatory reason + returns to previous status (D-11); audit history for status changes and admin deletions (D-12).
- Changes: phone optional, name and email mandatory (D-13); `ACCEPTED`/`DECLINED` after `OFFER`, new FR-037 (D-14); resume mandatory when applying (D-15); jobs with applications cannot be permanently deleted until retention rules exist (D-16); resumes in a private bucket, authorized downloads only, no public URLs (D-17).
- Database implications updated: 7 application statuses, `Application.resume_id` NOT NULL with `ON DELETE RESTRICT`, `phone` nullable, `AuditLog` table with no FK to deleted rows, reopen reason mandatory in status history, `Resume` stores `storage_key` never a URL.
- New proposals/questions raised: who sets `ACCEPTED`/`DECLINED` (proposed: the candidate, Q13); BCrypt 72-**byte** limit vs 64 characters (Q15); admin resume access (Q16); hidden-resume handling (Q9); reopen on a closed job (Q14).
- Still open: Q7 (retention policy, deferred by owner), Q8, Q9, Q12, Q13, Q14, Q15, Q16, Q17. Tag counts: 69 [A], 19 [P], 18 [S]. 21 functional requirements with ACs, 40 NFRs, 17 decisions.
- Not verified: planning document, awaiting your consistency check. Checked by search for stale/contradictory phrases (none left).

### Task 0.4 – Requirements, revision v0.2 (2026-11-18)
- Updated `docs/requirements.md` v0.1 → v0.2 with owner decisions D-01..D-07 (documentation only; no code changed).
- Decisions: anonymous job browsing with login required to apply (D-01); controlled status workflow, reopen only by authorized recruiter (D-02); close jobs, admin-only permanent deletion subject to retention rules (D-03); password min 8 chars + secure hashing (D-04); candidate registration needs name, email, basic contact, resume optional (D-05); multiple resumes, one chosen per application (D-06); AI advisory only (D-07).
- New requirements: FR-036 (reopen), FR-042 (manage resumes), FR-052 (admin delete job), NFR-038 (public endpoints), NFR-039 (audit), NFR-040 (AI transparency). Changed: FR-001, 005, 010, 030–035, 040, 041, NFR-005, 007, 018, 021, 022, 032, 035, 036.
- Added status tags **[A]** approved / **[P]** proposal / **[S]** assumption, a "Design implications for the database" section (input to Task 0.6) and an open-questions table (Q1,Q3–Q6 answered; Q2a, Q7–Q12 open).
- Proposals still needing approval include: phone as mandatory contact field, 64-char password maximum, common-password check, 5 MB / 5 resumes limits, the exact status workflow (no backward steps), reason required on reopen, reopen returns to prior status, audit log.
- Final document size: 20 detailed FRs listed with ACs (19 MVP + FR-051 Planned) plus 8 Planned FRs in a table, 40 NFRs. Tag counts: 37 [A], 18 [P], 11 [S].
- Not verified: planning document, awaiting your review of v0.2.

### Task 0.4 – Requirements (2026-11-18)
- Created `docs/requirements.md` (documentation only; no code or other files changed).
- Contents: scope and out-of-scope, 3 roles, 10 modules, 17 detailed functional requirements with acceptance criteria (FR-001..FR-050 MVP, FR-051 Planned), 8 Planned requirements listed briefly (FR-060..FR-080) and 5 Future ideas (FUT-001..005), 37 non-functional requirements (NFR-001..NFR-037) covering performance, security, reliability, responsible AI, maintainability, usability and privacy, plus traceability, assumptions and 6 open questions (Q1–Q6).
- Key decisions recorded in the doc: MVP/Planned/Future tiers mapped to roadmap phases; one role per account and one company per recruiter (MVP); stateless JWT backend; close-instead-of-delete for jobs (to confirm in Phase 1); AI never decides hiring (NFR-021); privacy treated as a requirement.
- Numeric targets (e.g. 500 ms, 70% coverage) are goals only, not measured.
- Not verified: it is a planning document; it has not been reviewed by you yet.

### Task 0.3 – Folder structure (2026-11-18)
- Decision: keep `backend/` and `ui/` (deviation from roadmap names `hiresense-monolith` / `hiresense-ui`; avoids breaking builds and history).
- Created (6 files, each with a purpose README so Git can track the folder): `docs/adr/`, `docs/database/`, `docs/diagrams/`, `docs/api/`, `docs/learning-notes/`, `infra/`. No existing file moved, renamed or deleted.
- Verified: backend `package -DskipTests` builds `HireSense-0.0.1-SNAPSHOT.jar` (the skip is a command-line flag only; no test was disabled in code). Backend tests: 1 run, 1 error (unchanged `contextLoads`, Task 1.2). Frontend build OK; frontend tests 2/2 passed.
- Uncommitted: `.gitignore`, `PROJECT_PROGRESS.md` (modified); `docs/`, `infra/` (new). Nothing staged.

### Task 0.2 – GitHub repository (2026-11-18)
- Repo already exists: `yuktavyas14/HireSense-AI-Powered-Recruitment-Platform`; name kept (user decision).
- Verified: visibility is **public** (GitHub API `private:false`); `origin` fetch/push URLs match; branch `main`; local `HEAD` equals `origin/main` (79d3a50), so nothing unpushed.
- Edited `.gitignore` (+ `ui/.angular/`). No Git state-changing commands run; nothing staged or committed.
- Uncommitted changes: `.gitignore`, `PROJECT_PROGRESS.md`.

### Task 0.1 – Development tools (2026-11-18)
- Installed Eclipse Temurin JDK 21.0.13 via winget (user-approved). Java 11 and 17 left untouched.
- Verified: `mvnw -version` runs on Java 21.0.13 (Maven 3.10.1 wrapper); Node 20.20.2, npm 10.8.2, Git 2.55.0, VS Code, Postman present.
- Compiled: `mvnw clean verify` compiles the backend on JDK 21.
- Tested (backend): NOT passing – 1 test, 1 error (missing datasource, see Known Issues). Deliberately left failing until Task 1.2 (user decision).
- Verified (fresh environment): `java -version` = 21.0.13 and `mvnw -version` = Java 21.0.13.
- Frontend: `npm run build` succeeded (Angular 21, 214 kB initial bundle). `npm test -- --no-watch`: 1 file, 2 tests passed (Vitest). Not manually run in the browser.
- Docker Desktop deferred to Phase 4 by decision.

### Initial Setup
- Created project progress tracker.
- Created initial development roadmap.
- No application features implemented yet.

---

## 10. Rules for Updating This File

1. Update the current phase whenever development moves forward.
2. Update task status after completing each meaningful task.
3. Never mark tasks completed without implementation.
4. Never mark verification complete without running relevant tests.
5. Record important concepts learned.
6. Record blockers immediately.
7. Record technical decisions.
8. Record completed features.
9. Update Git history only after approved Git operations.
10. Keep the next task clearly identified.
11. Never delete historical progress.
12. Maintain accurate progress percentages.

---

*End of Project Progress Tracker.*
