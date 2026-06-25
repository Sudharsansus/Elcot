# Operations — Northflank Deployment

Practical runbook for the services actually deployed on **Northflank**
(`avgcxr-api`, `avgcxr-public-portal`, Postgres/Redis/RabbitMQ/Elasticsearch
addons). For the K8s/blue-green target architecture see
`docs/operations/deployment.md`.

> 🔐 **Never commit secrets.** All values below are Northflank **secret-group
> references** (`${NF_*}`). Real credentials live only in the Northflank secret
> group, not in git.

## 1. Service env-var patterns

### PostgreSQL (the one with a driver-format trap)

Spring Boot's PostgreSQL JDBC driver requires a `jdbc:`-prefixed URL and rejects
Northflank's addon URI (`postgresql://user:pass@host/db`). **Bind individual addon
fields, not the URI:**

```
SPRING_DATASOURCE_URL      = jdbc:postgresql://${NF_AVGCXR_PG_HOST}:${NF_AVGCXR_PG_PORT}/${NF_AVGCXR_PG_DATABASE}?sslmode=require
SPRING_DATASOURCE_USERNAME = ${NF_AVGCXR_PG_USERNAME}
SPRING_DATASOURCE_PASSWORD = ${NF_AVGCXR_PG_PASSWORD}
```

❌ Do **not** bind `SPRING_DATASOURCE_URL` to `NF_AVGCXR_PG_*_URI`. See
`docs/operations/API-DEPLOY-FIX.md` for the full root cause + driver-level proof.

### Redis / RabbitMQ / Elasticsearch (component-based — no jdbc prefix)

```
REDIS_HOST=${NF_AVGCXR_REDIS_HOST}      REDIS_PORT=${NF_AVGCXR_REDIS_PORT}      REDIS_PASSWORD=${NF_AVGCXR_REDIS_PASSWORD}
RABBITMQ_HOST=${NF_AVGCXR_RMQ_HOST}     RABBITMQ_PORT=${NF_AVGCXR_RMQ_PORT}     RABBITMQ_USER=${NF_AVGCXR_RMQ_USERNAME}     RABBITMQ_PASSWORD=${NF_AVGCXR_RMQ_PASSWORD}
ELASTICSEARCH_HOST=${NF_AVGCXR_ES_HOST} ELASTICSEARCH_PORT=${NF_AVGCXR_ES_PORT}
```

If you must use a URI form: Redis accepts `redis://…` via `spring.data.redis.url`;
RabbitMQ accepts `amqp://…` via `spring.rabbitmq.addresses`. Elasticsearch already
expects an `http://` URL. **Only PostgreSQL** needs the `jdbc:` prefix.

### Required non-DB vars

```
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
SPRING_JPA_HIBERNATE_DDL_AUTO=validate   # prod: never 'update'/'create'
CORS_ALLOWED_ORIGINS=https://avgcxr.elcot.tn.gov.in
```

## 2. How to bind a single field from a secret group

In the Northflank service → **Environment** → add a variable and reference the
addon's individual output (e.g. `host`, `port`, `database`, `username`,
`password`) rather than its combined `*_URI`. Construct composite values (like the
JDBC URL) by interpolating those single fields — keeping the password out of the
URL string.

## 3. Deployment troubleshooting checklist

| Symptom | Likely cause | Action |
|---|---|---|
| `Driver … claims to not accept jdbcUrl` | `SPRING_DATASOURCE_URL` bound to the `postgresql://` URI | Rebind to `jdbc:postgresql://…` + separate user/pass (§1) |
| Crash-loop, no DB error | Wrong `sslmode` (addon requires `require`) | Add `?sslmode=require` to the JDBC URL |
| `FlywayValidateException` on boot | `ddl-auto` not `validate`, or migrations out of sync | Set `SPRING_JPA_HIBERNATE_DDL_AUTO=validate`; review `db/migration` |
| 502 from public-portal → api | nginx upstream / port mismatch | api listens on `8080`; confirm nginx `proxy_pass` + Northflank resolver |
| `Unsupported engine` on CMS build | Node version | Pin CMS runner to Node `>=20 <=22` |
| Auth/JWT failures after deploy | `JWT_SECRET` / key env missing | Verify secret-group bindings present on the service |

## 4. Health & smoke checks

```
GET /actuator/health        -> 200 {"status":"UP"}   (api)
GET /actuator/health/db     -> DB component UP
GET /healthz                -> public-portal nginx stub
```

Startup log success marker: `HikariPool-1 - Added connection`. Absence of any
`claims to not accept jdbcUrl` line confirms the §1 fix took effect.
