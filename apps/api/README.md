# AVGC-XR Portal API Service

Modular monolith REST API built with Java 21 and Spring Boot 3.3.

## Architecture

All 17 bounded contexts are internal packages following **Hexagonal Architecture**:

- **api/** — Inbound adapters (REST controllers, message consumers)
- **application/** — Use cases (ports in/out, services, commands)
- **domain/** — Pure business logic (aggregates, events, exceptions)
- **infrastructure/** — Outbound adapters (persistence, messaging, cache, search)

## Running Locally

```bash
# Ensure infrastructure is running
make docker-up

# Start API with dev profile
make api-run

# Or with debugging
make api-debug
```

## Key Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | /api/auth/v1/register | User registration |
| POST | /api/auth/v1/login | User login (JWT) |
| GET | /api/schemes/v1 | List schemes with filters |
| GET | /api/schemes/v1/{id} | Get scheme details |
| POST | /api/applications/v1 | Create draft application |
| POST | /api/applications/v1/{id}/submit | Submit application |
| GET | /api/workflow/v1/tasks | Get pending tasks |
| GET | /api/analytics/v1/dashboard | Admin dashboard data |

## Profiles

- **dev**: Local development, SQL logging, hot reload
- **staging**: Pre-production with external services
- **prod**: Production with full observability and security
