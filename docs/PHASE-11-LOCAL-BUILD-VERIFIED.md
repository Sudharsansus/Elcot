# Phase 11 — Frontend builds locally (verified) + the registry truth

**Branch:** `phase-11/local-frontend-build`
**Date:** 2026-06-26

This corrects a belief that stood for the whole engagement: that the frontend
**could not be built on this machine**. It can. Here is what's actually true and
what was fixed.

## The registry was misunderstood

The local npm registry is **NOT a fake mock** — it is a **real mirror that serves
real package code**, but it **injects synthetic version *labels*** and poisons the
`latest` dist-tag:

- `npm view @angular/core version` → `22.0.3` (a version that doesn't exist upstream).
- BUT `@angular/core@19.2.0` downloads a **real 2.2 MB tarball** (`fesm2022/core.mjs`…).
- Even an injected label like `lodash@4.18.0` downloads **real lodash code** (315 KB).
- `^19.2.0` resolves to a **real `19.2.25`** (the injected `22.x` is out of range).

**Consequence:** `pnpm install` + `nx build` work here with real code. The single
thing that stays unreliable is **vulnerability scanning by version** (OSV trusts the
synthetic labels) — so CVE verification still prefers a clean registry.

## What actually blocked the install: Windows Defender

`pnpm install` failed repeatedly with `ERR_PNPM_ENOENT … <pkg>_tmp_NNNN/node_modules`
on a *different* package each run. Root cause confirmed: `RealTimeProtectionEnabled =
True` — Defender was locking/removing pnpm's temp extraction dirs mid-install. Fix
(user, admin PowerShell):

```powershell
Add-MpPreference -ExclusionPath "C:\avgc-xr-portal"
Add-MpPreference -ExclusionPath "C:\Users\sudha\AppData\Local\pnpm"
Add-MpPreference -ExclusionProcess "node.exe"
```

After that, `pnpm install` completed (~4 min) and stayed stable.

## The real Angular bug (the "old code is Angular 17" issue)

Root `package.json` was bumped to Angular 19, but **every `libs/*` and `apps/*`
`package.json` still declared `@angular/* ^17.3.x`**. pnpm installed a private
`@angular/core@17.3.12` inside each of the 8 libs + 3 apps next to root's `19.2.25`.
The compiler then saw `InputSignal` typed by two different physical Angular copies:

```
NG: Property '__@ɵINPUT_SIGNAL_BRAND_WRITE_TYPE@7503' does not exist on type
    'InputSignal<…>'. Did you mean '…@2505'?
```

**Fix:** bumped all 11 project manifests' `@angular/*` (incl. cdk/material) → `^19.2.0`
(+ `zone.js ~0.15`), reinstalled → single `@angular/core@19.2.25`, cleared `.angular`.

## Verified result (real local `nx build --configuration=production`)

| Project | Build |
|---|---|
| public-portal | ✅ bundle complete |
| applicant-portal | ✅ bundle complete (0 NG8109 after the Button fix) |
| admin-portal | ✅ bundle complete |

So the **Angular 17 → 19 migration is now BUILD-VERIFIED**, not just code-complete.

Additional real fixes (committed): `libs/ui-kit` Button bound the signal itself
(`[class]="classes"`) instead of its value → `[class]="classes()"`; and
`apps/cms` removed `@strapi/plugin-i18n@^5.37.0` (doesn't exist — i18n is core in
Strapi 5; it was aborting the workspace install).

## ⚠️ What is NOT committable from here: the lockfile

The generated `pnpm-lock.yaml` pins **synthetic version numbers** (e.g.
`lodash@4.18.0`) that **do not exist on real npm**, so committing it would break real
CI. Therefore:

- **Building / iterating the frontend locally = unlocked** (real code).
- **The canonical `pnpm-lock.yaml` regen + `pnpm.overrides` → `pnpm-workspace.yaml`
  migration + correcting synthetic pins to real versions = still a real-registry
  (Codespace/CI) task.** See `docs/HANDOFF.md`.

## Net

Local Windows can now **develop and build** all three Angular 19 portals (after the
Defender exclusion). What it still can't do trustworthily: produce the committable
lockfile and run version-accurate CVE scans — those need a clean registry.
