# 0001 — Record architecture decisions

**Status:** Accepted
**Date:** 2026-06-25

## Context

This codebase will be operated by ELCOT for the **1-year support period plus 2-year ATS**
(and, realistically, longer), and handed between engineering teams. The master prompt's
guiding principle is that "a fresh graduate in 2035 who has never heard of you should be
able to understand what you built and why." Significant decisions were being made
(framework choices, the security deferral strategy) with no durable record of their
rationale — only AI-generated audit reports of uncertain provenance.

## Decision

We will keep **Architecture Decision Records** in `docs/adr/`, one Markdown file per
significant decision, using the MADR-style template described in `docs/adr/README.md`.
A decision is "significant" if reversing it would be expensive or if a future maintainer
would reasonably ask "why was it done this way?"

## Consequences

- **Positive:** durable rationale; onboarding aid; directly satisfies master-prompt
  Goal #35; supports the QCBS "Solution & Architecture" evaluation criterion.
- **Negative:** small per-decision authoring cost.
- **Follow-up:** backfill the decisions listed in the README backlog.
