# `apps/public-portal/src/app`

<!-- gen-folder-docs -->
> Folder guide for knowledge transfer — auto-generated from each file's own header documentation.
> Regenerate with `node scripts/gen-folder-docs.mjs`. Do not edit by hand (changes are overwritten); to keep a hand-written note, remove the `<!-- gen-folder-docs -->` marker line.

**Purpose:** Folder holding the files listed below (part of `apps/public-portal/src`).

## Files (7)

| File | Type | What it does |
| --- | --- | --- |
| `app.component.html` | HTML | Mira: floating AI chat agent (bilingual EN/TA) |
| `app.component.scss` | Sass | Sass stylesheet. |
| `app.component.spec.ts` | TypeScript | Unit test specification for the matching source file. |
| `app.component.ts` | TypeScript | APP ROOT — cinematic shell: glass navbar + content + footer + Mira. Boots the Lenis smooth-scroll engine on the client. Auth routes (/auth/*) render chrome-free: the focused full-screen AuthShell carries its own brand, so the marketing nav/footer/Mira hide. |
| `app.config.server.ts` | TypeScript | TypeScript source. |
| `app.config.ts` | TypeScript | App configuration. |
| `app.routes.ts` | TypeScript | UPDATED: app.routes.ts (add contact + grievance routes) Replace: apps/public-portal/src/app/app.routes.ts |

## Subfolders (3)

- [`core/`](core/README.md) — Core singletons used app-wide: services, guards, interceptors, tokens.
- [`features/`](features/README.md) — Feature modules — each subfolder is a self-contained feature.
- [`shared/`](shared/README.md) — Shared, reusable building blocks (components, pipes, directives).

---
_Part of the Tamil Nadu AVGC-XR portal. This guide describes files as documented in their source headers; for authoritative behaviour, read the code._
