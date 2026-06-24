# Deploying the Tamil Nadu AVGC-XR Portal to Northflank

A step-by-step manual guide using the Northflank UI. Provisions **5 managed
addons** + **6 services**, all built from the Dockerfiles in this repo via a
connected GitHub repository.

> Architecture on Northflank
>
> | Component | Type | Image source | Port | Public? |
> |---|---|---|---|---|
> | PostgreSQL 16 | Addon (managed) | Northflank | 5432 | no |
> | Redis | Addon (managed) | Northflank | 6379 | no |
> | RabbitMQ | Addon (managed) | Northflank | 5672 | no |
> | Elasticsearch | Addon (managed) | Northflank | 9200 | no |
> | MinIO | Addon (managed) | Northflank | 9000 | no |
> | **api** | Combined service | `apps/api/Dockerfile` | 8080 | yes |
> | **worker** | Combined service | `apps/worker/Dockerfile` | 8081 (health) | no |
> | **public-portal** | Combined service | `apps/public-portal/Dockerfile` | 8080 | yes |
> | **applicant-portal** | Combined service | `apps/applicant-portal/Dockerfile` | 8080 | yes |
> | **admin-portal** | Combined service | `apps/admin-portal/Dockerfile` | 8080 | yes |
> | **cms** (Strapi) | Combined service | `apps/cms/Dockerfile` | 1337 | yes |

Schema is managed by **Flyway** (migrations `V1`–`V28`) and runs automatically
on `api` startup — there is **no separate migration job**.

---

## STEP 0 — Push the repo to GitHub (one-time)

Northflank builds from a connected Git repo.

```bash
cd C:\avgc-xr-portal
git init
git add -A
git commit -m "Initial commit: AVGC-XR Portal"
git branch -M main
git remote add origin https://github.com/<you>/avgc-xr-portal.git
git push -u origin main
```

> `.gitignore` already excludes `node_modules/`, `dist/`, `target/`, `.env`,
> Terraform state, and the integration bundles. Confirm **no secrets** are
> committed: `git ls-files | grep -iE '\.env$|secret|\.pem|\.key'` should be empty.

---

## STEP 1 — Generate secrets (do this before creating services)

Keep these in a password manager; you'll paste them into a Northflank **Secret Group** in Step 4.

```bash
# API JWT signing key — MUST be valid Base64 that decodes to >= 32 bytes (HS256)
openssl rand -base64 48          # -> JWT_SECRET

# Strapi CMS secrets
openssl rand -base64 16          # -> APP_KEYS  (repeat 4x, comma-join: a,b,c,d)
openssl rand -base64 16          # -> ADMIN_JWT_SECRET
openssl rand -base64 16          # -> API_TOKEN_SALT
openssl rand -base64 16          # -> TRANSFER_TOKEN_SALT
openssl rand -base64 16          # -> CMS JWT_SECRET (separate from the API one)
```

> The API's `JwtTokenProvider` does `Base64.decode(JWT_SECRET)` — a non-Base64
> or short value will throw at startup. `openssl rand -base64 48` is safe.

---

## STEP 2 — Create the project & connect GitHub

1. **Create Project** → name `avgcxr-portal`, pick a region close to users (e.g. an India/Asia region). Choose a small-but-real plan; you can scale later.
2. **Account → Git → Connect GitHub** → install the Northflank GitHub App on the repo from Step 0.

---

## STEP 3 — Add the 5 managed addons

In the project: **Addons → New addon**, for each of the following. Use the
smallest production-grade plan; enable persistent storage where offered.

| Addon | Type to pick | Notes |
|---|---|---|
| PostgreSQL | PostgreSQL 16 | create database `avgcxr_portal`, user `avgcxr_app` |
| Redis | Redis | note whether it requires a password/TLS |
| RabbitMQ | RabbitMQ | note the **vhost** it creates (often `/`) |
| Elasticsearch | Elasticsearch / OpenSearch | single node is fine for start |
| MinIO | MinIO (S3-compatible) | create bucket `application-documents` |

After each addon is **running**, open it and copy its **connection details**
(host, port, username, password, and for MinIO the access/secret keys). Northflank
also exposes these as **linkable env variables** — prefer linking over copy-paste
where the addon supports it.

