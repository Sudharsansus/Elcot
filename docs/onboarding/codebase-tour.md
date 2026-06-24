\
# Codebase Tour

## Repository Structure

```
avgc-xr-portal/
├── apps/                          # Deployable applications
│   ├── api/                       # Spring Boot API (modular monolith)
│   │   ├── src/main/java/in/elcot/avgcxr/
│   │   │   ├── boundedcontext/    # 17 bounded contexts
│   │   │   │   ├── api/           # REST controllers
│   │   │   │   ├── application/   # Use cases / command handlers
│   │   │   │   ├── domain/        # Entities, value objects, repository interfaces
│   │   │   │   └── infrastructure/# JPA repositories, external service adapters
│   │   │   └── config/            # Cross-cutting configuration
│   │   └── src/main/resources/
│   │       ├── db/migration/      # Flyway migration files
│   │       └── workflows/         # Flowable BPMN definitions
│   └── cms/                       # Strapi CMS (Node.js)
├── libs/                          # Nx shared libraries
│   ├── ui-kit/                    # 13 reusable Angular components
│   ├── design-system/             # Tokens, themes, palettes
│   ├── api-contracts/             # TypeScript interfaces matching API
│   ├── auth/                      # Authentication service, guards, interceptors
│   ├── data-access/               # Generic API client
│   ├── i18n/                      # Translation service and pipe
│   ├── util/                      # Directives and pipes
│   └── testing/                   # Mock services and test helpers
├── packages/                      # Maven platform packages
│   ├── platform-core/             # Shared domain primitives
│   ├── platform-persistence/      # JPA + Flyway configuration
│   ├── platform-security/         # JWT, RBAC
│   ├── platform-events/           # RabbitMQ event publishing
│   ├── platform-observability/    # Structured logging
│   ├── platform-search-client/    # Elasticsearch wrapper
│   ├── platform-notification-client/# Email + SMS services
│   ├── platform-cms-client/       # Strapi API client
│   ├── platform-codegen/          # Code generation utilities
│   └── platform-testing/          # Testcontainers + test base classes
├── infra/                         # Infrastructure configuration
│   ├── docker/                    # Docker Compose + service configs
│   ├── k8s/                       # Kubernetes manifests (base + overlays)
│   ├── terraform/                 # AWS infrastructure (modules + environments)
│   ├── nginx/                     # Reverse proxy configuration
│   ├── pgbouncer/                 # Connection pooler configuration
│   └── strapi/                    # Strapi CMS configuration
├── docs/                          # Project documentation
├── scripts/                       # Build, deploy, and utility scripts
├── tests/                         # Cross-cutting test suites
└── tools/                         # Development tools (plop, codemods)
```

## Key Patterns

### Hexagonal Architecture (per bounded context)

```
api/           → REST controllers (inbound adapter)
application/   → Use case handlers (application service)
domain/        → Entities, value objects, repository ports
infrastructure/→ JPA repositories, external clients (outbound adapters)
```

### Bounded Contexts (17)

| Context | Responsibility |
|---------|---------------|
| scheme-management | CRUD for government schemes |
| application-management | Application lifecycle |
| workflow-engine | Flowable BPMN process management |
| user-management | Registration, profiles, roles |
| authentication | Login, OTP, JWT tokens |
| authorization | RBAC enforcement |
| payment | Payment processing integration |
| notification | Email and SMS delivery |
| reporting | Reports and analytics |
| search | Elasticsearch search |
| document-management | File upload/download |
| compliance | Regulatory compliance checks |
| dashboard | Dashboard aggregation |
| content-bridge | CMS content proxy |
| audit | Audit trail logging |
| communication | Broadcast messages |
| configuration | Application configuration |
