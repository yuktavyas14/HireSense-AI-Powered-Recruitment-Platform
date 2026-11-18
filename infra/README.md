# Infrastructure

Everything needed to run and deploy HireSense outside the application code.
Intentionally empty for now: nothing here is needed until later phases.

| Subfolder (created when needed) | Phase | Contents |
|---|---|---|
| `docker/` | 4 | Dockerfiles and Nginx config |
| `compose/` | 4 | Docker Compose files, `.env.example` |
| `kubernetes/`, `helm/` | 10 | Manifests and Helm charts |
| `monitoring/` | 11 | Prometheus, Grafana, alert config |

Rule: never commit real secrets here; use environment variables and `.env.example`.
