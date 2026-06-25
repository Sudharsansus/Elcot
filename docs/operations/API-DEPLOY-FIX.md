# API Service — JDBC URL Startup Fix (Northflank)

**Date:** 2026-06-25 · **Service:** `avgcxr-api` · **Branch:** `phase-4/real-env-verification`

> ⚠️ **No credentials in this document.** All secret values are shown as Northflank
> secret-group **references** (`${NF_AVGCXR_PG_*}`), never literals. Do not paste the
> real username/password/URI into the repo — they live only in the Northflank secret
> group.

## Problem

`avgcxr-api` crash-loops on startup with:

```
Driver org.postgresql.Driver claims to not accept jdbcUrl,
"postgresql://<user>:<pass>@primary.avgcxr-pg--vqhfpm7k7z6j.addon.code.run:5432/<db>?sslmode=require"
```

## Root cause — env override, NOT app code

`apps/api/src/main/resources/application.yml` is **already correct** — it builds a
valid JDBC URL from components and keeps credentials in separate properties:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${PGBOUNCER_PORT:6432}/${DB_NAME:avgcxr_portal}?...
    username: ${DB_USER:avgcxr_user}
    password: ${DB_PASSWORD:avgcxr_user}
    driver-class-name: org.postgresql.Driver
```

The crash comes from the **Northflank service env var** `SPRING_DATASOURCE_URL`
being bound to Northflank's PostgreSQL **addon URI** (`NF_AVGCXR_PG_*_URI`, scheme
`postgresql://user:pass@host:port/db`). Spring Boot relaxed binding maps
`SPRING_DATASOURCE_URL` → `spring.datasource.url`, so that URI **overrides** the
correct YAML value. The PostgreSQL JDBC driver requires the `jdbc:` prefix and
rejects the bare `postgresql://` URI.

### Driver-level proof (offline, real pgjdbc 42.7.8, no DB connection)

`org.postgresql.Driver.acceptsURL(...)` run locally:

```
Northflank URI  ("postgresql://…")        acceptsURL = false   <- the crash
JDBC format     ("jdbc:postgresql://…")   acceptsURL = true    <- the fix
```

This reproduces the exact error and confirms the fix at the driver contract level.

## Fix — set these env vars on the `avgcxr-api` Northflank service

Bind to the **individual addon fields** from the secret group, not the URI:

| Env var | Bind to (secret-group ref) | Notes |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://${NF_AVGCXR_PG_HOST}:${NF_AVGCXR_PG_PORT}/${NF_AVGCXR_PG_DATABASE}?sslmode=require` | **JDBC prefix + `sslmode=require`**; **no** user/pass in the URL |
| `SPRING_DATASOURCE_USERNAME` | `${NF_AVGCXR_PG_USERNAME}` | separate field |
| `SPRING_DATASOURCE_PASSWORD` | `${NF_AVGCXR_PG_PASSWORD}` | separate field |
| `SPRING_DATASOURCE_DRIVER_CLASS_NAME` | `org.postgresql.Driver` | explicit |

**Remove / do NOT bind:** any `SPRING_DATASOURCE_URL` that points at
`NF_AVGCXR_PG_*_URI` (the `postgresql://…` form). That single binding is the bug.

> Northflank addon var names can vary; confirm the exact keys in the secret group
> (`NF_AVGCXR_PG_HOST` / `_PORT` / `_DATABASE` / `_USERNAME` / `_PASSWORD`). The
> addon host here is `primary.avgcxr-pg--vqhfpm7k7z6j.addon.code.run`, port `5432`.

Note: connecting directly to the addon (port 5432), **not** through PgBouncer, so
the JDBC override intentionally drops the PgBouncer-only query params
(`prepareThreshold`, `preparedStatementQueryModes`) from the YAML default.

## Verification

- [x] Driver contract proof: `acceptsURL` false for URI, true for JDBC (above)
- [x] `application.yml` confirmed already JDBC-correct; `application-prod.yml` does
      not override the datasource
- [ ] Northflank redeploy succeeds (operator action — not doable from repo)
- [ ] `GET /actuator/health` returns `200 {"status":"UP"}`
- [ ] Startup log shows `HikariPool-1 - Added connection ...` (no driver error)

## Other services — checked, no JDBC-style trap

Only PostgreSQL needs the `jdbc:` prefix. The rest use component vars or their
native URI scheme, so they are not affected by this class of bug:

| Service | Config in `application.yml` | Status |
|---|---|---|
| Redis | `host`/`port`/`password` (`REDIS_*`) | ✅ component-based; if a `redis://` URI must be used, bind it to `spring.data.redis.url` (Redis accepts that scheme) |
| RabbitMQ | `host`/`port`/`username`/`password`/`virtual-host` (`RABBITMQ_*`) | ✅ component-based; for an `amqp://` URI use `spring.rabbitmq.addresses` |
| Elasticsearch | `uris: http://${ELASTICSEARCH_HOST}:${ELASTICSEARCH_PORT}` | ✅ correct `http://` URL |
| Mail/MinIO | `host`/`port` / `http://` endpoint | ✅ |

## Related

- Northflank service: `avgcxr-api` · addon: `avgcxr-pg`
- `docs/OPERATIONS.md` — Northflank env-var patterns + troubleshooting checklist
- `apps/api/src/main/resources/application.yml` (datasource block)
