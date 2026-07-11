# `apps/public-portal/src/app/features/chat`

<!-- gen-folder-docs -->
> Folder guide for knowledge transfer — auto-generated from each file's own header documentation.
> Regenerate with `node scripts/gen-folder-docs.mjs`. Do not edit by hand (changes are overwritten); to keep a hand-written note, remove the `<!-- gen-folder-docs -->` marker line.

**Purpose:** The Mira / AI Mode assistant feature (public portal).

## Files (5)

| File | Type | What it does |
| --- | --- | --- |
| `ai-audit.service.ts` | TypeScript | AI AUDIT — governance log of every action the AI takes. Layer 9 (governance): a tamper-evident client trail of what the AI did on the user's behalf — tool, arguments, whether the user confirmed it, and when. … |
| `ai-mode.service.spec.ts` | TypeScript | Unit test specification for the matching source file. |
| `ai-mode.service.ts` | TypeScript | AI MODE — the portal's primary, agentic interaction surface. This is the "AI-first" layer: the user states intent (typed or spoken) and the platform ACTS — navigates, finds schemes, checks eligibility, pre-fills forms, tracks status. … |
| `chat.service.ts` | TypeScript | Chat service — injectable providing business logic, state and/or API access. |
| `form-assist.service.ts` | TypeScript | FORM ASSIST — lets a form hand itself to Mira so she can guide the user field-by-field, validate each answer, explain mistakes, and fill the fields live. A form registers a spec on enter and clears it on leave. |

## Subfolders (3)

- [`ai-mode/`](ai-mode/README.md) — AI Mode — the command-palette AI interaction surface.
- [`chat-widget/`](chat-widget/README.md) — Folder holding the files listed below (part of `apps/public-portal/src/app/features/chat`).
- [`mira/`](mira/README.md) — Folder holding the files listed below (part of `apps/public-portal/src/app/features/chat`).

---
_Part of the Tamil Nadu AVGC-XR portal. This guide describes files as documented in their source headers; for authoritative behaviour, read the code._
