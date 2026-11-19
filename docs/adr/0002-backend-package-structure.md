# ADR-0002: Backend package structure (modular monolith, package-by-feature)

- **Status:** Accepted, with the items in section 4 recorded as **provisional** (not decisions) and the items in section 6 explicitly **not decided here**.
- **Date:** 2026-11-19
- **Task:** 1.4 – Design backend package structure
- **Deciders:** project owner
- **Related:** ADR-0001 (modular monolith first; planned for Task 0.8, not yet written), `docs/requirements.md`, `docs/database/database-design.md`, `HIRESENSE.md` (Phases 1 and 5)

---

## 1. Context

The roadmap (Phase 1) asks for one Spring Boot application ("modular monolith") with these modules: User, Candidate,
Recruiter, Company, Job, Application, Resume, Common and Configuration. Microservices are introduced only in Phase 5,
by transforming the working monolith into independently deployable services.

The package structure has to:

- keep the layered architecture Controller → Service → Repository (roadmap Phase 1, Backend Rules);
- keep business rules in the service layer, where they can be unit-tested (database design, section 5.2; NFR-023);
- make module boundaries visible, so the later Phase 5 split follows lines that already exist;
- stay small: no technology, dependency or abstraction before a task needs it.

Facts this ADR relies on:

- the 12 tables in `docs/database/database-design.md` (users, candidate_profiles, recruiter_profiles, companies, jobs,
  applications, resumes, application_status_history, audit_log, skills, job_skills, candidate_skills);
- decisions D-03 and D-16 (jobs are closed, not deleted), D-11 (reopen), D-18 (only the candidate answers an offer),
  D-22 (public company basics), D-24 and D-28 (admins cannot see applicants or resumes).

The current code has only `com.hiresense.HireSenseApplication`; no business class exists yet.

---

## 2. Decision

### 2.1 Package-by-feature, with layers inside each feature

Code is grouped by business module (`job`, `application`, ...). Inside a module the code is grouped by layer. All code
lives under `com.hiresense`.

### 2.2 Package tree

```
com.hiresense
├── HireSenseApplication
├── common/
│   ├── error/        ErrorResponse, ApiException and generic NotFound / Conflict / BusinessRule exceptions,
│   │                 GlobalExceptionHandler (@RestControllerAdvice)
│   ├── persistence/  AuditableEntity (createdAt, updatedAt, version)
│   ├── web/          PageResponse<T>, paging and sorting helpers
│   ├── identity/     PROVISIONAL (see 4.1): who is acting. Its content depends on decision C1.
│   └── util/         created only when two or more modules need the same helper
├── config/           global Spring configuration: OpenAPI (Task 1.12), JPA auditing;
│                     later Security and CORS (Phase 2)
├── user/             accounts
├── skill/            normalized skill names
├── company/          company information
├── recruiter/        recruiter profile: links a user to a company
├── candidate/        candidate profile and candidate skills
├── resume/           resume metadata (file storage arrives in Phase 4)
├── job/              job postings and their skills
└── application/      applications and their status history
```

Not created now: `auth` (Phase 2), an `audit` module for administrator deletions (needs the admin role), and
`notification` (Phase 6). The tree above is the target; packages are created only as described in 2.8.

### 2.3 Module responsibilities and dependencies

| Module | Owns tables | Responsibility | May depend on |
|---|---|---|---|
| `user` | `users` | Account, role, enabled flag | `common` |
| `skill` | `skills` | Find-or-create a skill by name (one skill per name, ignoring case) | `common` |
| `company` | `companies` | Company data; public basic fields (D-22) | `common` |
| `recruiter` | `recruiter_profiles` | Which company a recruiter belongs to; the MVP rule "one recruiter per company" (a service rule, not a schema limit) | `user`, `company` |
| `candidate` | `candidate_profiles`, `candidate_skills` | Candidate profile and skills | `user`, `skill` |
| `resume` | `resumes` | Resume metadata; at most 5 active; hide and delete rules | `candidate` |
| `job` | `jobs`, `job_skills` | Job create, update, open/close, filtering; ownership by company | `company`, `recruiter`, `skill` |
| `application` | `applications`, `application_status_history` | Apply, duplicate prevention, status workflow, history | `job`, `candidate`, `resume` |

