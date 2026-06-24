# ADR-0010: PgBouncer Connection Pooling

## Status

Accepted

## Date

2026-02-01

## Context

PostgreSQL 16 has a default max_connections of 200. Each Spring Boot API instance maintains a HikariCP pool (25 connections). With 3 API replicas, that's 75 connections for the application alone. Adding monitoring, background workers, and Strapi CMS easily exceeds 200 connections. Opening too many PostgreSQL connections degrades performance due to process overhead and memory usage.

## Decision

Deploy PgBouncer as a connection pooler between the application and PostgreSQL. Use transaction-level pooling mode, which means PgBouncer assigns a real PostgreSQL connection only for the duration of each transaction. This allows 1000+ client connections while using only 35 actual PostgreSQL connections. The application connects to PgBouncer on port 6432 instead of directly to PostgreSQL on port 5432.

## Consequences

### Positive

- Supports up to 1000 concurrent API connections with only 35 PostgreSQL connections.
- Reduces PostgreSQL memory usage and CPU context switching.
- Connection spikes during scheme application deadlines are absorbed by PgBouncer.
- Transparent to the application (just change the JDBC URL port).

### Negative

- Server-side prepared statements are not supported in transaction pooling mode.
- `prepareThreshold=0` must be set on the JDBC connection to disable prepared statements.
- PgBouncer adds a small amount of latency (sub-millisecond) per connection.
- An additional service to monitor and configure.

### Risks

- Risk: Prepared statement reliance causes errors. Mitigation: Set `prepareThreshold=0` in PgBouncer-aware DataSource configuration; test all bounded contexts with PgBouncer enabled.