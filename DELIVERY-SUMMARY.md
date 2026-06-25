# Delivery Summary — AVGC-XR Portal Hardening (P0–P10)

**Date:** 2026-06-25 · **Integration tag:** `backend-zero-criticals` (commit `a76ca12`)
**Toolchain (real):** OpenJDK 21.0.11 + Maven 3.9.9, Node 25 / pnpm 9.15, osv-scanner 2.4.0.

> Honesty first: the **backend is verified** on a real Maven toolchain. The
> **frontend is code-complete but UNVERIFIED** — this machine's npm registry is a
> mock (advertises non-existent versions like `@angular/core@22.0.2`), so Angular
> 19 / Strapi 5 cannot be built or audited here. Nothing below is marked "done"
> that wasn't actually run.

## Status at a glance

| Area | State | Evidence |
|---|---|---|
| Backend build | ✅ `mvn clean verify` BUILD SUCCESS (Spring Boot 3.5.16 / Java 21, 13 modules) | `docs/PHASE-4-REAL-ENV-VERIFICATION.md` |
| Backend tests | ✅ 48 pass (0 fail, 0 error, 2 skipped) | same |
| Backend coverage | ⚠️ 17.8% line (real; below the 80% gate) | JaCoCo |
| **Backend CVEs (OSV)** | ✅ **0 critical** (was 10), 6 high, 13 medium | `docs/security/osv-backend-2026-06-25-after-3.5.md` |
| API deploy (JDBC) | ✅ root-caused + driver-proven (operator applies Northflank env) | `docs/operations/API-DEPLOY-FIX.md` |
| Frontend (Angular 19 / Strapi 5) | ⏳ code-complete, UNVERIFIED (mock registry) | `docs/PHASE-3-ANGULAR-19-MIGRATION.md`, `docs/PHASE-1-STRAPI-MIGRATION.md` |
| Frontend CVEs (OSV) | ⏳ 4 critical on the **committed Angular-17/Strapi-4 lockfile** (pre-upgrade baseline) | `docs/security/osv-frontend-2026-06-25.md` |
| `config-integrity` gate | 🔴 RED **by design** (pnpm override drift; pnpm 9.15 ignores `package.json` overrides) | `scripts/check-config-integrity.mjs` |

## Backend CVE progression (OSV — real, no NVD key)

| Stage | Critical | High | Medium | Low | Findings |
|---|---|---|---|---|---|
| Initial (Spring Boot 3.4.13) | 10 | 32 | 39 | 9 | 90 |
| After component overrides | 1 | 25 | 31 | 8 | 65 |
| **After Spring Boot 3.5.16** | **0** | **6** | **13** | **0** | **19** |

Source: [OSV](https://osv.dev) (Google) via osv-scanner + batch API. Method:
`docs/SECURITY-SCANNING.md`.

## Phases merged

- **P0/P1** — config-integrity gate, honest CI (no `--no-frozen-lockfile` / `|| true` / `continue-on-error`), ADRs.
- **P1** — Strapi 4 → 5 migration (code-complete).
- **P3 (CMS)** — 5 content-type schemas, `StrapiContentService`, OpenAPI annotations.
- **P3 (Angular)** — Angular 17 → 19 + control-flow (`@if`/`@for`) migration.
- **P4** — real-env backend verification + JDBC deploy fix.
- **OSV** — real CVE scan + `osv-scanner` scripts/workflow.
- **P7** — surgical component overrides (tomcat/thymeleaf/minio/poi): 10 → 1 critical.
- **P8** — Spring Boot 3.5.16: 1 → 0 critical.
- **P10** — quality gates (see below).

## Quality Gates (Phase 10)

All three gaps found by the verification pass are closed and **verified by `mvn clean verify`**:

- **Spotless:** ✅ now ENFORCED in the build (moved from `<pluginManagement>` into the
  active `<build><plugins>`, bound to `validate`). `spotless:apply` reformatted 694
  files; `mvn spotless:check` passes on every module.
- **Checkstyle:** ✅ added (maven-checkstyle-plugin 3.6.0 + Checkstyle 10.20.1 for Java 21),
  config `checkstyle.xml`, enforced at `validate`. **0 violations.** The rule set
  COMPLEMENTS Spotless (UnusedImports, RedundantImport, OneTopLevelClass, EqualsHashCode,
  MissingSwitchDefault, FallThrough, UpperEll, ArrayTypeStyle, ModifierOrder).
  **Honestly deferred** (conflict with the adopted google-java-format style; tracked in
  `checkstyle.xml`): `AvoidStarImport` (126), `NeedBraces` (80), `LineLength` (7 unwrappable
  long string literals). Re-enable incrementally as the code is cleaned.
- **`-DskipTests` in `release.yml`:** ✅ documented — it is the release *packaging* job;
  the authoritative test gate is `ci-backend.yml` (`mvn verify`). Not a hidden skip.

## Deployable?

- **Backend:** Yes — verified, 0 criticals. Apply the JDBC env fix in Northflank (`docs/operations/API-DEPLOY-FIX.md`), then `/actuator/health` should be `UP`.
- **Frontend / Strapi:** Not yet — requires a **real npm environment** to: regenerate `pnpm-lock.yaml` (Angular 19 + Strapi 5), move `pnpm.overrides` → `pnpm-workspace.yaml`, build all 3 portals + CMS, and re-run OSV. Only then merge the frontend to a green `main`.

## Rollback anchor

```
git reset --hard backend-zero-criticals   # return to the verified-backend state
```

New frontend work should branch **from this tag** so the verified backend is preserved.
