\
# Developer Onboarding

Welcome to the AVGC-XR Portal project. This guide will help you set up your development environment and understand the codebase.

## Quick Start

1. Read [Dev Environment Setup](dev-environment.md) to install prerequisites
2. Run `./scripts/bootstrap.sh` to set up the full stack
3. Run `./scripts/doctor.sh` to verify everything works
4. Read [Codebase Tour](codebase-tour.md) to understand the architecture
5. Follow [Adding a Component](adding-a-component.md) or [Adding a Service](adding-a-service.md) for your first contribution

## Architecture Overview

- **Backend**: Java 21 + Spring Boot 3.3 modular monolith with 17 bounded contexts
- **Frontend**: Angular 17 (Nx monorepo) with 3 portals and 8 shared libraries
- **Infrastructure**: PostgreSQL 16, Redis 7, RabbitMQ 3, Elasticsearch 8, MinIO, Strapi CMS

## Key Conventions

- Hexagonal architecture within each bounded context (api -> application -> domain -> infrastructure)
- Bilingual support (English + Tamil) is mandatory for all user-facing content
- All API endpoints versioned under `/api/v1/`
- Flyway for database migrations (never modify existing migrations)
- Angular standalone components with signals (no NgModules)
