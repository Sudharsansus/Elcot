# `apps/public-portal/src/app/shared/motion`

<!-- gen-folder-docs -->
> Folder guide for knowledge transfer — auto-generated from each file's own header documentation.
> Regenerate with `node scripts/gen-folder-docs.mjs`. Do not edit by hand (changes are overwritten); to keep a hand-written note, remove the `<!-- gen-folder-docs -->` marker line.

**Purpose:** Folder holding the files listed below (part of `apps/public-portal/src/app/shared`).

## Files (3)

| File | Type | What it does |
| --- | --- | --- |
| `aurora-hero.component.ts` | TypeScript | AURORA HERO — cinematic particle constellation behind the hero. Pure Canvas2D (no three.js): cursor-reactive depth field with additive glow + constellation links. Browser-only, zoneless RAF, reduced-motion safe. |
| `parallax.directive.ts` | TypeScript | PARALLAX — GSAP ScrollTrigger scrub. Element drifts vertically as it passes through the viewport. Browser-only, reduced-motion safe. Usage: <div [appParallax]="120"> (px of total travel) |
| `typewriter.component.ts` | TypeScript | TYPEWRITER — types a phrase, holds, backspaces, types the next. Cycles bilingual phrases (Tamil first, then English). Browser-only, reduced-motion safe. Caret is crimson so it shows through gradient text. |

---
_Part of the Tamil Nadu AVGC-XR portal. This guide describes files as documented in their source headers; for authoritative behaviour, read the code._
