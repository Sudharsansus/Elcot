# Phase 3 — Angular 17 → 19 Upgrade (code-complete; UNVERIFIED in sandbox)

**Branch:** `phase-3/angular-19-upgrade`
**Date:** 2026-06-25
**Status:** Version/config bumps + control-flow migration applied and **syntax-verified**.
**Real `pnpm install` / `nx migrate` / `nx build` / `pnpm audit` are NOT possible here**
(npm registry is a simulated mock) — they must run in real CI.

---

## 1. What was changed

### Dependencies — root `package.json` (Nx monorepo: ONE manifest, not per-app)
| Package(s) | 17 → 19 |
|---|---|
| `@angular/*` (core, common, compiler(-cli), forms, router, animations, platform-*, language-service, ssr) | 17.3 → **^19.2** |
| `@angular/cdk`, `@angular/material` | 17.3 → **^19.2** |
| `@angular/cli`, `@angular-devkit/build-angular` | 17.3 → **^19.2** |
| `@nx/*`, `nx` | 17.3 → **^20.3** (Nx 20 supports Angular 19) |
| `typescript` | 5.4 → **~5.6.3** |
| `zone.js` | 0.14 → **~0.15** |
| `@typescript-eslint/*` | 7 → **^8** · `@angular-eslint/eslint-plugin` 17 → **^19** |
| `ng-packagr` | 17.3 → **^19.2** |
| `@ngx-translate/core` + http-loader | 15 → **^16** *(⚠ v16 = new standalone API, see §4)* |
| `ngx-echarts` | 17 → **^19** *(best-effort)* |
| `pnpm.overrides.@angular-devkit/build-angular` | 17.3.12 → **^19.2.0** |

### Control flow → `@if`/`@for` (7 files)
All `*ngIf`/`*ngFor` migrated to built-in control flow:
`apps/{public,admin,applicant}-portal/.../app.component.html`, the public `chat-widget`,
and the 3 `login.component.ts` inline templates. `*ngFor … trackBy` → `@for (…; track $index)`.
(Structural directives still work in v19; this is the recommended modern syntax.)

### NOT changed (already v19-ready)
- **Build executor** is already the modern `@nx/angular:application` (esbuild) — no builder swap.
- `tsconfig.base.json` is already `target`/`module` **ES2022**, `moduleResolution: bundler`.
- `ci-frontend.yml` already uses `--frozen-lockfile` + Node 22 (from the P0/P1 CI-honesty pass).

---

## 2. Verification status (honest)

### ✅ Verified here
- `package.json` valid JSON; pins read back correctly.
- **0** `*ngIf`/`*ngFor` remain; `@if`/`@for` braces balance across all 7 files.
- 3 inline-template login components pass TypeScript transpile.

### ⏳ Requires a real npm environment
- `pnpm install` + **`pnpm-lock.yaml` regeneration** (see §3).
- `nx migrate @angular/core@19` + `nx migrate @nx/angular@20` to run framework + workspace
  migration schematics (the parts a mock registry blocks).
- `nx build` for all 3 portals (the real Angular template + type compile).
- `pnpm audit` to confirm the Angular CVEs are actually cleared.

---

## 3. ⚠️ Known red gate (by design, honest)

The `@angular-devkit/build-angular` override moved to `^19.2.0` in `package.json` while
`pnpm-lock.yaml` still pins `17.3.12`. The **`config-integrity` gate correctly flags this
drift**, and `--frozen-lockfile` installs fail — **until the lockfile is regenerated** in a
real npm env. This is the same situation as the Strapi migration and is expected, not a
regression. (It's a good sign the gate works: it caught the unverified change.)

---

## 4. Cascade follow-ups for real CI (do NOT skip)

These are real-world Angular-19 migration steps that a mock registry cannot perform:

1. **Exact version resolution + lockfile regen** — `^` ranges resolve to the real latest
   compatible versions; commit the new `pnpm-lock.yaml`.
2. **`nx migrate`** — run Angular + Nx migration schematics (e.g. `provideHttpClient` already
   present; check `bootstrapApplication`, control-flow migration schematic for any missed spots).
3. **`@ngx-translate/core` v16** — v16 introduced a **standalone provider API**
   (`provideTranslateService`) replacing `TranslateModule.forRoot`. The app bootstrap config
   may need updating. Verify against the real v16 release notes.
4. **Material 19** — check theming/token API (the apps use a prebuilt theme, low risk).
5. **`nx build` per app** — fix any v19 deprecations/removals the compiler surfaces.

---

## 5. Honest framing

This is **version/config-bumped + control-flow-migrated + best-effort**, NOT a verified
Angular 19 build. The target versions are real-world-correct but **unverified against this
mock registry**, and the ecosystem cascade (§4) will need reconciliation in real CI. The
work is a correct, recoverable starting point — `nx migrate` in a real env will finish it,
and any mistake fails loudly at `nx build`, not silently.
