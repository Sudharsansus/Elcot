# ADR-0004: PostgreSQL 16 with Flyway Migrations

## Status

Accepted

## Date

2026-01-15

## Context

The AVGC-XR Portal needs a relational database for transactional data (applications, users, schemes, payments). The Tamil Nadu government mandates data sovereignty, requiring on-premise or India-region hosting. The database must support bilingual text search (English + Tamil), JSONB for flexible schema fields, and row-level security for multi-tenant data isolation.

## Decision

Use PostgreSQL 16 as the primary relational database with Flyway for schema migration management. PostgreSQL 16 provides native full-text search with custom Tamil dictionaries, JSONB with GIN indexing, logical replication for read replicas, and pg_stat_statements for query performance monitoring. Flyway provides versioned, repeatable, and undoable migrations with Spring Boot integration.

## Consequences

### Positive

- PostgreSQL 16 LTS support through 2028 ensures stability.
- Native full-text search eliminates the need for a separate search engine for simple queries.
- JSONB columns allow flexible schema evolution without migrations for non-critical fields.
- Flyway migrations are version-controlled, auditable, and repeatable across environments.
- PgBouncer integration enables connection pooling for high-concurrency government traffic spikes.

### Negative

- PostgreSQL does not scale horizontally for writes; scaling requires application-level sharding.
- Flyway migrations must be carefully ordered to avoid lock contention in production.
- Tamil text search requires custom dictionary configuration and regular maintenance.

### Risks

- Risk: Migration failures in production. Mitigation: All migrations are tested in staging with production-like data volumes before deployment.