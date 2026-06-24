# ADR-0002: Angular 17 with Standalone Components and Signals

## Status

Accepted

## Date

2026-01-15

## Context

The AVGC-XR Portal requires three separate Angular frontends (public, applicant, admin) with shared components and consistent bilingual UI. We need a modern, performant framework that supports the government's 5-year maintenance horizon. The Nx monorepo tool is mandated for code sharing across portals.

## Decision

Use Angular 17 with standalone components (no NgModules) and Angular Signals for reactive state management. All three portals live in a single Nx monorepo with 8 shared libraries. Components use the new control flow syntax (`@if`, `@for`) and signals API (`signal()`, `computed()`, `effect()`).

## Consequences

### Positive

- Standalone components eliminate NgModule boilerplate and improve tree-shaking.
- Signals provide fine-grained reactivity without RxJS complexity for local state.
- Nx workspace enables shared UI kit, design tokens, and API contracts across portals.
- Angular 17 LTS ensures 5+ years of support matching the government maintenance requirement.
- Functional guards and interceptors simplify route protection.

### Negative

- Team must learn Signals patterns; RxJS still needed for complex async flows (WebSocket, server-sent events).
- Nx adds build configuration complexity.
- Some third-party Angular libraries may not yet support standalone components natively.

### Risks

- Risk: Signals API may have breaking changes in future Angular versions. Mitigation: Pin to Angular 17 LTS and plan incremental upgrades.