---

## STEP 4 — Create a Secret Group (shared env)

**Project → Secrets → New secret group** named `avgcxr-shared`. Add the values
below. (Addon hosts/passwords can be **linked** from the addons instead of typed.)

```
# Profile
SPRING_PROFILES_ACTIVE=prod-fixed        # ddl-auto=validate + Flyway enabled

# PostgreSQL (link from the Postgres addon)
DB_HOST=<postgres-internal-host>
DB_PORT=5432
DB_NAME=avgcxr_portal
DB_USER=avgcxr_app
DB_PASSWORD=<postgres-password>

# Redis
REDIS_HOST=<redis-internal-host>
REDIS_PORT=6379
REDIS_PASSWORD=<redis-password-or-blank>

# RabbitMQ
RABBITMQ_HOST=<rabbitmq-internal-host>
RABBITMQ_PORT=5672
RABBITMQ_USER=<rabbitmq-user>
RABBITMQ_PASSWORD=<rabbitmq-password>
RABBITMQ_VHOST=/                          # match the addon's vhost (NOT /avgcxr unless you created it)

# Elasticsearch
ELASTICSEARCH_HOST=<es-internal-host>
ELASTICSEARCH_PORT=9200

# MinIO
MINIO_ENDPOINT=http://<minio-internal-host>:9000
MINIO_ACCESS_KEY=<minio-access-key>
MINIO_SECRET_KEY=<minio-secret-key>
MINIO_BUCKET=application-documents

# JWT (from Step 1)
JWT_SECRET=<base64-48-byte-secret>
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# CORS — the public domains of the 3 portals (fill in after Step 6 assigns domains)
CORS_ALLOWED_ORIGINS=https://<public-portal-domain>,https://<applicant-portal-domain>,https://<admin-portal-domain>

# Chat agent (Mira) — OPTIONAL. Omit for the free rule-based provider.
# avgcxr.llm.provider=anthropic
# avgcxr.llm.anthropic.api-key=sk-ant-...
```

> **`RABBITMQ_VHOST`**: the app defaults to `/avgcxr`. Northflank's managed
> RabbitMQ usually provisions vhost `/`. Set this to whatever the addon shows,
> or create the `/avgcxr` vhost in the RabbitMQ management UI.

---

## STEP 5 — Create the `api` service (do this first)

**Services → New service → Combined service**:

- **Repository:** your GitHub repo, branch `main`. Enable "build on push".
- **Build:** Dockerfile · **Build context = `/`** (repo root) · **Dockerfile path = `apps/api/Dockerfile`**.
- **Runtime → Ports:** add port **8080**, protocol HTTP, **Publicly exposed = yes** (assign a domain — note it, e.g. `api-avgcxr.<region>.code.run`).
- **Runtime → Environment:** link the `avgcxr-shared` secret group.
- **Health check:** HTTP GET `/actuator/health` on port 8080 (start-period ~60s; the JVM + Flyway need a moment).
- **Resources:** ≥ 1 vCPU / 1 GB RAM (Spring Boot + Flyway). Deploy.

Watch the logs: you should see Flyway apply `V1 … V28`, then `Started ApiApplication`.

---

## STEP 6 — Create the 3 portal services

For **each** of `public-portal`, `applicant-portal`, `admin-portal`, create a
**Combined service**:

- **Build:** Dockerfile · context `/` · Dockerfile path `apps/<name>/Dockerfile`.
- **Ports:** **8080**, HTTP, **Publicly exposed = yes** (assign a domain).
- **Environment — one required var:**
  ```
  API_UPSTREAM=<api-internal-address>:8080
  ```
  The portal nginx reverse-proxies `/api/*` to the backend (so the SPA's
  same-origin `/api/v1/...` calls reach `api` with **no CORS**). Use the **api
  service's internal/private address** (Networking tab of the `api` service),
  e.g. `api` or `api.<project>.svc.cluster.local` depending on what Northflank
  shows — append `:8080`.
- **Health check:** HTTP GET `/` on 8080. Deploy.

> The portal Docker build runs `nx build` on **Node 20** with **pnpm 9.14.4**
> (pinned in the Dockerfile because Angular 17 needs Node ≤20 while pnpm 9.15
> needs Node ≥22). No action needed — just don't "upgrade" those pins.

