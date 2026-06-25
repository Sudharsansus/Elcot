# Deploying the AVGC-XR frontends on Render

This deploys the three Angular 19 portals (`public-portal`, `applicant-portal`,
`admin-portal`) as **static sites** on Render's CDN. Static delivery means the
frontend scales to very large audiences — the only concurrency ceiling is the
backend API, never the SPA.

The blueprint is [`render.yaml`](../render.yaml) at the repo root.

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

## What is NOT in this blueprint

- **Backend API** (`apps/api`) and **CMS** (`apps/cms`) — deploy separately
  (Docker web services). The frontends call them at the URLs from step 2.
- A real lockfile — see §4.
