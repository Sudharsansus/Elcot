# Deploying the AVGC-XR Portal on Render

The blueprint [`render.yaml`](../render.yaml) deploys the **full stack**:

| Group | Services |
|---|---|
| **Datastores** | Postgres ×2 (API + CMS), Redis, RabbitMQ, Elasticsearch, MinIO |
| **Backend** | API (Spring Boot), Worker (background), CMS (Strapi 5) |
| **Frontends** | public / applicant / admin portals (Angular 19 static sites) |

The three portals deploy as **static sites** on Render's CDN — they scale to very
large audiences (the only concurrency ceiling is the API, never the SPA).

**Verification status (honest):** the three portals build clean and are fully
verified. The API / worker / CMS / datastores are wired with the **correct
env-var names taken from the real config** (`apps/api/.../application.yml`,
`apps/cms/config`, the Dockerfiles) — but they cannot be runtime-verified from
the build machine. They need your secrets (auto-generated where possible) and a
first-deploy pass. See **§6 Full-stack notes**.

---

## 1. Prerequisites

- A Render account with this Git repository connected.
- The **backend API** and **Strapi CMS** deployed and reachable over HTTPS
  (they are separate services — see their Dockerfiles under `apps/api` and
  `apps/cms`). The backend is verified separately (tag `backend-zero-criticals`)
  and is **not** part of this blueprint.

## 2. Point the frontends at your API + CMS (one-time)

The production API/CMS URLs are baked in at **build time**. Before the first
deploy, set them in each portal's `src/environments/environment.prod.ts`:

```ts
// apps/public-portal/src/environments/environment.prod.ts
apiUrl:    'https://<your-api-host>/api/v1',
strapiUrl: 'https://<your-cms-host>/api',
```

The repo currently targets the production gov domains
(`cms.avgcxr.tn.gov.in`, and a relative `/api/v1`). If your API is on a
**different origin** than the static site, use an absolute `https://…` URL (a
relative `/api/v1` only works when something proxies that path to the API on the
same origin). Alternatively add a Render rewrite route `/api/*` → your API URL.

## 3. Deploy via Blueprint

1. In Render: **New → Blueprint**, pick this repo. Render reads `render.yaml`
   and creates three static sites:
   `avgcxr-public-portal`, `avgcxr-applicant-portal`, `avgcxr-admin-portal`.
2. Each builds with `pnpm install` + `nx build <portal> --configuration=production`
   and publishes `dist/<portal>/browser`.
3. SPA routing + baseline security headers are configured per site.

Node is pinned to **20.18.0** via `NODE_VERSION`; pnpm comes from the
`packageManager` field via `corepack`.

## 4. The lockfile bootstrap (important — read this)

The committed `pnpm-lock.yaml` was generated on the original dev machine, whose
npm registry was a **mock** that injected version *labels* that do not exist on
real npm (e.g. `lodash@4.18.0`). It therefore **cannot** be used with
`--frozen-lockfile` against real npm. For this reason the blueprint installs with
`--no-frozen-lockfile`, so the **first real-npm build regenerates a correct
lockfile**.

The dependency security pins now live in `pnpm-workspace.yaml` (`overrides:`),
where pnpm 9.15 reads them — as caret ranges anchored at known-secure majors, so
real npm resolves the latest *patched* version in range.

### Harden after the first successful deploy

1. Copy the `pnpm-lock.yaml` produced by Render's build (or run `pnpm install`
   once in any real-npm environment) and **commit it**.
2. Switch the `render.yaml` build commands from `--no-frozen-lockfile` to
   `--frozen-lockfile` for reproducible builds.
3. Run a version-accurate vulnerability scan and reconcile if needed:
   ```bash
   osv-scanner -L pnpm-lock.yaml
   ```
4. The `config-integrity` CI gate compares `pnpm-workspace.yaml` overrides to the
   lockfile overrides — it will pass once the real lockfile is committed.

## 5. Alternative: Docker web services

Each portal also has a Dockerfile (`apps/<portal>/Dockerfile`) that builds and
serves via unprivileged nginx on `:8080`. These currently use
`pnpm install --frozen-lockfile`; they will only work once a real lockfile is
committed (see §4). Static sites (above) are the simplest path for a first
deploy.

---

## 6. Full-stack notes (API / worker / CMS / datastores)

These services are now in the blueprint, wired from the **real config** (the
env-var names are verified against `apps/api/.../application.yml`,
`apps/cms/config/*.js`, and the Dockerfiles). What you must do / confirm:

**Secrets** — auto-handled where possible:
- Postgres / Redis credentials come from the managed resources (`fromDatabase` /
  `fromService`).
- RabbitMQ password and MinIO root user/password are **generated** and shared to
  the API/worker via `fromService` (no literals anywhere).
- `JWT_SECRET` (API) and the Strapi secrets (`APP_KEYS`, `ADMIN_JWT_SECRET`,
  `API_TOKEN_SALT`, `TRANSFER_TOKEN_SALT`, `JWT_SECRET`) are **generated**.

**You must set these by hand (marked `sync: false`):**
- **`MINIO_ENDPOINT`** on the API + worker → the MinIO private service's internal
  URL, e.g. `http://avgcxr-minio:9000` (confirm the internal host/port Render
  assigns).
- **`CORS_ALLOWED_ORIGINS`** on the API → comma-separated deployed portal URLs
  (e.g. `https://avgcxr-public-portal.onrender.com,https://avgcxr-applicant-portal.onrender.com,https://avgcxr-admin-portal.onrender.com`).
- **`EMAIL_HOST` / `EMAIL_USER` / `EMAIL_PASSWORD`** (optional SMTP).
- Each portal's **`apiUrl` / `strapiUrl`** in `environment.prod.ts` → the
  deployed API/CMS URLs, then redeploy that portal (static = baked at build).

**One-time provisioning:**
- **MinIO bucket** `application-documents` — create it once (MinIO console on
  `:9001`, or `mc`). The API/worker upload here.
- **Strapi admin** — open the CMS URL `/admin` and create the first admin user;
  publish content for the public portal's News/Events.
- Flyway runs the API DB migrations automatically on first boot.

**Deliberate Render adaptations (verified, not copied from compose):**
- **pgbouncer dropped** — the API connects straight to managed Postgres; the URL
  template's `${PGBOUNCER_PORT}` is set to the managed Postgres port.
- **nginx dropped** — every Render service has its own HTTPS URL.
- **`RABBITMQ_VHOST=/`** — uses the always-present default vhost, so no
  `/avgcxr` vhost provisioning is needed (Spring AMQP declares its own
  queues/exchanges at runtime).

**Could not verify from the build machine:** the API/worker/CMS Docker builds and
their runtime startup (they depend on every datastore being reachable). Expect
the first deploy to settle as the private services (RabbitMQ/Elasticsearch/MinIO)
come up. **Plan names** (`basic-1gb`, `standard`, …) and Redis auth behaviour may
need adjusting to current Render offerings.

## Still required

- A real `pnpm-lock.yaml` — see §4.
- The manual values + provisioning in §6.