Every module may also depend on `common` and `config`. Nothing depends on `application`.

### 2.4 Layout inside a module

```
job/
├── web/          controller, request and response DTOs, mapper
├── service/      business rules and @Transactional boundaries
├── repository/   Spring Data repositories
├── domain/       JPA entities, enums, value objects
└── api/          the only part other modules may use: a small interface and read-only DTOs
```

`api` is created only when another module needs the module.

### 2.5 Dependency rules

1. **Within a module:** `web` → `service` → `repository` (and `domain`). A controller never calls a repository. A
   repository knows nothing about HTTP. Calls go down only.
2. **Between modules:** communication goes **only through the target module's public `api`**. A module never imports
   another module's `web`, `service`, `repository` or `domain` packages.
3. **No direct repository-to-repository access across modules.** A module never queries another module's tables or
   repositories; it asks that module's `api`.
4. **No circular module dependencies.** The allowed direction is the one in 2.3. If two modules seem to need each other,
   the design is changed (for example by moving a concept into a third module); the cycle is not accepted.
5. **Entities never cross a module boundary.** Other modules receive small read-only DTOs (for example
   `JobSummary(id, status, companyId)`). DTOs never enter repositories.
6. **`common` depends on no business module.**

### 2.6 References between modules (decision D1 = option B)

- Inside one module, JPA associations are used (for example `Job` ↔ `JobSkill`, `Application` ↔ status history).
- Across modules, an entity stores only the **id** of the other module's entity (for example `Application` holds
  `jobId`, `candidateId`, `resumeId`). Data from another module is fetched through that module's `api`.
- The approved database schema and its foreign keys are **unchanged**. This rule concerns the Java mapping only.
- Because ids are used, a service that needs friendly errors checks the referenced entity through the other module's `api`
  and does not rely on the database foreign key alone.

### 2.7 Shared code and configuration

- **DTOs** belong to the module that owns the data (in its `web` package). Business DTOs are not shared. Only generic ones
  (`PageResponse`, `ErrorResponse`) live in `common`.
- **Exceptions:** generic ones live in `common.error`. A rule-specific exception (for example an invalid status
  transition) lives in the module that owns the rule and extends a common base.
- **Utilities:** `common.util` is created only when a second module needs the same helper. It must not become a dumping
  ground.
- **Configuration:** global Spring configuration lives in `config`. Configuration that belongs to a single module (for
  example resume storage settings in Phase 4) stays in that module.
- **`common.identity` is provisional.** Whether it exists and what it contains depends on decision C1 (section 4.1). Until
  C1 is finalized, nothing in this ADR commits to its shape.

### 2.8 Creation policy

No empty packages, placeholder classes or `package-info.java` files are created for Task 1.4. Each package is created when
the task that needs it first adds a real class to it (Tasks 1.5 to 1.10 and later).

### 2.9 Enforcement

Module rules are enforced by **convention and code review**. An automated architecture test (ArchUnit) is planned for
**Phase 9**. No dependency is added now. The owner accepts knowingly that, until then, a violation is caught only by review.

---

## 3. Options considered

| Option | Why not chosen |
|---|---|
| **Package-by-layer** (`controller/`, `service/`, `repository/`, `entity/` for the whole application) | Simple at first, but every feature touches four folders, nothing marks a module boundary, and it gives no preparation for Phase 5. |
| **JPA associations across modules** (decision D1 option A) | Least code and one SQL join, but modules become glued together, cycles are tempting, entities leak into other modules, and the later split becomes more expensive. Starting strict (ids) is easier to relax than starting loose is to tighten. |
| **`skill` inside `common`, or inside `job`** | `common` must stay free of business logic; inside `job`, `candidate` would depend on `job`, the wrong direction. |
| **`recruiter` merged into `company`** | `company` would depend on `user`, and recruiter-specific rules would sit in company code. |
| **Architecture test (ArchUnit) now** | Would add a dependency before the structure has any classes to protect. Deferred to Phase 9 by the owner. |

---

## 4. Provisional leanings (NOT decisions)

