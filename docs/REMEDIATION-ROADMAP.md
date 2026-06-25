# AVGC-XR Portal — Grounded Remediation Roadmap

**Tender:** ELCOT-ST-FPP-2026-0026 (Tamil Nadu AVGC-XR Portal)
**Source of requirements:** `CLAUDE-BACKEND-MASTER-PROMPT.md` (51 acceptance criteria)
**Author:** Engineering (Claude Code session, 2026-06-25)
**Status:** Living document — supersedes the aspirational claims in `SECURITY_KNOWN_ISSUES.md`

---

## 0. Why this document exists

`CLAUDE-BACKEND-MASTER-PROMPT.md` defines a **75-day, 51-criteria** production-hardening
programme. This roadmap maps each of those 51 goals to **what is actually true in this
repository today**, separates *real work* from *aspirational claims*, flags data that
appears **fabricated/unverified**, and sequences the work into reviewable PRs with an
explicit **verification step for each**.

It was written deliberately **without making unverifiable code changes**, because the
environment it was authored in cannot validate them (see §1). Treat every "✅" below as
"verifiable by inspection or already true"; treat "🟡/🔴" as "not yet proven."

---

## 1. Environment reality (read first)

The analysis environment is a **sandbox** that cannot build or verify the system. Anyone
executing this roadmap must do so in a **real CI environment** (JDK 21 + Maven + a real
npm registry + the Docker service stack already defined in `ci-backend.yml`).

| Constraint | Evidence | Consequence |
|---|---|---|
| **No JDK / no Maven / no `mvnw`** | `java`, `mvn` not on PATH; no wrapper in repo | The entire Spring Boot backend — the core subject of the master prompt — **cannot be compiled, tested, coverage-measured, or VAPT-scanned here.** All backend goals are unverified. |
| **Mocked npm registry** | `npm view lodash@4.18.0 version` → `4.18.0`. Lodash has **no** real `4.18.0` (real ceiling `4.17.21`). The registry echoes back any version queried. | `pnpm audit` output, "fixed-in" versions, and CVE existence **cannot be trusted as ground truth here.** Several `.npmrc` pins are versions that do not exist on the real npm. |
| **No running app** | sandbox, no service stack up | Performance, Lighthouse, axe-core / WCAG, and load-test goals are unmeasurable. |

**Implication:** do not let any agent (including me) report these backend/audit goals as
"done" from inside this sandbox. "Done" requires the real toolchain.

---

## 2. Data-integrity warnings

These must be resolved before the security posture can be trusted. They are the highest-
value findings of this analysis.

1. **`SECURITY_KNOWN_ISSUES.md` CVE IDs and "fixed-in" versions are unverified and
   likely partly fabricated.** It lists 16 sequential `CVE-2026-*` Angular IDs plus
   `CVE-2026-1774`, `CVE-2026-27886`, etc. None have been confirmed against NVD /
   GitHub Advisory. The provenance is AI-generated (`docs/AUDIT_REPORT.md` cites a
   "GLM-5 Build Agent" and `/home/z/my-project/...` paths from a different machine).
   → **Action:** re-derive the vulnerability list from a real `pnpm audit` +
   `mvn org.owasp:dependency-check` run, against the real registries. Discard any CVE
   that cannot be confirmed.

2. **`.npmrc` override versions are unverified and some don't exist on real npm.**
   e.g. `lodash=4.18.0`, `serialize-javascript=7.0.3`, `tmp=0.2.7`. On the real
   registry these resolutions will **fail `pnpm install --frozen-lockfile`** or pin to
   a non-existent build. → **Action:** validate every override against real npm; replace
   with the genuine patched version or remove.

