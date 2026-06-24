# ADR-0007: Monorepo with Nx and Maven

## Status

Accepted

## Date

2026-01-20

## Context

The project has 3 Angular portals, 8 shared Nx libraries, 10 Maven platform packages, and 17 bounded contexts. Code sharing (UI components, API contracts, design tokens) is critical to avoid duplication. The tender requires a single source code deliverable. Build orchestration must handle both Java and TypeScript/Node.js ecosystems.

## Decision

Use a single monorepo with Nx for the frontend (Angular portals + shared libraries) and Maven for the backend (Spring Boot modular monolith + platform packages). The repository root contains both `package.json` (Nx workspace) and `pom.xml` (Maven parent). Nx handles Angular builds, testing, and linting. Maven handles Java compilation, testing, and packaging.

## Consequences

### Positive

- Single repository simplifies version control, code review, and release management.
- Nx's affected command ensures only changed projects are rebuilt, reducing CI time.
- Shared UI kit and design system guarantee visual consistency across portals.
- API contract library keeps frontend and backend types in sync.
- Single CI/CD pipeline for the entire project.

### Negative

- Monorepo can become slow as project grows; requires Nx caching and Maven parallel builds.
- Git history can become noisy with commits touching many files.
- Merge conflicts are more likely with many contributors.

### Risks

- Risk: Build times increase as codebase grows. Mitigation: Nx distributed caching with Nx Cloud or self-hosted cache; Maven build parallelism with `-T 4C`.