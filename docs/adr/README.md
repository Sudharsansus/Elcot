# Architecture Decision Records (ADRs)

This directory records the **significant decisions** behind the AVGC-XR Portal — what we
decided, why, and what we traded away. Master-prompt Goal #35 requires an ADR for every
major decision; this directory was previously missing and is seeded here.

## Format

One file per decision: `NNNN-short-title.md`, using the lightweight
[MADR](https://adr.github.io/madr/)-style template:

- **Status** — Proposed | Accepted | Superseded by ADR-XXXX | Deprecated
- **Context** — the forces at play (technical, tender, compliance)
- **Decision** — what we chose
- **Consequences** — positive, negative, and follow-ups

Keep them short. Supersede rather than rewrite history.

## Index

| ADR | Title | Status |
|---|---|---|
| [0001](0001-record-architecture-decisions.md) | Record architecture decisions | Accepted |
| [0002](0002-dependency-vulnerability-handling.md) | Dependency vulnerability handling & the CI audit filter | Accepted (interim) |

## Backlog (decisions made but not yet written up)

From `CLAUDE-BACKEND-MASTER-PROMPT.md` §4.2 — backfill these:
modular-monolith vs microservices · Spring Boot 3.x · Angular frontend · PostgreSQL ·
Flowable workflow engine · Strapi CMS · RabbitMQ · Redis · MinIO · Elasticsearch ·
JWT auth strategy · Northflank hosting.
