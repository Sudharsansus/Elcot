# `scripts`

<!-- gen-folder-docs -->
> Folder guide for knowledge transfer — auto-generated from each file's own header documentation.
> Regenerate with `node scripts/gen-folder-docs.mjs`. Do not edit by hand (changes are overwritten); to keep a hand-written note, remove the `<!-- gen-folder-docs -->` marker line.

**Purpose:** Developer / operational scripts.

## Files (12)

| File | Type | What it does |
| --- | --- | --- |
| `bootstrap.sh` | SH | Shell script. |
| `check-audit.mjs` | JS module | scripts/check-audit.mjs Honest dependency-audit gate for the AVGC-XR Portal. Runs `pnpm audit` and FAILS (exit 1) on ANY critical or high advisory. There is NO whitelist, NO ignore list, and NO deferral filter. … |
| `check-config-integrity.mjs` | JS module | scripts/check-config-integrity.mjs Guards the config-integrity invariants established in the P0 hardening pass (see docs/REMEDIATION-ROADMAP.md) so they cannot silently drift back: 1. .npmrc carries no "override.*" keys (pnpm never reads them; dead config). … |
| `doctor.sh` | SH | Shell script. |
| `gen-folder-docs.mjs` | JS module | FOLDER DOCS GENERATOR — knowledge transfer for the AVGC-XR portal. … |
| `load-test.sh` | SH | Shell script. |
| `migrate.sh` | SH | Shell script. |
| `release.sh` | SH | Shell script. |
| `reset.sh` | SH | Shell script. |
| `security-scan.sh` | SH | Shell script. |
| `seed.sh` | SH | Shell script. |
| `smoke.sh` | SH | Shell script. |

---
_Part of the Tamil Nadu AVGC-XR portal. This guide describes files as documented in their source headers; for authoritative behaviour, read the code._
