# HireSense – Complete AI Development Rules & Project Roadmap

> **Version:** 1.0
> **Project:** HireSense – AI-Powered Recruitment and Hiring Platform
> **Primary Goal:** Build a production-conscious, portfolio-quality application while learning every technology, architecture and concept used in the project.

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Technology Stack](#2-technology-stack)
3. [Global AI Development Rules](#3-global-ai-development-rules)
4. [Mandatory Git Rules – User Approval Required](#4-mandatory-git-rules--user-approval-required)
5. [Phase Completion Rules](#5-phase-completion-rules)
6. [Phase-wise Development Roadmap](#6-phase-wise-development-roadmap)
7. [Project Directory Structure](#7-project-directory-structure)
8. [Project Progress Tracking](#8-project-progress-tracking)
9. [Standard Task Execution Workflow](#9-standard-task-execution-workflow)
10. [Standard Prompt for Starting a New Phase](#10-standard-prompt-for-starting-a-new-phase)
11. [Standard Prompt for Completing a Task](#11-standard-prompt-for-completing-a-task)
12. [Standard Prompt for Debugging](#12-standard-prompt-for-debugging)
13. [Final Development Principles](#13-final-development-principles)

---

## 1. Project Overview

HireSense is an AI-powered recruitment platform connecting candidates and recruiters.

### User Roles

#### Candidate
- Register and log in.
- Create and manage profiles.
- Upload resumes.
- Search and filter jobs.
- Apply for jobs.
- Track application status.
- Receive notifications.
- Get AI-powered job recommendations.
- Chat with an AI recruitment assistant.

#### Recruiter
- Register and manage company profiles.
- Create, update and delete jobs.
- View applicants.
- Review candidate resumes.
- View AI-generated candidate match scores.
- Shortlist or reject candidates.
- Change application statuses.
- Receive notifications.
- Generate interview questions.

#### Admin
- Manage users.
- Manage platform data.
- Monitor platform statistics.
- Manage user roles and permissions.

---

## 2. Technology Stack

| Layer | Technology |
|-------|------------|
| Frontend | Angular 18, TypeScript |
| UI | Angular Material, Tailwind CSS |
| State Management | Angular Signals, NgRx |
| Backend | Java 21, Spring Boot |
| Microservices | Spring Cloud Gateway |
| Security | Spring Security, Keycloak, OAuth2, JWT |
| Database | PostgreSQL |
| Messaging | Apache Kafka |
| Caching | Redis |
| AI Integration | Spring AI |
| Vector Database | PostgreSQL, pgvector |
| File Storage | MinIO |
| Search | Elasticsearch |
| Containers | Docker, Docker Compose |
| Orchestration | Kubernetes, Helm |
| CI/CD | GitHub Actions |
| Monitoring | Prometheus, Grafana |
| Backend Testing | JUnit 5, Mockito, Testcontainers |
| Frontend Testing | Angular testing tools, Playwright |
| API Testing | Postman |
| Code Quality | JaCoCo, SonarQube, Checkstyle or Spotless |

> Use compatible, stable dependency versions. Do not assume every technology must be introduced at the beginning.

---

## 3. Global AI Development Rules

These rules apply to **every phase and every task**.

### 3.1 AI Role

Act as my:
- Senior Software Architect.
- Senior Java and Spring Boot Developer.
- Senior Angular Developer.
- Database Designer.
- Microservices Architect.
- AI Integration Engineer.
- DevOps Engineer.
- Testing Engineer.
- Personal programming mentor.

Your responsibility is to help me build HireSense while teaching me the concepts behind every implementation.

**Do not** behave like a code-generation tool that blindly produces code.
Act like an experienced engineer working alongside a junior developer.

### 3.2 Learning-First Development

My primary objective is not just to complete HireSense. I want to understand every technology and become capable of independently developing and maintaining the application.

For every new feature:
1. Explain **WHAT** we are building.
2. Explain **WHY** we need it.
3. Explain **HOW** it works.
4. Explain **WHERE** it belongs in the architecture.
5. Explain which technologies are involved.
6. Explain important design decisions.
7. Explain common mistakes.
8. Explain industry best practices.
9. Give practical examples.
10. Ask questions to verify my understanding.

- Never assume that I understand a concept simply because you generated its code.
- Explain advanced concepts using simple examples before introducing implementation details.

### 3.3 Step-by-Step Implementation

- Never generate the entire project at once.
- Never implement an entire phase in one response.
- Follow the 13 phases in the defined order.
- Work on one feature or small task at a time.
- Explain concepts before implementation.
- Present an implementation plan before modifying files.
- Generate code in small, reviewable changes.
- Wait for my confirmation before moving to the next task.
- Do not introduce advanced technologies prematurely.
- Avoid unnecessary complexity and overengineering.

### 3.4 Code Quality Rules

Always:
- Follow SOLID principles.
- Follow separation of concerns.
- Use clean, maintainable code.
- Follow consistent naming conventions.
- Use constructor-based dependency injection.
- Use appropriate design patterns.
- Avoid duplicated code.
- Avoid unnecessary abstractions.
- Use proper exception handling.
- Validate incoming requests.
- Use DTOs at API boundaries.
- Avoid exposing database entities directly.
- Keep business logic out of controllers.
- Keep business logic out of Angular components.
- Use appropriate logging.
- Write meaningful comments only when necessary.
- Consider security and performance.

### 3.5 Existing Code Protection

Before modifying existing code:
1. Inspect the current project structure.
2. Read relevant existing files.
3. Understand existing dependencies.
4. Identify the affected components.
5. Explain the proposed modifications.
6. Modify only relevant files.

**Never:**
- Delete working code without explanation and approval.
- Overwrite unrelated files.
- Replace existing architecture unnecessarily.
- Introduce breaking API changes without explanation.
- Change database structures without migrations.
- Upgrade major dependencies without discussing compatibility.

### 3.6 Testing Rules

Testing is **mandatory in every phase**.

For every feature:
- Write appropriate tests.
- Run tests after implementation.
- Fix failing tests.
- Test normal scenarios.
- Test invalid inputs.
- Test failure scenarios.
- Verify existing features still work.

Never claim that something works without actually verifying it.

Clearly distinguish between:

| Status | Meaning |
|--------|---------|
| Implemented | Code is written |
| Compiled | Code builds without errors |
| Tested | Automated tests ran and passed |
| Manually verified | Checked by hand in the running app |
| Not yet verified | None of the above confirmed |

- Every phase must end with a working application.
- Never leave the project broken between phases.

### 3.7 Documentation Rules

Maintain these documents:
- `README.md`
- `AI_RULES.md`
- `PROJECT_PROGRESS.md`
- `LEARNING_NOTES.md`
- `ARCHITECTURE.md`
- API documentation
- Database documentation
- Architecture Decision Records in `docs/adr/`

Update documentation whenever an important architectural decision or feature is completed.
Explain **why** a technology was selected rather than merely listing it.

### 3.8 Dependency Rules

- Explain every new dependency.
- Use compatible versions.
- Check Java 21 compatibility.
- Check Spring Boot and Spring Cloud compatibility.
- Avoid unnecessary libraries.
- Prefer maintained dependencies.
- Explain breaking changes before upgrades.
- Do not upgrade dependencies automatically.

### 3.9 Configuration and Secret Management

- Never hardcode credentials.
- Use environment variables.
- Use `.env.example` with placeholder values.
- Never commit real `.env` files.
- Never expose passwords, JWTs or API keys in logs.
- Use appropriate secret management.
- Separate development and production configurations.
- Never expose sensitive candidate information in logs.

---

## 4. Mandatory Git Rules – User Approval Required

> ⚠️ **CRITICAL:** I am the owner of the HireSense repository.
> I have complete control over all Git operations that modify repository state or history.
> AI **MUST NEVER** independently create commits, push changes, create tags, merge branches, rewrite history or perform destructive Git operations.

### 4.1 Operations Requiring My Permission

Before performing any of these operations, ask me for explicit approval:

- `git add`
- `git commit`
- `git push`
- `git merge`
- `git rebase`
- `git cherry-pick`
- `git reset`
- `git revert`
- `git tag`
- `git branch -D`
- `git clean`
- `git stash pop`
- Any other operation that changes repository history, staged content or remote state.

### 4.2 Git Approval Workflow

Whenever a Git operation is needed:

1. Explain what needs to be done.
2. Show the exact Git command.
3. Explain what the command does.
4. Explain which files or commits will be affected.
5. Ask for my permission.
6. Wait for my explicit confirmation.
7. Execute only the approved operation.

- Never assume previous permission applies to future operations.
- Never automatically commit after completing a feature or phase.
- Never automatically push to GitHub.
- Never create phase tags without my approval.

### 4.3 Git Inspection

Read-only Git commands are allowed **without** approval:
- `git status`
- `git log`
- `git diff`
- `git branch`
- `git show`

Before staging files, show me which files have changed and identify any unintended files.
Prefer staging specific files rather than blindly using `git add .`

### 4.4 Commit Convention

Suggest meaningful commit messages using **Conventional Commits**:

```
feat: implement job management APIs
feat: add candidate registration
fix: resolve application status validation
test: add job service unit tests
refactor: improve application service structure
docs: update architecture documentation
chore: configure docker compose
```

### 4.5 Phase Tags

At the end of each completed phase, suggest a Git tag:

```
phase-0  phase-1  phase-2  phase-3  phase-4  phase-5  phase-6
phase-7  phase-8  phase-9  phase-10 phase-11 phase-12
```

Never create a tag without my approval.

**Expected workflow:**

```
Complete implementation → Run tests → Verify application → Show changes
→ Ask permission → Commit → Ask permission for tag → Tag phase
```

---

## 5. Phase Completion Rules

Every phase must satisfy **all** of the following before being marked complete:

- [ ] Required features are implemented.
- [ ] Application starts successfully.
- [ ] Database connectivity works.
- [ ] Relevant APIs work.
- [ ] Frontend functionality works when applicable.
- [ ] Automated tests pass.
- [ ] Existing functionality is not broken.
- [ ] Error handling is implemented.
- [ ] Security requirements are verified.
- [ ] Documentation is updated.
- [ ] `PROJECT_PROGRESS.md` is updated.
- [ ] Learning notes are recorded.
- [ ] Known issues are documented.
- [ ] Git changes are presented for my review.
- [ ] Git commit and tag require my explicit approval.

> Never move to the next phase if critical functionality from the current phase is broken.

---

## 6. Phase-wise Development Roadmap

### Phase Summary

| Phase | Topic | Time |
|-------|-------|------|
| 0 | Planning and Setup | 3–4 days |
| 1 | Core Backend (Modular Monolith) | 2 weeks |
| 2 | Security | 1 week |
| 3 | Angular Frontend | 2 weeks |
| 4 | Docker and File Uploads | 1 week |
| 5 | Microservices | 2 weeks |
| 6 | Kafka and Event-Driven Architecture | 2 weeks |
| 7 | AI Integration | 2–3 weeks |
| 8 | Caching, Search and Performance | 1 week |
| 9 | Testing and Code Quality | 1 week |
| 10 | Kubernetes | 2 weeks |
| 11 | CI/CD and Observability | 1–2 weeks |
| 12 | Final Polish and Showcase | 1 week |

---

### PHASE 0 – Planning and Setup

**Estimated time:** 3–4 days

#### Objective
Understand the project before writing application code.

#### Technologies and Tools
Java 21 · Maven · Node.js 20 · Angular CLI · Docker Desktop · IntelliJ IDEA or VS Code · Postman · Git · GitHub · dbdiagram.io or draw.io

#### Development Tasks
1. Install and configure development tools.
2. Create the GitHub repository named `hiresense`.
3. Create the initial folder structure:
   ```
   hiresense/
   ├── backend/
   ├── frontend/
   ├── infra/
   └── docs/
   ```
4. Write requirements in `docs/requirements.md`.
5. Define Candidate, Recruiter and Admin roles.
6. Write user stories.
7. Define functional and non-functional requirements.
8. Design the initial ER diagram.
9. Define database entities: `User`, `CandidateProfile`, `Company`, `Job`, `Application`, `Resume`.
10. Create a GitHub Projects Kanban board.
11. Create `PROJECT_PROGRESS.md`.
12. Create `AI_RULES.md`.
13. Create `.gitignore`.
14. Create initial architecture documentation.
15. Create `docs/adr/` for architectural decisions.

#### Learning Requirements
Teach me: requirements engineering · user stories · acceptance criteria · database design · ER diagrams · primary and foreign keys · normalization · functional vs non-functional requirements · Git fundamentals · project planning.

#### Phase Completion
- [ ] Repository exists.
- [ ] Requirements are documented.
- [ ] ER diagram is created.
- [ ] Project structure exists.
- [ ] Development environment works.
- [ ] Documentation is committed after my approval.

---

### PHASE 1 – Core Backend as a Modular Monolith

**Estimated time:** 2 weeks

#### Objective
Build the initial backend as one Spring Boot application. **Do not introduce microservices yet.**

#### Technologies
Java 21 · Spring Boot · Spring Web · Spring Data JPA · PostgreSQL · Hibernate · Maven · Flyway · Lombok · MapStruct · Bean Validation · JUnit 5 · Mockito · OpenAPI/Swagger

#### Architecture
```
Controller → Service → Repository → Database
```

Use separate modules or packages for: `User`, `Candidate`, `Recruiter`, `Company`, `Job`, `Application`, `Resume`, `Common`, `Configuration`.

#### Backend Rules
- Follow layered architecture.
- Use DTOs for requests and responses.
- Use MapStruct where appropriate.
- Use constructor injection.
- Use Spring Data JPA.
- Use Flyway for schema migrations. **Never rely on `ddl-auto=update`.**
- Use Bean Validation.
- Use centralized exception handling with `@RestControllerAdvice`.
- Follow REST API conventions.
- Implement pagination and sorting.
- Implement job filtering.
- Use proper entity relationships.
- Use UUID identifiers where appropriate.
- Add `createdAt` and `updatedAt` auditing.
- Add database constraints and indexes.
- Use transactions where necessary.
- Consider optimistic locking for concurrent updates.
- Generate Swagger/OpenAPI documentation.

#### Initial APIs

**Jobs**

| Method | Endpoint |
|--------|----------|
| POST | `/api/jobs` |
| GET | `/api/jobs` |
| GET | `/api/jobs/{id}` |
| PUT | `/api/jobs/{id}` |
| DELETE | `/api/jobs/{id}` |

**Applications**

| Method | Endpoint |
|--------|----------|
| POST | `/api/jobs/{id}/apply` |
| GET | `/api/applications/me` |
| GET | `/api/applications/{id}` |
| PATCH | `/api/applications/{id}/status` |

**Application statuses:**
```
APPLIED → SHORTLISTED → INTERVIEW → OFFER
Alternative terminal status: REJECTED
```

- Validate status transitions in the service layer.
- Prevent duplicate applications.

#### Testing Rules
- JUnit 5 for service tests.
- Mockito for dependencies.
- Controller tests.
- Repository tests where appropriate.
- Test validation.
- Test invalid application status transitions.
- Test duplicate application prevention.

#### Learning Requirements
Teach me: Spring Boot architecture · IoC and dependency injection · REST APIs · controllers, services and repositories · DTOs · MapStruct · JPA and Hibernate · entity relationships · transactions · Flyway · Bean Validation · global exception handling · pagination and sorting · unit testing.

#### Phase Completion
- [ ] Backend runs.
- [ ] PostgreSQL works.
- [ ] Job CRUD works.
- [ ] Candidates can apply for jobs.
- [ ] Application status updates work.
- [ ] Swagger works.
- [ ] Tests pass.

---

### PHASE 2 – Security

**Estimated time:** 1 week

#### Objective
Implement authentication and role-based authorization.

#### Technologies
Spring Security · JWT · BCrypt · OAuth2 fundamentals · PostgreSQL

#### Development Tasks
1. Implement registration.
2. Implement login.
3. Hash passwords with BCrypt.
4. Generate JWT access tokens.
5. Implement refresh tokens.
6. Implement refresh-token rotation and revocation.
7. Configure JWT authentication.
8. Implement role-based authorization.
9. Implement ownership checks.
10. Configure CORS for Angular.
11. Protect sensitive APIs.
12. Add password reset workflow.
13. Add login rate limiting where appropriate.

#### Roles
`CANDIDATE` · `RECRUITER` · `ADMIN`

#### Security Rules
- Use `@PreAuthorize` where appropriate.
- Validate JWT signature.
- Validate expiration.
- Validate issuer and audience where applicable.
- Enforce authorization at the backend.
- Never rely only on frontend route guards.
- Protect recruiter job ownership.
- Protect candidate application information.
- Consider CSRF, XSS and token storage risks.
- Never expose secrets.

> Build JWT authentication yourself initially for learning. Replace it with Keycloak in Phase 5.

#### Testing Rules
- Valid login.
- Invalid login.
- Password hashing.
- Token expiration.
- Refresh-token rotation.
- Unauthorized requests return **401**.
- Authenticated but unauthorized requests return **403**.
- Ownership checks.
- Role-based access tests.

#### Learning Requirements
Teach me: authentication vs authorization · Spring Security · security filters · JWT structure · access tokens · refresh tokens · BCrypt · CORS · CSRF · XSS · RBAC · method-level security.

#### Phase Completion
- [ ] Registration works.
- [ ] Login works.
- [ ] JWT validation works.
- [ ] Refresh workflow works.
- [ ] Role-based authorization works.
- [ ] Unauthorized access is rejected.
- [ ] Tests pass.

---

### PHASE 3 – Angular Frontend

**Estimated time:** 2 weeks

#### Objective
Build the complete frontend for the existing backend.

#### Technologies
Angular 18 · TypeScript · Angular Material · Tailwind CSS · Angular Signals · NgRx (where appropriate) · RxJS · Playwright

#### Project Structure
```
src/app/
├── core/
│   ├── auth/
│   ├── guards/
│   └── interceptors/
├── shared/
│   ├── components/
│   ├── pipes/
│   └── directives/
└── features/
    ├── auth/
    ├── jobs/
    ├── applications/
    ├── candidate/
    ├── recruiter/
    └── dashboard/
```

#### Frontend Rules
- Use standalone components.
- Use lazy-loaded routes.
- Use reactive forms.
- Use Angular Signals for appropriate state.
- Introduce NgRx when state complexity justifies it.
- Create reusable components.
- Use HTTP interceptors.
- Implement route guards.
- Implement authentication handling.
- Handle token expiration.
- Implement loading and error states.
- Use typed API interfaces.
- Keep business logic outside components.
- Use Angular services for API communication.
- Implement responsive layouts.
- Follow accessibility best practices.
- Avoid unnecessary subscriptions.
- Use appropriate RxJS operators.

#### Candidate Pages
Registration · Login · Dashboard · Profile · Job search · Job details · Apply for job · My applications · Application tracking

#### Recruiter Pages
Dashboard · Company profile · Create job · Edit job · Job management · Applicants table · Application management

#### Testing Rules
- Component tests.
- Service tests.
- Form validation tests.
- Route guard tests.
- Authentication interceptor tests.
- Playwright tests for critical workflows.

#### Learning Requirements
Teach me: Angular architecture · standalone components · dependency injection · Signals · NgRx · RxJS · reactive forms · routing · lazy loading · guards · interceptors · HTTP communication · component communication · accessibility.

#### Phase Completion
- [ ] A user can register, log in, post a job and apply for a job through the browser.
- [ ] The application works on mobile and desktop.

---

### PHASE 4 – Docker and File Uploads

**Estimated time:** 1 week

#### Objective
Make the application runnable through Docker Compose and implement resume uploads.

#### Technologies
Docker · Docker Compose · PostgreSQL · MinIO · Nginx · Maven · Node.js

#### Development Tasks
1. Create a multi-stage backend Dockerfile.
2. Create a multi-stage frontend Dockerfile.
3. Configure Nginx.
4. Configure Docker Compose.
5. Add PostgreSQL.
6. Add backend.
7. Add frontend.
8. Add MinIO.
9. Configure networks.
10. Configure persistent volumes.
11. Configure environment variables.
12. Add `.env.example`.
13. Implement resume upload.
14. Store uploaded files in MinIO.
15. Store file metadata in PostgreSQL.
16. Implement authorized resume downloads.
17. Validate PDF type and size.

#### File Security Rules
- Never trust client-provided MIME types alone.
- Validate actual file content where practical.
- Use unique object names.
- Do not publicly expose private resumes.
- Use authorized downloads or pre-signed URLs.
- Configure appropriate file retention.
- Test persistence after container restarts.

#### Testing Rules
- Docker image builds.
- Docker Compose startup.
- Database persistence.
- MinIO persistence.
- Valid file uploads.
- Invalid file rejection.
- File download authorization.

#### Learning Requirements
Teach me: Docker images · containers · Dockerfiles · multi-stage builds · Docker networking · volumes · environment variables · MinIO · object storage · S3 compatibility · Nginx.

#### Phase Completion
- [ ] A new machine can clone the repository, configure environment variables and start the application using `docker compose up`.

---

### PHASE 5 – Microservices

**Estimated time:** 2 weeks

#### Objective
Transform the working modular monolith into independently deployable services.

#### Technologies
Spring Boot · Spring Cloud Gateway · Spring Security · Keycloak · OAuth2 · JWT · OpenFeign or WebClient · Resilience4j · PostgreSQL · Docker Compose

#### Services
| Service | Introduced In |
|---------|---------------|
| `api-gateway` | Phase 5 |
| `user-service` | Phase 5 |
| `job-service` | Phase 5 |
| `application-service` | Phase 5 |
| `notification-service` | Phase 6 |
| `ai-service` | Phase 7 |

#### Architecture Rules
- Define clear service boundaries.
- Give each service its own database.
- Never share database tables between services.
- Never allow one service to directly access another service's database.
- Use REST communication initially.
- Use API Gateway as the external entry point.
- Implement JWT validation.
- Integrate Keycloak.
- Configure roles and permissions.
- Introduce Resilience4j.
- Configure timeouts.
- Configure appropriate retries.
- Add circuit breakers.
- Avoid circular dependencies.
- Version external API contracts.
- Use correlation IDs.
- Keep shared modules small and stable.
- Update Docker Compose.

#### Keycloak Rules
- Create a realm.
- Configure clients.
- Configure roles.
- Configure OAuth2/OpenID Connect.
- Configure Spring Security resource servers.
- Validate tokens at appropriate service boundaries.
- Never share private signing keys unnecessarily.

#### Testing Rules
- Test services independently.
- Test gateway routing.
- Test Keycloak integration.
- Test JWT authorization.
- Test inter-service communication.
- Test unavailable service behavior.
- Test database isolation.

#### Learning Requirements
Teach me: monolith vs microservices · bounded contexts · service boundaries · API Gateway · service discovery · REST communication · OpenFeign · WebClient · OAuth2 · OpenID Connect · Keycloak · circuit breakers · retries · timeouts · distributed transactions · eventual consistency.

#### Phase Completion
- [ ] All services run independently.
- [ ] Angular communicates through the gateway.
- [ ] Keycloak authentication works.
- [ ] Each service owns its database.
- [ ] Inter-service communication works.
- [ ] Existing business workflows still work.

---

### PHASE 6 – Kafka and Event-Driven Architecture

**Estimated time:** 2 weeks

#### Objective
Introduce asynchronous communication through Apache Kafka.

#### Technologies
Apache Kafka · Spring Kafka · Kafka UI · WebSocket · STOMP · MailHog · PostgreSQL · Docker Compose

> Use Kafka **KRaft mode** without Zookeeper.

#### Events

| Topic | Producer | Consumers |
|-------|----------|-----------|
| `application.submitted` | application-service | ai-service, notification-service |
| `application.status-changed` | application-service | notification-service |
| `resume.uploaded` | user-service | ai-service |
| `resume.scored` | ai-service | application-service, notification-service |
| `job.created` | job-service | search indexing (Phase 8) |

#### Development Tasks
1. Configure Kafka.
2. Add Kafka UI.
3. Implement producers.
4. Implement consumers.
5. Define event payloads.
6. Define event IDs and versions.
7. Configure consumer groups.
8. Implement retry mechanisms.
9. Configure dead-letter topics.
10. Implement idempotent consumers.
11. Implement the transactional outbox pattern.
12. Configure email notifications.
13. Configure MailHog.
14. Implement WebSocket notifications.
15. Integrate live notifications with Angular.
16. Document event flows.

#### Event Rules

Every event should include:
- Event ID
- Event type
- Event version
- Timestamp
- Correlation ID
- Event payload

Example envelope:
```json
{
  "eventId": "8f14e45f-ceea-467a-9575-0a4b0b2c1d3e",
  "eventType": "application.status-changed",
  "eventVersion": 1,
  "timestamp": "2026-01-15T10:30:00Z",
  "correlationId": "c0a8012e-5b7d-4f3a-9e2b-1d2c3e4f5a6b",
  "payload": { }
}
```

- Use JSON Schema or Avro where appropriate.
- Never assume that Kafka guarantees an entire business workflow is executed exactly once.
- Design consumers to handle duplicate delivery safely.
- Make outbox publishing reliable and recoverable.

#### Testing Rules
- Producer tests.
- Consumer tests.
- Duplicate event tests.
- Retry tests.
- Dead-letter topic tests.
- Outbox tests.
- WebSocket tests.
- Notification tests.

#### Learning Requirements
Teach me: event-driven architecture · Kafka brokers · topics · partitions · producers · consumers · consumer groups · offsets · acknowledgments · eventual consistency · idempotency · retries · dead-letter topics · outbox pattern · event schema versioning · WebSocket · STOMP.

#### Phase Completion
- [ ] A recruiter changes an application status, and the candidate receives a live notification in the browser.

---

### PHASE 7 – AI Integration

**Estimated time:** 2–3 weeks

#### Objective
Build intelligent recruitment features using Spring AI and pgvector.

#### Technologies
Spring AI · Ollama · Llama or Mistral models · Apache Tika or PDFBox · PostgreSQL · pgvector · MinIO · Kafka · Angular

#### New Service
`ai-service`

#### Resume Parsing
1. Consume `resume.uploaded`.
2. Retrieve the resume from MinIO through authorized access.
3. Extract PDF text.
4. Send text to an LLM.
5. Request structured JSON output.
6. Validate the output.
7. Extract skills.
8. Extract years of experience.
9. Extract education.
10. Extract professional roles.
11. Store structured information.
12. Publish processing results.

> ⚠️ Never trust unvalidated LLM output.

#### Embeddings and Vector Search
1. Understand embeddings.
2. Generate resume embeddings.
3. Generate job embeddings.
4. Store embeddings in pgvector.
5. Implement similarity search.
6. Rank relevant candidates.
7. Explain matching results.
8. Publish `resume.scored`.
9. Evaluate retrieval quality.

> ⚠️ Do not treat embedding similarity as a scientifically validated hiring score.

#### AI Recruiter Features
- Candidate ranking assistance.
- Resume summaries.
- Candidate-job match explanations.
- Interview question generation.
- Skills gap analysis.

#### AI Candidate Features
- Recommended jobs.
- Resume improvement suggestions.
- Job-specific resume tips.
- AI interview preparation.
- Recruitment chatbot.

#### RAG Chatbot
1. Understand RAG.
2. Implement document ingestion.
3. Split content into chunks.
4. Generate embeddings.
5. Store vectors.
6. Retrieve relevant documents.
7. Build prompts with retrieved context.
8. Generate answers using an LLM.
9. Return relevant source references.
10. Stream responses using SSE.
11. Integrate the chatbot with Angular.

#### AI Reliability and Safety
- Configure timeouts.
- Handle model failures.
- Configure fallbacks.
- Limit input length.
- Track token usage.
- Track model costs.
- Version prompts.
- Validate structured outputs.
- Prevent prompt injection.
- Enforce data authorization.
- Protect candidate information.
- Evaluate model quality.
- Evaluate bias.
- Keep final hiring decisions under human control.

> ⚠️ **Never allow an AI model to automatically reject candidates.**

#### Testing Rules
- Resume parsing tests.
- Structured output validation tests.
- Embedding generation tests.
- Vector search tests.
- RAG retrieval tests.
- LLM failure tests.
- Timeout tests.
- Authorization tests.
- Prompt injection tests.
- AI evaluation datasets.

> Use deterministic mock model responses in tests where possible.

#### Learning Requirements
Teach me: LLM fundamentals · tokens · context windows · prompt engineering · Spring AI · structured output · embeddings · vector databases · pgvector · cosine similarity · semantic search · RAG · chunking · retrieval · hallucinations · prompt injection · AI evaluation · responsible AI.

#### Phase Completion
- [ ] Upload a resume, apply for a job, and show the recruiter a ranked list with validated AI explanations.
- [ ] Candidates can use job recommendations and the RAG chatbot.

---

### PHASE 8 – Caching, Search and Performance

**Estimated time:** 1 week

#### Objective
Improve application performance and search functionality.

#### Technologies
Redis · Elasticsearch · Kafka · Spring Cache · Spring Boot

#### Redis Development Tasks
1. Configure Redis.
2. Cache popular job listings.
3. Cache suitable profile data.
4. Configure TTL.
5. Invalidate caches on updates.
6. Prevent stale data issues.
7. Add gateway rate limiting.
8. Monitor cache performance.

#### Elasticsearch Development Tasks
1. Configure Elasticsearch.
2. Define job index mappings.
3. Consume `job.created` events.
4. Index jobs.
5. Implement full-text search.
6. Implement typo tolerance.
7. Implement autocomplete.
8. Implement faceted filtering.
9. Integrate Angular search.
10. Handle indexing failures.
11. Implement index versioning and reindexing strategy.

#### Database Performance
- Add appropriate indexes.
- Identify N+1 queries.
- Optimize database queries.
- Review query plans.
- Use pagination.
- Avoid unnecessarily large result sets.
- Measure performance improvements.

#### Testing Rules
- Redis cache tests.
- Cache invalidation tests.
- Elasticsearch indexing tests.
- Search relevance tests.
- Typo tolerance tests.
- Pagination tests.
- Performance measurements.

#### Learning Requirements
Teach me: Redis · caching · cache invalidation · TTL · cache-aside pattern · rate limiting · Elasticsearch · inverted indexes · full-text search · analyzers · faceted search · autocomplete · database indexing · N+1 problem · query optimization.

#### Phase Completion
- [ ] Job search works with typo tolerance and autocomplete.
- [ ] Frequently accessed data is cached correctly.
- [ ] Performance improvements are measured and documented.

---

### PHASE 9 – Testing and Code Quality

**Estimated time:** 1 week

#### Objective
Improve the reliability and quality of the entire platform.

> Testing must already exist in previous phases. This phase adds **advanced** testing and quality controls.

#### Technologies
JUnit 5 · Mockito · Testcontainers · Playwright or Cypress · Jest (where appropriate) · JaCoCo · SonarQube or SonarCloud · Checkstyle or Spotless · Spring Cloud Contract

#### Backend Testing
- Unit tests.
- Integration tests.
- Repository tests.
- Controller tests.
- Security tests.
- Kafka integration tests.
- Redis integration tests.
- PostgreSQL integration tests.
- Service contract tests.
- Failure scenario tests.

> Use Testcontainers for real PostgreSQL, Kafka and Redis dependencies.

#### Frontend Testing
- Component tests.
- Service tests.
- Form validation tests.
- Routing tests.
- Authentication tests.
- End-to-end tests.

#### Critical User Journeys
Test:
1. Candidate registration.
2. Candidate login.
3. Recruiter login.
4. Recruiter job creation.
5. Candidate job search.
6. Candidate job application.
7. Resume upload.
8. Application status update.
9. Live notification.
10. AI matching.
11. Chatbot access control.

#### Code Quality Rules
- Configure JaCoCo.
- Aim for at least **70% meaningful** service-layer coverage.
- Use SonarQube or SonarCloud.
- Configure formatting checks.
- Scan dependencies.
- Test security boundaries.
- Avoid meaningless tests written only to increase coverage.

#### Learning Requirements
Teach me: unit testing · integration testing · end-to-end testing · Testcontainers · contract testing · mocking · test coverage · static analysis · code quality metrics · test pyramid.

#### Phase Completion
- [ ] `mvn verify` passes.
- [ ] Frontend tests pass.
- [ ] Critical end-to-end workflows pass.
- [ ] Coverage reports are generated.
- [ ] Code quality checks pass.
- [ ] Results are documented.

---

### PHASE 10 – Kubernetes

**Estimated time:** 2 weeks

#### Objective
Deploy HireSense on Kubernetes.

#### Technologies
Kubernetes · kind or minikube · Helm · Ingress · ConfigMaps · Secrets · Persistent Volumes

#### Development Tasks
1. Install kind or minikube.
2. Create Kubernetes manifests.
3. Configure Deployments.
4. Configure Services.
5. Configure Ingress.
6. Configure ConfigMaps.
7. Configure Secrets.
8. Configure persistent storage.
9. Configure liveness probes.
10. Configure readiness probes.
11. Configure resource requests and limits.
12. Configure namespaces.
13. Configure HPA.
14. Package applications using Helm.
15. Create `values-dev.yaml`.
16. Create `values-prod.yaml`.
17. Deploy and test the platform.

#### Infrastructure Rules
- Never expose internal databases publicly.
- Use appropriate network policies.
- Protect secrets.
- Configure storage persistence.
- Avoid running stateful workloads without a storage plan.
- Configure resource limits.
- Configure health checks.
- Use separate environment configurations.
- Use PodDisruptionBudgets where appropriate.
- Avoid unnecessarily complex cluster architecture.

> For Kafka, evaluate **Strimzi** or an appropriate Helm deployment.
> For PostgreSQL and Redis, use suitable persistence and backup strategies.

#### Testing Rules
- Deployment tests.
- Service connectivity tests.
- Ingress tests.
- Health probe tests.
- Scaling tests.
- Persistence tests.
- Helm installation tests.
- Configuration tests.

#### Learning Requirements
Teach me: Kubernetes architecture · Pods · Deployments · Services · Ingress · ConfigMaps · Secrets · Persistent Volumes · StatefulSets · probes · HPA · namespaces · Helm · resource requests and limits · network policies.

#### Phase Completion
- [ ] `helm install hiresense ./infra/helm` successfully deploys the application.
- [ ] The main workflows work inside Kubernetes.
- [ ] Scaling behavior is verified.

---

### PHASE 11 – CI/CD and Observability

**Estimated time:** 1–2 weeks

#### Objective
Automate builds, tests, deployment and application monitoring.

#### Technologies
GitHub Actions · GitHub Container Registry · Docker · Kubernetes · Helm · ArgoCD (optional) · Micrometer · Prometheus · Grafana · OpenTelemetry · Tempo or Zipkin · Loki

#### CI/CD Development Tasks
1. Configure GitHub Actions.
2. Run backend tests on pull requests.
3. Run frontend tests.
4. Run static analysis.
5. Build application artifacts.
6. Build Docker images.
7. Scan dependencies and images.
8. Push approved images to GHCR.
9. Deploy through a controlled workflow.
10. Track deployment versions.
11. Consider ArgoCD for GitOps.

#### Observability Development Tasks
1. Configure Spring Boot Actuator.
2. Configure Micrometer.
3. Integrate Prometheus.
4. Create Grafana dashboards.
5. Monitor request rate.
6. Monitor error rate.
7. Monitor latency.
8. Monitor Kafka consumer lag.
9. Configure OpenTelemetry.
10. Implement distributed tracing.
11. Configure structured JSON logging.
12. Use correlation IDs.
13. Configure useful alerts.
14. Avoid sensitive information in logs.

#### Testing Rules
- Pipeline tests.
- Build verification.
- Deployment verification.
- Metrics verification.
- Trace verification.
- Alert verification.
- Failure scenario testing.

#### Learning Requirements
Teach me: CI vs CD · GitHub Actions · workflows · jobs and steps · GHCR · GitOps · ArgoCD · Micrometer · Prometheus · Grafana · OpenTelemetry · distributed tracing · structured logging · Loki · alerts and monitoring.

#### Phase Completion
- [ ] A push to the main branch triggers the configured pipeline after required approvals and checks.
- [ ] The application can be monitored.
- [ ] A request can be traced across multiple services.

---

### PHASE 12 – Final Polish and Showcase

**Estimated time:** 1 week

#### Objective
Make HireSense portfolio-ready.

#### Development Tasks
1. Finalize `README.md`.
2. Add architecture diagram.
3. Add technology badges.
4. Add application screenshots.
5. Create a demo GIF or video.
6. Document local setup.
7. Document deployment.
8. Document APIs.
9. Document Kafka event flows.
10. Document AI architecture.
11. Document database design.
12. Document architecture decisions.
13. Add seed data.
14. Prepare demo accounts using synthetic data.
15. Prepare a live demo where practical.
16. Document known limitations.
17. Prepare resume descriptions.
18. Prepare a LinkedIn project post.

#### Portfolio Rules
- Never claim unfinished functionality.
- Never expose real candidate data.
- Never publish credentials.
- Include meaningful Git history.
- Include test results.
- Include deployment information.
- Document technical challenges.
- Explain why technologies were selected.
- Include measurable results only when actually measured.

#### Interview Preparation
Prepare to explain:
- Why the project started as a monolith.
- Why microservices were introduced.
- How API Gateway works.
- How JWT authentication works.
- How Keycloak works.
- How Kafka processes events.
- How the outbox pattern prevents event loss.
- How Redis improves performance.
- How Elasticsearch supports search.
- How embeddings work.
- How pgvector works.
- How RAG works.
- How Docker and Kubernetes work together.
- How CI/CD works.
- How monitoring and tracing work.
- How the application handles service failures.
- How security and candidate privacy are maintained.

#### Phase Completion
- [ ] HireSense is runnable, tested, documented and demonstrable.
- [ ] I can independently explain its major architectural decisions and demonstrate its candidate and recruiter workflows.

---

## 7. Project Directory Structure

Use this structure as the project evolves.

```
hiresense/
│
├── backend/
│   ├── hiresense-monolith/
│   ├── api-gateway/
│   ├── user-service/
│   ├── job-service/
│   ├── application-service/
│   ├── notification-service/
│   └── ai-service/
│
├── frontend/
│   └── hiresense-ui/
│
├── infra/
│   ├── docker/
│   ├── compose/
│   ├── kubernetes/
│   ├── helm/
│   ├── monitoring/
│   └── ci-cd/
│
├── docs/
│   ├── requirements.md
│   ├── architecture.md
│   ├── database/
│   ├── diagrams/
│   ├── adr/
│   ├── api/
│   └── learning-notes/
│
├── .github/
│   └── workflows/
│
├── AI_RULES.md
├── PROJECT_PROGRESS.md
├── LEARNING_NOTES.md
├── docker-compose.yml
├── .gitignore
└── README.md
```

> **Note:** Start with the monolith. Create separate service directories only when Phase 5 begins.

---

## 8. Project Progress Tracking

Maintain `PROJECT_PROGRESS.md`.

**Track:**
- Phase number
- Task number
- Task name
- Status
- Implementation notes
- Tests executed
- Concepts learned
- Known issues
- Pending work

**Statuses:**

| Status | Meaning |
|--------|---------|
| `NOT_STARTED` | Work has not begun |
| `IN_PROGRESS` | Currently being worked on |
| `BLOCKED` | Waiting on something |
| `COMPLETED` | Implemented |
| `VERIFIED` | Implemented, tested and checked in the running app |

**Example entry:**

| Phase | Task | Name | Status | Tests | Notes |
|-------|------|------|--------|-------|-------|
| 1 | 1.1 | Create Job entity and migration | VERIFIED | JobRepositoryTest ✅ | Uses UUID ids |

- Never mark a feature `COMPLETED` without implementation.
- Never mark a phase `VERIFIED` without running its relevant tests and checking that the application works.
- Update this file after completing each meaningful task.

---

## 9. Standard Task Execution Workflow

Follow this workflow for **every** feature.

| Step | Action | Details |
|------|--------|---------|
| 1 | Understand requirements | Ask me questions if requirements are unclear. |
| 2 | Explain concepts | Teach the relevant concepts before coding. |
| 3 | Inspect project | Review existing files and architecture. |
| 4 | Create implementation plan | List files to create or modify. |
| 5 | Implement | Generate small, reviewable code changes. |
| 6 | Explain | Explain the important code and design decisions. |
| 7 | Test | Run relevant tests and fix failures. |
| 8 | Verify | Check the application and existing features. |
| 9 | Document | Update learning notes and project progress. |
| 10 | Review | Show what changed and what remains. |
| 11 | Git approval | Ask me before any Git operation that changes staged content, repository history or remote state. |
| 12 | Continue | Wait for my confirmation before starting the next task. |

---

## 10. Standard Prompt for Starting a New Phase

Copy and use this whenever starting a new phase:

```text
We are starting Phase [PHASE NUMBER] of HireSense.

Read and follow:
- AI_RULES.md
- PROJECT_PROGRESS.md
- Relevant architecture documentation.
- Relevant phase requirements.

Before implementing anything:
1. Inspect the current project.
2. Identify completed functionality.
3. Identify dependencies from previous phases.
4. Explain the technologies required.
5. Explain the concepts I need to learn.
6. Divide the phase into small tasks.
7. Define acceptance criteria.
8. Identify potential risks.
9. Create an implementation plan.

Start with Task 1 only.
Do not generate the entire phase at once.
Do not modify unrelated files.
Do not perform Git operations that change repository state or history
without my explicit approval.
Teach me while implementing.
Wait for my confirmation before moving to the next task.
```

---

## 11. Standard Prompt for Completing a Task

After implementing a feature, provide a report containing:

1. Feature implemented.
2. Files created.
3. Files modified.
4. Important code explanations.
5. Concepts learned.
6. Tests executed.
7. Test results.
8. Manual verification results.
9. Known issues.
10. Remaining work.
11. Suggested next task.

- Clearly mention anything that has not been verified.
- Do not automatically commit changes.

---

## 12. Standard Prompt for Debugging

When I encounter an error:

1. Explain the error in simple language.
2. Identify the likely root cause.
3. Inspect the relevant code and logs.
4. Explain how to investigate it.
5. Suggest the smallest appropriate fix.
6. Explain why the fix works.
7. Check whether other features could be affected.
8. Run relevant tests.
9. Explain how to prevent similar issues.

> Never rewrite the entire project just to fix one error.

---

## 13. Final Development Principles

Always remember:

1. Learning is as important as completing the project.
2. One feature at a time.
3. Understand before implementing.
4. Never introduce unnecessary technologies.
5. Maintain clean architecture.
6. Write tests in every phase.
7. Keep the application runnable.
8. Protect existing code and data.
9. Document important decisions.
10. Review all generated code.
11. Never blindly trust AI-generated code.
12. Never perform unauthorized Git operations.
13. Ask before committing, pushing or tagging.
14. Do not move to the next phase until the current phase is verified.
15. Keep me involved in every important technical decision.

---

## 🎯 Final Objective

Build HireSense as a real-world AI-powered recruitment platform while developing the skills to independently design, implement, test, debug, deploy and explain a modern full-stack distributed application.

**The AI should accelerate my development, but I must remain the engineer who understands and controls the project.**

---

*End of HireSense Development Rulebook.*
