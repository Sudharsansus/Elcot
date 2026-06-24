# PgBouncer - Connection Pooling for AVGC-XR Portal

## Purpose

PgBouncer provides transaction-level connection pooling between the Spring Boot
application and PostgreSQL 16. This prevents connection exhaustion under high
concurrent loads during scheme application windows.

## Configuration

| Setting | Value | Rationale |
|---------|-------|-----------|
| `pool_mode` | `transaction` | Best for web app workloads |
| `max_client_conn` | 1000 | Supports 1000 concurrent API connections |
| `default_pool_size` | 25 | Limits to 25 actual PG connections |
| `min_pool_size` | 5 | Keeps 5 warm connections ready |
| `reserve_pool_size` | 10 | Burst capacity for traffic spikes |

## Architecture

```
Spring Boot API (1000 max connections)
    ↓
PgBouncer (:6432, transaction pooling)
    ↓ (25 default + 10 reserve = 35 max)
PostgreSQL 16 (:5432)
```

## Monitoring

PgBouncer exposes stats at port 6432:

```sql
SHOW POOLS;
SHOW STATS;
SHOW CLIENTS;
```

## Connection String

Application JDBC URL:
```
jdbc:postgresql://pgbouncer:6432/avgcxr_portal?prepareThreshold=0
```

Note: `prepareThreshold=0` is required for PgBouncer transaction pooling.
