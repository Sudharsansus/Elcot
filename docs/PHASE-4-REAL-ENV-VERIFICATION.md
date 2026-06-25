# Phase 4 — Real-Environment Verification

**Branch:** `phase-4/real-env-verification`
**Date:** 2026-06-25
**Toolchain used:** OpenJDK 21.0.11 (Temurin) + Apache Maven 3.9.9 (portable, `~/dev-tools`),
pnpm 9.15.0, Node 25.9.0, Docker CLI 29.5.2, Git 2.54.

This phase ran the actual commands a "real CI" would run and records the **real
output** — pass or fail. No `|| true`, no `continue-on-error`, no `-DskipTests`,
no fabricated numbers.

---

## 0. Environment truth (STEP 1) — this is NOT a Codespace

The prompt assumed a GitHub Codespace with Node 22 and a real registry. The actual
machine is the **same local Windows 11 box** as every prior phase. Verified:

| Tool | Assumed | Actually present | Note |
|---|---|---|---|
| Environment | Codespace | **local win32**, `c:\avgc-xr-portal` | not a Codespace |
| node | 22 | **25.9.0** | newer than assumed |
| pnpm | 9 | **9.15.0** | warns `pnpm.overrides` no longer read (see §3) |
| JDK 21 | yes | **Temurin 21.0.11** (`dev-tools/jdk-extract`) | not on PATH; wired via `mvnj` |
| Maven 3.9 | yes | **3.9.9** (`dev-tools/apache-maven-3.9.9`) | works |
| Docker | yes | CLI **29.5.2 present, daemon DOWN** | `npipe ... dockerDesktopLinuxEngine` not found |
| **npm registry** | real | **MOCK** | advertises `@angular/core@22.0.2`, `lodash@4.18.1` — versions that DO NOT exist on real npm |

**Consequence:** the backend (Maven Central is real) is **genuinely verifiable**.
The frontend + Strapi CMS (npm registry is a mock) are **not** — see §3.

---

## 1. Backend build — ✅ REAL PASS

```
mvn -B clean compile      → BUILD SUCCESS (all 13 reactor modules)
```

The upgraded stack compiles against real Maven Central:
Spring Boot **3.4.13**, Spring Cloud **2024.0.3**, Flowable **7.2.0**, Java **21**.
`apps/api` compiled 633 source files. Log: `dev-tools/p4-compile.log`.

## 2. Backend tests + coverage — ✅ REAL PASS (coverage honestly low)

```
mvn -B verify             → BUILD SUCCESS
```

**Tests: 48 run, 0 failures, 0 errors, 2 skipped.**

| Module | Tests | Result |
|---|---|---|
| `apps/api` | 43 (2 skipped: `ApiApplicationTests`) | ✅ |
| `packages/platform-security` | 4 (`JwtTokenProviderForgedTest`) | ✅ |
| `apps/worker` | 1 | ✅ |

**JaCoCo coverage — REAL numbers** (only the 3 modules that execute tests emit a report):

| Module | Instructions | Branches | Lines |
|---|---|---|---|
| `apps/api` | 15.1% (3154/20914) | 9.6% (79/823) | 17.6% (681/3868) |
| `apps/worker` | 15.8% (79/500) | 0.0% (0/39) | 27.0% (30/111) |
| `packages/platform-security` | 9.5% (69/728) | 0.0% (0/22) | 15.4% (21/136) |
| **Aggregate (3 modules)** | **14.9% (3302/22142)** | **8.9% (79/884)** | **17.8% (732/4115)** |

> ⚠️ **Honest gap:** the `coverage-gate` profile requires **80% line coverage**
> (`mvn -Pcoverage-gate verify`). At ~18% lines, that gate would **FAIL**. The
> backend builds and its existing tests pass, but it is **far from the 80% target**.
> This is a real, un-papered-over finding: substantial test authoring is still
> required before the coverage gate can be turned on in CI. Reports:
> `apps/{api,worker}/target/site/jacoco/index.html`,
> `packages/platform-security/target/site/jacoco/index.html`.

## 3. Backend CVE scan — OWASP dependency-check

```
mvn -B -Psecurity verify  (dependency-check:check, failBuildOnCVSS=7)
```

