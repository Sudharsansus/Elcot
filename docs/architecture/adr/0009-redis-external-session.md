# ADR-0009: Redis for External Session Storage

## Status

Accepted

## Date

2026-01-25

## Context

The portal needs session management for authenticated users across three portals. Spring Session must be externalized from the application JVM to support horizontal scaling and survive application restarts. Session data includes user context, language preference, and CSRF tokens.

## Decision

Use Redis 7 as the external session store via Spring Data Redis + Spring Session. Redis provides sub-millisecond session read/write latency, automatic session expiration via TTL, and persistence options for session durability. Sessions are stored as JSON-serialized Spring Session objects with a 30-minute idle timeout.

## Consequences

### Positive

- Sessions survive application restarts and deployments without user logout.
- Horizontal API scaling is possible because any instance can serve any session.
- Redis TTL automatically expires idle sessions, reducing database cleanup load.
- Spring Session Redis integration is battle-tested and requires minimal configuration.

### Negative

- Redis is an additional infrastructure component to deploy and monitor.
- Session serialization/deserialization adds slight latency compared to in-memory sessions.
- Redis memory usage must be monitored and sized appropriately for peak session counts.

### Risks

- Risk: Redis failure causes all users to be logged out. Mitigation: Deploy Redis with AOF persistence and configure Spring Session with session fixation protection.