These were recorded on 2026-11-19. They are **not final**, and they **must be finalized before Task 1.8**. Until then this
ADR does not rely on them, except that `common.identity` is reserved as a possible place for C1 (section 2.7).

### 4.1 C1 – who is the acting user before Phase 2 security?

Leaning: a development-only mechanism (a header) read through one `CurrentActor` provider, with an `Actor` (id and role)
passed into services as a parameter. In Phase 2 the provider is replaced by the authenticated principal and the services do
not change. The mechanism would be limited to local development and removed in Phase 2. Alternatives considered: the user id
in the URL or body, bringing Spring Security forward, or no actor until Phase 2.

### 4.2 C2 – roadmap API list versus approved decisions

- **Jobs:** `DELETE /api/jobs/{id}` conflicts with D-03 and D-16 (close instead of delete; a job with applications cannot
  be permanently deleted). Leaning (option c): recruiters close a job through a status endpoint
  (`PATCH /api/jobs/{id}/status`, `OPEN`/`CLOSED`); administrator hard delete is added only when the admin role exists
  (Phase 2) and answers 409 when applications exist.
- **Applications:** a single `PATCH /api/applications/{id}/status` cannot express "reopen to the previous status" (D-11),
  "only the candidate accepts or declines" (D-18), or the mandatory reasons for reject and reopen (D-11, D-25). Leaning
  (option c): keep the PATCH for forward moves and reject, and add `POST /api/applications/{id}/reopen` and a candidate
  `POST /api/applications/{id}/offer-response`.
- **Open points to decide with C2:** the body of `POST /api/jobs/{id}/apply` (`resumeId`, D-15), and an endpoint for a
  recruiter to list the applicants of a job (absent from the roadmap list).

---

## 5. Consequences

**Good**

- Clear module boundaries; services are easy to unit-test because other modules can be replaced by a fake `api`.
- The Phase 5 split follows lines that already exist.
- Business rules stay in one place per module.

**Costs**

- More code at module boundaries: calls through an `api` instead of reading a field.
- Screens that need data from several modules need several calls or a purpose-built read query. List screens need batch
  methods so that a list of 20 items does not cause 20 calls.
- Less practice with JPA associations between modules (they are still practised inside modules).

**Phase 5, according to the roadmap**

- Phase 5 is, by the roadmap, the point where the working monolith is **transformed** into independently deployable services
  (`api-gateway`, `user-service`, `job-service`, `application-service`), each with its own database. Until Phase 5 begins,
  nothing in this ADR introduces a distributed service, a network call between modules, or a second database.
- This structure is only a preparation: if modules already communicate through ids and a small `api`, then in Phase 5 a
  local call behind an `api` can be replaced by a REST call, and the Java mapping does not need to be rewritten.
- Things that Phase 5 will have to handle, and which are **not decided here**:
  - joins across modules become calls or copies of data;
  - the composite foreign key that stops an application from using another candidate's resume (design decision DD-07)
    cannot span two databases and becomes a check in code;
  - `skills` is used by both `job_skills` and `candidate_skills`; which service owns it is decided in Phase 5 (Task 5.1,
    service boundaries);
  - which services own `company`, `recruiter` and `resume` is likewise decided in Task 5.1.

**Risk**

- Module rules are enforced only by review until Phase 9 (section 2.9). The structure can drift unnoticed in the meantime.

---

## 6. Not decided here

- Everything in section 4 (C1 and C2).
- Libraries such as Lombok, MapStruct and springdoc (decision C5): each is decided, and checked for compatibility with
  Spring Boot 4.1.2 and Java 21, when it is first needed.
- Service boundaries and data ownership in Phase 5.
- Introduction of an architecture test library (planned for Phase 9).

---

## 7. References

- `docs/requirements.md`: D-03, D-11, D-15, D-16, D-18, D-22, D-24, D-25, D-26, D-28.
- `docs/database/database-design.md`: tables (section 1), business rules in the service layer (section 5.2), design decisions
  (section 10, including DD-07).
- `HIRESENSE.md`: Phase 1 (modular monolith, layered architecture, DTOs, REST rules) and Phase 5 (microservices).
- ADR-0001 (planned, Task 0.8): modular monolith first.
