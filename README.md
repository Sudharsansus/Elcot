# Tamil Nadu AVGC-XR Portal

> Government portal for Animation, VFX, Gaming, Comics & Extended Reality industry development in Tamil Nadu — built for ELCOT.

## Architecture

**Modular Monolith** with **Hexagonal Architecture** (Ports & Adapters). All 17 bounded contexts live inside a single Spring Boot application, organized by domain:

```
apps/api/src/main/java/in/elcot/avgcxr/
├── common/          # Cross-cutting: security, caching, messaging, audit
├── platform/        # Auth, User, Notification, File, Audit, Search
├── policy/          # Scheme, Application, Workflow, Document (CORE)
├── ecosystem/       # Business Connect, Talent Connect, Freelancer Registry
├── analytics/       # Dashboard, Reporting
└── support/         # Helpdesk, Grievance (DPDP Act 2023)
```

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | Angular 17 (Standalone + Signals) |
| Backend | Java 21 + Spring Boot 3.3 (Virtual Threads) |
| CMS | Strapi 4 (Headless, PostgreSQL) |
| Workflow | Flowable BPMN 2.0 |
| Database | PostgreSQL 16 + Flyway |
| Search | Elasticsearch 8 |
| Cache/Session | Redis 7 (external) |
| Queue | RabbitMQ 3.13 |
| Storage | MinIO (S3-compatible) |
| Connection Pool | PgBouncer (transaction-mode) |
| Charts | Apache ECharts |
| Containerization | Docker (K8s-ready) |
| Web Server | Nginx (reverse proxy + cache) |

## Prerequisites

- **Java 21** (Eclipse Temurin recommended)
- **Node.js 20.18+** (LTS)
- **pnpm 9+**
- **Docker & Docker Compose**
- **Git**

## Quick Start

```bash
# Clone the repository
git clone <repository-url> avgc-xr-portal
cd avgc-xr-portal

# Copy environment file
cp .env.example .env
# Edit .env with your local values

# One-command setup (installs deps, starts infra, migrates DB, seeds data)
make setup

# Start development
make dev
```

### Access Points

| Service | URL |
|---------|-----|
| Public Portal | http://localhost:4200 |
| Applicant Portal | http://localhost:4201 |
| Admin Portal | http://localhost:4202 |
| API | http://localhost:8080/api |
| CMS Admin | http://localhost:1337/admin |
| RabbitMQ Management | http://localhost:15672 |
| MinIO Console | http://localhost:9001 |
| Grafana | http://localhost:3000 |

## Project Structure

- `apps/` — Deployable applications (3 Angular portals, 1 API, 1 Worker, 1 CMS)
- `libs/` — Shared Angular libraries (UI Kit, Design System, Auth, i18n, etc.)
- `packages/` — Shared Java libraries (platform-core, platform-security, etc.)
- `infra/` — Infrastructure as Code (Docker, Nginx, PgBouncer, K8s, Terraform)
- `observability/` — Monitoring stack (Prometheus, Grafana, Loki, Tempo)
- `docs/` — Architecture decisions, compliance, runbooks, onboarding
- `scripts/` — Operational scripts (bootstrap, seed, smoke test, etc.)
- `tests/` — Cross-cutting tests (e2e, load, security, accessibility, visual)

## Development

```bash
make help          # Show all available commands
make lint          # Lint Java + TypeScript
make test          # Run all tests
make build         # Build everything
make db-migrate    # Run database migrations
make smoke-test    # Health check all services
make doctor        # Diagnose environment issues
```

## License

Proprietary — All intellectual property rights rest with ELCOT, Government of Tamil Nadu.