3. **Duplicated overrides — RESOLVED (commit `14f99f8`).** The initial reading here was
   backwards. Empirically: pnpm 9.15 **still honours `package.json` `pnpm.overrides`**
   (the deprecation warning is cosmetic; `pnpm install --frozen-lockfile` validated the
   lockfile against it and passed). The **`.npmrc` `override.*` keys were the dead ones** —
   pnpm does not read overrides from `.npmrc` (proof: the lockfile's 13 overrides matched
   `package.json`, not `.npmrc`'s 12, which lacked `@angular-devkit/build-angular`).
   **Action taken:** deleted the never-read `.npmrc` block; kept `package.json` as the
   single source; verified `--frozen-lockfile` stays green; a new `config-integrity` CI
   gate now blocks regression. Forward move to `pnpm-workspace.yaml` deferred to P2 (a real
   `pnpm install` — offline regeneration here dropped override targets absent from the store).

4. **Dangling reference.** `scripts/check-audit.mjs` and `SECURITY_KNOWN_ISSUES.md` both
   cite **`SCALING-ROADMAP.md`, which does not exist** in the repo. → **Action:** create
   it or remove the references.

5. **The "known-issues filter" is the exact pattern the master prompt forbids (Goal #4).**
   `scripts/check-audit.mjs` whitelists `@angular/*`, `@strapi/strapi`, `vite`,
   `launch-editor` and lets the build pass despite critical/high CVEs. This is a
   deliberate, documented decision — see [ADR-0002](adr/0002-dependency-vulnerability-handling.md)
   — but it **must not be described as "0 vulnerabilities."** It is "N vulnerabilities,
   risk-accepted and deferred."

---

## 3. The 51 goals, mapped to reality

**Legend**
`✅ NOW` verifiable here / already true ·
`🟡 CI` real work, but verification needs the real toolchain ·
`🔴 BLOCKED` cannot be done or proven in this sandbox ·
`⚠️ SUSPECT` depends on fabricated/unverified CVE or version data — re-confirm first.

### Security (1–7)
| # | Goal | Status | Note |
|---|---|---|---|
| 1 | `pnpm audit` 0 crit/high | ⚠️🟡 | Currently *masked* by `check-audit.mjs`, not achieved. Real fix = Angular 17→19, Strapi 4→5, vite 5→6. Re-confirm CVE list first. |
| 2 | `mvn dependency-check` 0 CVE | 🔴 | **No OWASP dependency-check plugin configured** in any `pom.xml`. No Maven here. Add plugin, run in real CI. |
| 3 | No `\|\| true` / `continue-on-error` | 🟡 | `--no-frozen-lockfile` in `ci-frontend`, `ci-cms`, `release`; `set +e` in `security-scan`; `\|\| true` in `release.sh`. All inspectable & fixable. |
| 4 | No known-issues filters | 🟡 | `check-audit.mjs` **is** the filter. Decision recorded in ADR-0002; removal is a real step once deps are actually upgraded. |
| 5 | Maven deps at latest | 🔴 | SB 3.3.5→3.4.x, Flyway 10.17→11, etc. Needs `mvn versions:display-dependency-updates`. |
| 6 | pnpm deps at latest | ⚠️🟡 | Angular 17→19, Nx 17→latest. Registry mocked here → can't trust "latest". |
| 7 | OWASP Dependency-Check in CI | 🔴 | Not present. Add to backend pom + CI job. |

### Quality (8–15)
| # | Goal | Status | Note |
|---|---|---|---|
| 8 | Backend coverage ≥ 80% | 🔴 | **10 test files for 694 Java sources.** No JaCoCo configured. Almost certainly far below 80%. Huge real effort. |
| 9 | Frontend coverage ≥ 70% | 🔴 | No coverage tooling wired; can't measure here. |
| 10 | SonarQube clean | 🔴 | Not configured. |
| 11 | Checkstyle/Spotless/ESLint clean | 🟡 | Spotless (Google Java Format) **is** in `pom.xml`; ESLint present. Can't run Java tools here. |
| 12 | No `any` in TS | 🟡 | `tsconfig.base.json` has `strict: true` ✅. Spot-grep for explicit `any` needed. |
| 13 | No `System.out`/`console.log` | ✅🟡 | No `System.out`/`printStackTrace` in `apps/api/src/main` ✅. **3** `console.log` in portals — trivial to remove. |
| 14 | No committed secrets (gitleaks) | 🟡 | CI env vars are test-only placeholders. Run `gitleaks` in real CI to confirm history. |
| 15 | All public APIs have OpenAPI docs | 🔴 | **No SpringDoc/OpenAPI dependency** found in `apps/api/pom.xml`. Not present. |

### Architecture (16–22)
| # | Goal | Status | Note |
|---|---|---|---|
| 16 | Modular monolith, clear boundaries | ✅🟡 | 10 `platform-*` modules + `api`/`worker` exist. `AUDIT_REPORT.md` flags **Spring/JPA imports leaking into domain layer** (hexagonal violation, 17 files) — real cleanup. |
| 17 | Each module: package, tests, README | 🟡 | Modules exist; per-module README/test coverage incomplete. |
| 18 | Event-driven (RabbitMQ) | ✅🟡 | `platform-events` module + RabbitMQ service in CI. Present; behaviour unverified here. |
| 19 | Idempotent operations | 🟡 | Needs code audit (workflow retries, payments). |
| 20 | Immutable audit trail | 🟡 | Claimed in docs; verify against schema (56 migrations). |
| 21 | Flyway migrations versioned/reversible | ✅🟡 | **56 `V*__*.sql`** migrations present ✅. "Reversible/tested" unverified. |
| 22 | Feature flags | 🔴 | No flag framework found; needs adding. |

### Performance (23–28) — **all 🔴 BLOCKED** (no running app/load harness here)
p95<500ms, p99<1s, cold-start<5s, pool<70%, Lighthouse≥90, JS<200KB gz. `scripts/load-test.sh` exists; run in a real perf env.

### Scalability (29–33)
| # | Goal | Status | Note |
|---|---|---|---|
| 29 | Stateless API, 10+ instances, Redis sessions | 🟡 | Redis in stack; statelessness needs code review. |
| 30 | DB read replicas | 🔴 | Infra/config, not in repo scope here. |
| 31 | CDN for static assets | 🟡 | See `infra/`; deployment-time. |
| 32 | K8s autoscaling (min2/max20) | 🟡 | Check `infra/` manifests. |
| 33 | Async decoupled (RabbitMQ) | ✅🟡 | `apps/worker` + `platform-events` present. |

### Documentation (34–38)
| # | Goal | Status | Note |
|---|---|---|---|
| 34 | OpenAPI 3.0 spec complete | 🔴 | Depends on Goal 15 (SpringDoc absent). |
| 35 | ADRs for every major decision | ✅ | **Started this session** — `docs/adr/` seeded (0001, 0002). Backfill remaining decisions. |
| 36 | Operations runbook | ✅ | `docs/operations/` + `docs/runbooks/` already substantial. |
| 37 | Security architecture doc | ✅ | `docs/security/threat-model.md` etc. exist; reconcile with §2 honesty. |
| 38 | Per-module README | 🟡 | Partial. |

### Observability (39–44) — **🟡 CI** (config inspectable, runtime not)
`observability/` dir + `platform-observability` module exist. Prometheus/Grafana/Sentry/
OTel/structured-logging config must be confirmed against a running stack.

### Compliance (45–51)
| # | Goal | Status | Note |
|---|---|---|---|
| 45 | DPDP Act 2023 | 🟡 | `docs/compliance/dpdp-act-2023.md` exists; claims need legal + code audit. |
| 46 | ISO 27001 | 🟡 | `docs/compliance/iso-27001-controls.md` exists. |
| 47 | WCAG 2.1 AA | 🔴 | Needs axe-core on a running app + manual SR testing. `docs/compliance/wcag-aa-evidence.md` exists but unverified. |
| 48 | CERT-In 2022 (180-day logs, 6h reporting) | 🟡 | Policy/infra; confirm log retention config. |
| 49 | MeitY GIGW v3 | 🟡 | `docs/compliance/gigw-checklist.md` exists. |
| 50 | OWASP Top 10 (2021) | 🟡 | Needs the real VAPT in tender's T+70 step. |
| 51 | OWASP API Top 10 (2023) | 🟡 | Same — real VAPT. |

**Tally (sandbox-honest):** ✅/✅🟡 doable-or-true now: ~9 · 🟡 needs real CI: ~22 ·
🔴 blocked here: ~14 · ⚠️ data-suspect: ~6. **Zero** of the backend security/coverage/
perf goals can be *proven* from this environment.

---

## 4. Sequenced PR plan

Each PR is small, reviewable, and has a **verification gate**. Order matters: integrity
and tooling first (so later claims are trustworthy), then the heavy upgrades.

> Tender clock: pre-bid 17.06.2026, submission 30.06.2026, QCBS presentation 01–02.07.2026,
> Go-Live T+75. The QCBS technical score weights "Proposed Solution & Architecture" 40/100,
> so an honest, well-sequenced roadmap is itself bid-relevant evidence.

### P0 — Integrity & honesty — ✅ DONE (2026-06-25, branch `chore/p0-p1-config-integrity`)

- ✅ Overrides single-sourced (commit `14f99f8`): removed the never-read `.npmrc`
  `override.*` block; `package.json` is the working source (see §2.3 — the original
  "package.json is dead" reading was corrected by evidence). `--frozen-lockfile` verified green.
- ✅ Dangling `SCALING-ROADMAP.md` references removed (folded into the audit-gate rewrite, `f49bdc0`).
- ✅ `SECURITY_KNOWN_ISSUES.md` rewritten (`d6dd380`): fabricated `CVE-2026-*` IDs removed;
  banner added; every entry marked PENDING VERIFICATION; links to ADR-0002.
- ✅ `docs/adr/` seeded (0001, 0002).
- **Verified here:** config-integrity gate passes; `pnpm install --frozen-lockfile` green.
  **Still needs real env:** re-validate override target versions against real npm (→ P2).

### P1 — Make CI tell the truth — ✅ DONE (2026-06-25)

- ✅ `--no-frozen-lockfile` → `--frozen-lockfile` in `ci-frontend`, `ci-cms`, `release`
  (`907f954`); `./mvnw`→`mvn` and the `release.sh` `\|\| true` test gate removed (same commit).
- ✅ `check-audit.mjs` rewritten as an honest gate — no whitelist, fails on any
  critical/high (`f49bdc0`); `security-scan.yml` calls it directly, no `set +e` (`df92c56`).
  Fixture-tested: high/critical → exit 1, clean → exit 0.
- ✅ JaCoCo (report active; 80% gate behind `-Pcoverage-gate`) + OWASP dependency-check
  (behind `-Psecurity`) added to `pom.xml` (`6468958`); SpringDoc OpenAPI added (`8c88771`).
- ✅ New `config-integrity` gate prevents drift (`a6aa1d8`); negative-tested.
- **By design:** the honest audit gate makes **Security Scan RED in real CI** until the
  framework CVEs are actually fixed (P4/P5). That red is the truth, not a regression.
- **Still needs real env:** confirm the other workflows are green where they should be;
  publish coverage + dependency-check artifacts.

### P2 — Re-derive the real vulnerability picture
- Run `pnpm audit` (real registry) + `mvn dependency-check` (real Maven). Capture as `docs/BASELINE-AUDIT.md`.
- Validate/replace every `.npmrc` override against real npm.
- **Verify:** baseline numbers are reproducible; overrides resolve under `--frozen-lockfile`.

### P3 — Backend dependency upgrades (needs JDK/Maven)
- SB 3.3.5→3.4.x, Flyway 10→11, Jackson/Hibernate via BOM, SpringDoc added.
- One bump per PR, full `mvn verify` after each.
- **Verify:** `mvn -B verify` green; OpenAPI spec generated (closes Goals 15/34).

### P4 — Frontend major upgrade (needs real registry; ~weeks)
- Angular 17→18→19, Nx aligned, vite 5→6, new control-flow + signals.
- **Verify:** all 3 portals `nx build:production` green; bundle budget; Lighthouse≥90.

### P5 — Strapi 4→5 (needs real registry; ~weeks, breaking)
- Content backup → codemod → fix API-response wrapping in consumers → re-import → UAT.
- **Verify:** CMS-driven public pages render; round-1 VAPT on Strapi 5.

### P6 — Remove the filter, flip the gates
- Once P3–P5 genuinely clear the CVEs: **delete `check-audit.mjs`**, fail CI on real high/critical (Goal #4), turn JaCoCo + dependency-check thresholds **blocking**.
- **Verify:** `pnpm audit`/`dependency-check` green *without* any whitelist.

### P7 — Coverage & quality climb (largest effort)
- Backend 10→ enough tests for ≥80%; frontend ≥70%. Hexagonal-layer cleanup (domain-layer Spring/JPA imports).
- **Verify:** JaCoCo ≥80%, frontend coverage ≥70%, thresholds blocking.

### P8 — Perf, observability, compliance evidence
- Load tests (p95/p99), Lighthouse, axe-core/WCAG, OWASP Top-10 VAPT (tender T+70).
- **Verify:** measured numbers attached; CERT-In-empanelled VAPT certificate.

---

## 5. What I deliberately did NOT do, and why

- **No mass dependency bumps.** Unverifiable here (mocked registry, no Maven); would
  leave the tree in an unprovable state and risk a red build with no way to confirm a fix.
- **No "0 vulnerabilities" claim.** The current green is a *filtered* green; saying
  otherwise would be false.
- **No deletion of `check-audit.mjs` yet.** Removing it before the deps are actually
  upgraded just turns CI red without fixing anything — that's P6, after P3–P5.

This sequencing keeps every later claim honest, which is the whole point.
