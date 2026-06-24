# ADR-0003: Java 21 and Spring Boot 3.3

## Status

Accepted

## Date

2026-01-15

## Context

The backend must serve three Angular portals, handle government-scale traffic, integrate with Flowable BPMN, and maintain compliance with Indian government security standards. Java version choice affects performance, long-term support, and ecosystem compatibility. Spring Boot version determines available features and library compatibility.

## Decision

Use Java 21 (LTS) with Spring Boot 3.3.x. Java 21 provides virtual threads (Project Loom), record patterns, switch pattern matching, and sealed classes. Spring Boot 3.3 requires Jakarta EE 10+ (javax to jakarta migration), supports GraalVM native images, and includes native observability with Micrometer.

## Consequences

### Positive

- Java 21 LTS is supported until 2031, covering the full project lifecycle.
- Virtual threads improve throughput for I/O-bound operations (database, HTTP calls) without complex async code.
- Record patterns reduce boilerplate in domain model mapping.
- Spring Boot 3.3's native Micrometer integration simplifies metrics export.
- Jakarta EE 10 ensures modern servlet container support.

### Negative

- Requires migration from javax to jakarta namespace for all Java EE dependencies.
- Some legacy libraries may not support Java 21 or Jakarta EE 10.
- Virtual threads require careful testing with JDBC drivers and connection pools.

### Risks

- Risk: JDBC driver compatibility with virtual threads. Mitigation: Test with PostgreSQL JDBC 42.7+ and PgBouncer.