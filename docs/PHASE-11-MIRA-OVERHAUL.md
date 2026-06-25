# Phase 11 — Mira Chatbot Overhaul (accessibility + escalation + context)

**Branch:** `phase-11/mira-accessibility-overhaul`
**Date:** 2026-06-25
**Scope:** frontend only (`apps/public-portal/.../features/chat/chat-widget/*`). **No backend change.**

> ⚠️ **Verification status — honest.** This is **code-complete but UNVERIFIED on this
> machine**: the npm registry here is a mock (advertises non-existent versions such
> as `@angular/core@22.0.2`), so `pnpm install` / `nx build` / axe-core / Lighthouse
> **cannot run**. The claims below are **inspection-level** only (brace balance,
> Angular-19 control-flow, ViewChild/template-ref parity, no `*ngIf`/`*ngFor`). A real
> `nx build` + axe-core run in a real npm environment is required to confirm.

## Why an overhaul, not a rebuild

The widget already existed and was already strong: standalone, signals, `@if`/`@for`
control flow, typing indicator, suggestion chips, language toggle, mobile layout,
`localStorage` session, optimistic send + error handling, and it is already mounted
app-wide (`app.component.html`). It talks to the **real** backend `ChatService` at
`/api/v1/chat/send`. Rebuilding it as six new components would have duplicated working
code. So this change adds only the genuinely-missing capabilities.

## Backend contract (verified, unchanged)

Mira exists in the backend (do not modify):

- `POST /api/v1/chat/send` → `ChatTurnResponse` (what the widget uses today).
- `POST /api/v1/mira/chat?lang=en|ta` body `{ "message": "…" }` → `{ reply, model, matchedRule, lang, sources[] }`.
- `GET /api/v1/mira/suggestions` → `[{ text_en, text_ta }]`.

(The earlier brief assumed `/api/chat/messages` + `/api/chat/suggestions`; those are
**not** the real endpoints. The widget keeps using the working `/api/v1/chat` service.)

## What changed (3 files)

### Accessibility (WCAG 2.1 AA targets)

| Behaviour | How | WCAG |
|---|---|---|
| Focus moves into the panel on open, returns to launcher on close | `effect()` guarded on a real open↔close transition + `setTimeout` focus | 2.4.3 Focus Order |
| **ESC** closes the panel | `@HostListener('document:keydown')` | 2.1.2 No Keyboard Trap |
| **Tab is trapped** inside the open dialog | `trapFocus()` cycles first/last focusable | 2.4.3 |
| New replies / "Mira is typing" / open / close are announced | visually-hidden `aria-live="polite"` region driven by `srStatus` | 4.1.3 Status Messages |
| Dialog semantics | `role="dialog"` + `aria-modal="true"` + `aria-labelledby` | 4.1.2 |
| Decorative emoji hidden from SR; controls have bilingual `aria-label`s; input `autocomplete="off"` | template attrs | 1.1.1 / 4.1.2 |
| Honour user prefs | `prefers-reduced-motion`, `prefers-contrast`/`forced-colors` blocks | 2.3.3 / 1.4.11 |

The earlier-render focus-steal bug (focusing the launcher on initial load) was caught
by inspection and fixed with the `wasOpen` transition guard.

### Escalation to a human

If the user message matches human-request keywords (EN: agent/human/person/
representative/customer care/help desk; TA: மனிதர்/அதிகாரி/பணியாளர்/நபர்/உதவி மையம்/ஆதரவு),
a **"Talk to a person"** card appears with phone, email and working hours (bilingual,
dismissible). The message is still sent so the thread stays coherent.

> The phone number is a **clearly-labelled placeholder** (`1800-XXX-XXXX`,
> `TODO(go-live)`). Email follows the portal domain convention. No fabricated real
> contact data.

### Route-aware context

A `contextHint` computed from `Router.url` offers relevant help in the empty state
(`/schemes`, `/application`, `/contact|/grievance`, `/news|/event`), in the active
language. Additive — never blocks the normal flow.

## What was NOT done (and why)

- The broader "33-page frontend overhaul" + a 6-component `shared/mira-chatbot/`
  rewrite: out of scope here and **not verifiable** on this machine. Generating that
  volume of unbuildable code would be unsafe. This change is a focused, reviewable slice.
- No `axe-core` / Lighthouse numbers are claimed — they require a real build.

## To finish in a real env

`nx build public-portal` → `nx test public-portal` → axe-core on the widget (target 0
violations) → keyboard-only walkthrough (Tab in, ESC out, focus return) → screen-reader
pass (NVDA/VoiceOver) for the `aria-live` announcements.
