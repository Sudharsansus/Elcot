# ADR-0008: RabbitMQ over Kafka

## Status

Accepted

## Date

2026-01-25

## Context

The portal needs asynchronous messaging for notifications (email/SMS), search indexing, and export generation. We need to choose between Apache Kafka and RabbitMQ. Requirements include: dead letter queues, message replay, delayed delivery, and manageable operational complexity for a government IT team.

## Decision

Use RabbitMQ 3 with AMQP protocol. RabbitMQ provides native dead letter exchanges, delayed message exchange plugin, per-message TTL, and a user-friendly management UI. The message volume for this portal (notifications, indexing, exports) is moderate (thousands per day, not millions per second), making RabbitMQ's routing flexibility more valuable than Kafka's high-throughput log-based model.

## Consequences

### Positive

- RabbitMQ's management UI allows government ops teams to monitor queues without CLI tools.
- Native dead letter exchanges handle failed messages without custom error handling.
- Delayed message exchange plugin supports scheduled notifications (e.g., reminder emails).
- AMQP protocol is well-supported by Spring AMQP with minimal configuration.
- Lower operational complexity compared to Kafka (no ZooKeeper, no partition management).

### Negative

- RabbitMQ does not support message replay from arbitrary offsets like Kafka.
- Horizontal scaling requires a cluster with queue mirroring or quorum queues.
- Message ordering guarantees are per-queue, not per-partition like Kafka.

### Risks

- Risk: RabbitMQ memory pressure under sustained high load. Mitigation: Configure vm_memory_high_watermark at 60%, set disk_free_limit, and monitor queue depths.