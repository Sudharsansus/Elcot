# Platform Persistence

Database access infrastructure for AVGC-XR Portal.

## Features

- Spring Data JPA configuration with Hibernate
- PgBouncer-aware connection pooling (prepareThreshold=0)
- Audit columns (createdAt, updatedAt) via Hibernate Envers
- Soft delete support
- Read-only replica routing
- Flyway migration integration

## Configuration

Connections go through PgBouncer for production transaction-level pooling.
Direct PostgreSQL connections used in development.
