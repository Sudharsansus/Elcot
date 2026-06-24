# ADR-0001: Record Architecture Decisions

## Status

Accepted

## Date

2026-01-15

## Context

The AVGC-XR Portal is a government project with multiple stakeholders (ELCOT, Tamil Nadu IT department, scheme applicants, reviewers). Architecture decisions need to be documented, traceable, and reversible to ensure accountability and knowledge transfer across the project lifecycle.

## Decision

We will use Architecture Decision Records (ADRs) following the Michael Nygard format. Each ADR is a markdown file in `docs/architecture/adr/` numbered sequentially with a four-digit prefix. ADRs capture the context, decision, and consequences of significant architectural choices.

## Consequences

### Positive

- All architectural decisions are documented and traceable for ELCOT audit purposes.
- New team members can understand why decisions were made, not just what was decided.
- Decisions can be revisited and formally superseded when requirements change.

### Negative

- Slight overhead in documenting decisions during rapid development.
- ADRs must be kept up to date when decisions are revised.

### Risks

- Risk: ADRs become stale if not reviewed regularly. Mitigation: Include ADR review in each sprint retrospective.