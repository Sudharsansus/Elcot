# ADR-0005: Flowable BPMN 2.0 for Workflow Engine

## Status

Accepted

## Date

2026-01-20

## Context

The AVGC-XR Portal requires multi-step approval workflows for scheme applications. Different schemes have different approval processes (some need 2 levels, others need 5+). Workflows must support parallel review, rejection loops, re-submission, deadline tracking, and escalation. The workflow engine must be auditable for government compliance.

## Decision

Use Flowable 7.x as the BPMN 2.0 workflow engine embedded within the Spring Boot application. Workflow definitions are stored as BPMN 2.0 XML files in the bounded context's `resources/workflows/` directory. Each scheme type maps to a workflow definition. Flowable's Spring Boot starter provides auto-configuration, and its history tables provide full audit trails.

## Consequences

### Positive

- BPMN 2.0 is an open standard; workflow definitions are portable and readable.
- Flowable's Spring Boot integration requires minimal configuration.
- Visual workflow designer (Flowable Modeler) allows scheme administrators to modify workflows.
- Full audit trail via Flowable history tables satisfies government audit requirements.
- Supports timers, signals, and message events for deadline and notification integration.

### Negative

- Embedded Flowable shares the same JVM and database as the main application.
- Complex workflows with many parallel branches can generate significant database load.
- Workflow definition changes require careful versioning to avoid breaking in-flight processes.

### Risks

- Risk: Flowable table bloat over time. Mitigation: Implement history cleanup job (retain 2 years). Flowable's `historyTimeToLive` process engine configuration handles this automatically.