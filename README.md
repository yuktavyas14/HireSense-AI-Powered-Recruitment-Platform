# HireSense

**An AI-powered recruitment platform that connects candidates and recruiters.** It parses resumes, matches candidates to jobs, and sends live updates through an event-driven microservices architecture.

> 🚧 **Status: In development, Phase 0 (Planning and Setup).**
> This project is being built in 13 phases. Features below are marked ✅ when done and ⏳ while planned.
> Live progress: [PROJECT_PROGRESS.md](PROJECT_PROGRESS.md)

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-brightgreen)
![Angular](https://img.shields.io/badge/Angular-18-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-black)
![Docker](https://img.shields.io/badge/Docker-blue)
![Kubernetes](https://img.shields.io/badge/Kubernetes-326CE5)

---

## Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Development Roadmap](#development-roadmap)
- [Documentation](#documentation)
- [Screenshots](#screenshots)
- [License](#license)

---

## About the Project

Hiring involves many manual steps: reading every resume, comparing it to the job, and keeping candidates updated. HireSense automates the repetitive parts while **keeping hiring decisions with humans**.

- **Candidates** search jobs, apply, upload resumes, track their applications, and get AI job recommendations.
- **Recruiters** post jobs, review applicants ranked by AI match score with explanations, and manage the hiring pipeline.
- **Admins** manage users, roles, and platform statistics.

The project also serves as a hands-on learning journey through modern full-stack and distributed system development.

---

## Features

### Candidate
- ⏳ Register, log in and manage profile
- ⏳ Search and filter jobs
- ⏳ Apply for jobs and track application status
- ⏳ Upload resumes (PDF)
- ⏳ Live notifications
- ⏳ AI job recommendations
- ⏳ AI recruitment chatbot (RAG)

### Recruiter
- ⏳ Manage company profile
- ⏳ Create, update and delete jobs
- ⏳ View applicants and resumes
- ⏳ AI match scores with explanations
- ⏳ Shortlist, reject and update application status
- ⏳ AI-generated interview questions

### Admin
- ⏳ Manage users, roles and permissions
- ⏳ Platform statistics dashboard

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Frontend | Angular 18, TypeScript, Angular Material, Tailwind CSS, Signals, NgRx |
| Backend | Java 21, Spring Boot, Spring Data JPA, Flyway |
| Security | Spring Security, JWT, Keycloak (OAuth2 / OIDC) |
| Database | PostgreSQL, pgvector |
| Messaging | Apache Kafka |
| AI | Spring AI, Ollama (Llama / Mistral) |
| Caching and Search | Redis, Elasticsearch |
| File Storage | MinIO (S3-compatible) |
| DevOps | Docker, Docker Compose, Kubernetes, Helm, GitHub Actions |
| Monitoring | Prometheus, Grafana, OpenTelemetry |
| Testing | JUnit 5, Mockito, Testcontainers, Playwright |

---

## Architecture

> The full architecture is reached in Phase 7. The project **starts as a modular monolith** and is split into microservices in Phase 5. See [docs/architecture.md](docs/architecture.md).

```
                 Angular Frontend
                        │
                   API Gateway
                        │
 ┌──────────┬───────────┼────────────┬────────────────┐
 User       Job      Application     AI Service    Notification
 Service    Service  Service         (Spring AI)   Service
    │          │         │              │               │
    └──────────┴──── Apache Kafka ──────┴───────────────┘

 PostgreSQL · pgvector · Redis · Elasticsearch · MinIO
```

---

## Project Structure

```
hiresense/
├── backend/        # Spring Boot application (monolith first, services from Phase 5)
├── frontend/       # Angular application
├── infra/          # Docker, Kubernetes, Helm, monitoring
├── docs/           # Requirements, architecture, ADRs, diagrams
├── CLAUDE.md       # AI assistant instructions
├── HIRESENSE.md    # Development rules and full roadmap
├── PROJECT_PROGRESS.md
└── README.md
```

---

## Getting Started

> Setup instructions will be added as each phase is completed.

### Prerequisites

- Java 21
- Maven 3.9+
- Node.js 20+ and Angular CLI
- Docker Desktop
- Git

### Run locally

```bash
git clone https://github.com/<your-username>/hiresense.git
cd hiresense
```

_Backend, frontend and Docker Compose instructions will be added in Phases 1, 3 and 4._

---

## Development Roadmap

| Phase | Topic | Status |
|-------|-------|--------|
| 0 | Planning and Setup | 🔄 In progress |
| 1 | Core Backend (Modular Monolith) | ⏳ |
| 2 | Security (JWT, RBAC) | ⏳ |
| 3 | Angular Frontend | ⏳ |
| 4 | Docker and File Uploads | ⏳ |
| 5 | Microservices and Keycloak | ⏳ |
| 6 | Kafka and Event-Driven Architecture | ⏳ |
| 7 | AI Integration (Spring AI, RAG, pgvector) | ⏳ |
| 8 | Caching and Search (Redis, Elasticsearch) | ⏳ |
| 9 | Testing and Code Quality | ⏳ |
| 10 | Kubernetes and Helm | ⏳ |
| 11 | CI/CD and Observability | ⏳ |
| 12 | Final Polish and Showcase | ⏳ |

Full details: [HIRESENSE.md](HIRESENSE.md)

---

## Documentation

| Document | Purpose |
|----------|---------|
| [HIRESENSE.md](HIRESENSE.md) | Development rules and phase roadmap |
| [PROJECT_PROGRESS.md](PROJECT_PROGRESS.md) | Task and phase progress tracker |
| [docs/requirements.md](docs/requirements.md) | Requirements and user stories |
| [docs/architecture.md](docs/architecture.md) | System architecture |
| [docs/adr/](docs/adr/) | Architecture Decision Records |

---

## Screenshots

_Screenshots and a demo GIF will be added in Phase 12._

---

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