Status recorded separately because the **NVD update has no API key on this
machine** and dependency-check itself warns:

```
[WARNING] An NVD API Key was not provided - it is highly recommended to use an
          NVD API key as the update can take a VERY long time without an API Key
```

The plugin downloads the **entire NVD feed** (first run, no local cache) over a
slow link. **Real CVE numbers will be appended here when the scan completes**;
if it cannot complete unauthenticated, the honest record is "scan blocked on NVD
rate-limiting — requires `NVD_API_KEY`," NOT a claim of zero vulnerabilities.
See `<!-- CVE-RESULT -->` placeholder below.

<!-- CVE-RESULT: pending real scan output. Do not fill with anything but actual
     dependency-check-report.json data. -->

---

## 4. Frontend + Strapi CMS — ❌ CANNOT be verified on this machine (proven)

Two independent, **proven** blockers — neither is a refusal, both are reproducible:

### 4a. The npm registry is a mock
```
npm view @angular/core version   → 22.0.2   (real latest is 19.x — 22 does not exist)
npm view lodash version          → 4.18.1   (real latest is 4.17.21 — 4.18.x does not exist)
```
Any `pnpm install` here resolves `^` ranges to **invented versions**, so a
regenerated `pnpm-lock.yaml` would be **garbage** and any "successful" build or
`pnpm audit` would be **meaningless**. STEP 2 of the brief ("regenerate the
lockfile against the real registry and commit it") is therefore **impossible here**
and was not faked.

### 4b. pnpm 9.15 no longer reads `package.json` `pnpm.overrides`
```
pnpm install --frozen-lockfile
  [WARN] The "pnpm" field in package.json is no longer read by pnpm.
         The following keys were ignored: "pnpm.overrides".
  ERR_PNPM_LOCKFILE_CONFIG_MISMATCH: the current "overrides" configuration
         doesn't match the value found in the lockfile
```
This **corrects a previous mistaken comment** in `.npmrc` (now fixed) that claimed
pnpm 9.15 "still honours that field; the warning is cosmetic." It does **not** —
the overrides are genuinely dropped and the frozen install (the mode CI uses)
**aborts**. This compounds the existing Angular-19 override drift
(`@angular-devkit/build-angular`: package.json `^19.2.0` vs lockfile `17.3.12`).

**Required real-CI fix (roadmap P2 — now mandatory):**
1. Move the 13 overrides from `package.json` `pnpm.overrides` → `pnpm-workspace.yaml`
   `overrides:` (pnpm's supported home).
2. Update `scripts/check-config-integrity.mjs` to read overrides from
   `pnpm-workspace.yaml`.
3. `pnpm install --no-frozen-lockfile` **against a real registry** to regenerate
   `pnpm-lock.yaml`, then commit it.
4. Re-validate each override version actually exists on real npm (several pins —
   e.g. `lodash 4.18.0` — are mock-only and must be corrected to real versions
   such as `4.17.21`).
5. `pnpm nx run-many -t build` for the 3 portals + `nx run cms:build`.
6. `pnpm audit --audit-level=high` for the real frontend CVE picture.

Also noted: `apps/cms` declares `engines.node >=20 <=22`; this box runs node
25.9.0 → "Unsupported engine" warning. A CI runner must pin Node 20–22 for CMS.

---

## 5. Net status

| Item | Result |
|---|---|
| Backend compile (Spring Boot 3.4.13/Java 21) | ✅ real BUILD SUCCESS |
| Backend tests | ✅ 48 run, 0 fail, 0 error, 2 skipped |
| Backend line coverage | ⚠️ ~17.8% real — **below 80% gate** (gate would fail) |
| Backend CVE scan | ⏳ running (NVD, no API key) — real numbers TBD; never assume 0 |
| Frontend install/build/audit | ❌ blocked: mock registry + overrides-home change (both proven) |
| Strapi CMS build | ❌ blocked: same mock registry |
| Docker image build | ❌ blocked: daemon not running |

**Bottom line:** the backend upgrade is now **really verified** (builds + tests
green on the upgraded stack). The frontend/CMS remain **honestly unverifiable on
this machine** for two reproducible reasons, with a precise fix list for a real
registry. Nothing here is marked "resolved" that has not actually been run.