After domains are assigned, go back and fill **`CORS_ALLOWED_ORIGINS`** in the
secret group with the three portal URLs, and redeploy `api`. (CORS is a belt-and-
suspenders safety net; same-origin proxying already avoids it.)

---

## STEP 7 — Create the `worker` service

- **Build:** Dockerfile · context `/` · Dockerfile path `apps/worker/Dockerfile`.
- **Ports:** **8081**, **internal only** (health/metrics; not public).
- **Environment:** link `avgcxr-shared`, **plus**:
  ```
  AVGCXR_API_BASE_URL=http://<api-internal-address>:8080
  ```
  (the worker calls the API internally to dispatch notifications).
- **Health check:** HTTP GET `/actuator/health` on 8081. Deploy.

---

## STEP 8 — Create the `cms` (Strapi) service

- **Build:** Dockerfile · context `/` · Dockerfile path `apps/cms/Dockerfile`.
- **Build/runtime env — REQUIRED to avoid the interactive Strapi prompt:**
  ```
  CI=true
  NODE_ENV=production
  ```
- **Database env** (point Strapi at the same Postgres, or a separate DB):
  ```
  DATABASE_CLIENT=postgres
  DATABASE_HOST=<postgres-internal-host>
  DATABASE_PORT=5432
  DATABASE_NAME=avgcxr_portal
  DATABASE_USERNAME=avgcxr_app
  DATABASE_PASSWORD=<postgres-password>
  DATABASE_SSL=false
  ```
- **Strapi secrets** (from Step 1):
  ```
  APP_KEYS=<k1>,<k2>,<k3>,<k4>
  ADMIN_JWT_SECRET=<...>
  API_TOKEN_SALT=<...>
  TRANSFER_TOKEN_SALT=<...>
  JWT_SECRET=<cms-jwt-secret>
  HOST=0.0.0.0
  PORT=1337
  ```
- **Ports:** **1337**, HTTP, public (assign a domain). **Health:** GET `/_health` on 1337. Deploy.

> If the Strapi build still stops on a dependency prompt, set the build command
> to `npm install --no-audit --no-fund && npm run build` (the bundled
> `npm ci --production --ignore-scripts` skips the admin's peer deps). `CI=true`
> normally suffices.

---

## STEP 9 — Smoke test

```bash
API=https://<api-domain>
curl -fsS  $API/actuator/health                      # {"status":"UP"}
curl -fsS  $API/api/v1/public/stats                   # real DB-backed counts (not zeros)
curl -fsS  $API/api/v1/chat/health                    # {"status":"up", ...}  (Mira)
curl -fsS -X POST $API/api/v1/chat/send \
     -H 'Content-Type: application/json' \
     -d '{"message":"What schemes are available?","language":"en"}'
```

Then open each portal domain in a browser:
- Public portal loads, language toggle works, **Mira chat bubble** appears bottom-right.
- A scheme list/detail page loads (proves `/api` proxy → `api` works).
- Admin/applicant portals load their login.

---

## Operations notes

- **Migrations:** automatic on every `api` deploy (Flyway, idempotent). No manual step.
- **Scaling:** `api` and the portals are stateless — bump replicas freely. `worker` should stay at 1–2 (RabbitMQ consumers; listeners are idempotent).
- **Chat agent LLM:** rule-based by default (free, no key). To enable a real model, set `avgcxr.llm.provider=anthropic|openai|ollama` + the matching API key in the secret group and redeploy `api`. Mira's DPDP purge (90-day session retention) runs daily at 03:00 IST automatically.
- **Backups:** enable automated backups on the Postgres + MinIO addons.
- **TLS/domains:** Northflank issues certificates for assigned domains. For custom domains (`*.avgcxr.tn.gov.in`), add them per service and point DNS as Northflank instructs.

## Known caveats (pre-existing, not Northflank-specific)
- The **Terraform** (`infra/terraform`) is for AWS, **not** used for Northflank — ignore it for this deployment.
- `RABBITMQ_VHOST` and the CORS origins are the two values most likely to need a second pass once addons/domains are assigned.
- The CMS (Strapi) is the least-exercised service; if its admin build is heavy, give it ≥1 GB RAM during build.
