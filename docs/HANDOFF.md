# HANDOFF — Build the Full Frontend (real environment)

> **Read this end-to-end, then execute.** You are a Claude Code session running in a
> **real** dev environment (Codespace / tunnel / cloud VM with real internet npm). A
> previous session did all the backend hardening on a **local Windows box whose npm
> registry was a MOCK**, so the frontend could never be built or verified there. Your
> job is to build the frontend **for real** and **verify it**.

---

## STEP 0 — Prove you're in a real environment (do NOT skip)

```bash
uname -s                          # expect: Linux   (NOT MINGW64/Windows)
echo "$CODESPACES"                # expect: true    (if Codespace)
pwd
node --version; pnpm --version
npm view @angular/core version    # expect a REAL 19.x   <-- decisive
```

If `@angular/core` shows **22.x** (or any version > 19), you are still on the mocked
registry — **STOP and fix the environment first.** Everything below assumes real npm.

## STEP 1 — Sync, and know what's OFF-LIMITS

```bash
git fetch origin
git checkout main          # backend is DONE here (tag: backend-zero-criticals)
git checkout -b phase-11/frontend-overhaul
```

**Do NOT touch the backend** — it is verified and on `main`:
- `apps/api/**`, `apps/cms/**` (Strapi), `packages/**`, root `pom.xml`, `.mvn`, CI for backend.
- Backend facts: Spring Boot 3.5.16, **0 critical CVEs** (OSV-verified), 48 tests pass,
  Spotless + Checkstyle enforced. Don't regress it.

Read these first — they are the source of truth for current state and contracts:
`DELIVERY-SUMMARY.md`, `docs/PHASE-4-REAL-ENV-VERIFICATION.md`,
`docs/SECURITY-SCANNING.md`, `docs/PHASE-11-MIRA-OVERHAUL.md`.

## STEP 2 — Unblock the build (this is why it never built before)

1. **Move `pnpm.overrides`** out of `package.json` into **`pnpm-workspace.yaml`** under
   an `overrides:` key. pnpm 9.15 **no longer reads** the `package.json` field, so the
   frozen install currently aborts with `ERR_PNPM_LOCKFILE_CONFIG_MISMATCH`.
2. **Fix fabricated override pins** — several pinned versions only existed on the mock
   registry and **do not exist on real npm**, e.g. `lodash 4.18.0` → real latest
   `4.17.21`. Re-validate every override (`axios`, `tar`, `undici`, `minimatch`,
   `picomatch`, `serialize-javascript`, `js-cookie`, `tmp`, `ws`, `piscina`,
   `@casl/ability`, `@angular-devkit/build-angular`) against real npm; correct any that
   don't resolve.
3. **Regenerate the lockfile**: `pnpm install` (let it resolve Angular 19 + Strapi 5 +
   Nx 20 fresh). Commit the new `pnpm-lock.yaml`.
4. Expect to also reconcile the **Angular 17→19 / Strapi 4→5** ecosystem cascade that
   was code-bumped but never installed: `ngx-translate` v16 standalone API
   (`provideTranslateService`), Material 19 token/theming, `nx migrate` schematics.
   Fix what `nx build` surfaces — don't paper over it.

## STEP 3 — DESIGN DIRECTION (the important part)

**Do NOT ship a generic "AI-polished" website.** Make it innovative, interactive, and
unmistakably a **Tamil Nadu Government AVGC-XR** portal.

### ❌ Anti-patterns to avoid (the homogeneous LLM look)
- Centered hero = big gradient + one headline + a lone "Get Started" button.
- Three identical rounded cards with thin line-icons; pastel SaaS gradients (indigo/violet).
- Inter everywhere, airy empty whitespace with low information density.
- Stock blob illustrations, emoji-as-iconography, a generic "trusted by" logo strip.
- Everything fading-up identically on scroll.

### ✅ What to build instead
This is a portal for **Animation, VFX, Gaming, Comics & Extended Reality** — a creative,
motion-native sector — run by a government. So be **expressive but institutional**:

- **Keep the TN-Government identity** the repo already established (build on it, don't
  flatten it): emblem masthead, "Government of Tamil Nadu / தமிழ்நாடு அரசு", TN red
  `#C8102E` + yellow `#FFD200`, cream surfaces, **Noto Serif** headings + **Noto Sans
  Tamil**. Formal, trustworthy, content-dense — innovation must not fight this.
- **Interactivity with a purpose** (every interactive feature backed by REAL backend data):
  - **Scheme Finder / eligibility wizard** — a few questions → live-filtered matched
    schemes from `/api/schemes`. Useful *and* interactive.
  - **Explorable data viz** with `ngx-echarts` (already a dependency): a Tamil Nadu
    **district map / heatmap** of scheme uptake, sector split (animation vs VFX vs
    gaming vs comics vs XR), disbursement timelines — *explorable*, not static PNGs.
  - **Application-journey timeline** that animates through the real Flowable workflow
    stages (real status data), not a decorative stepper.
  - **Motion with restraint** — intersection/scroll-linked reveals that are *varied and
    intentional*, GPU-cheap, and **fully gated by `prefers-reduced-motion`**.
  - **Optional XR accent**: a lightweight, perf-budgeted Canvas/WebGL motif in the
    masthead (e.g., a subtle particle/line field) — decorative (`aria-hidden`), lazy,
    degrades gracefully, and only if it stays inside the perf budget. License-check any
    3D lib (Three.js = MIT ✓). Do not bloat the bundle or tank Lighthouse.
  - **Micro-interactions that reveal information**, keyboard-operable interactive cards,
    filter chips that animate live counts.
- **Bilingual as a first-class design problem**: both EN and தமிழ் equally polished;
  tuned Tamil typography (size/line-height); the language toggle must re-flow gracefully.
- **Information density done well**: strong type hierarchy, real sortable/filterable
  tables, clear navigation — this is a government service, not a marketing splash.

### Non-negotiable constraints (these keep "innovative" from becoming "gimmicky")
- **WCAG 2.1 AA**: every interaction keyboard-operable, visible focus, correct ARIA,
  reduced-motion honored. axe-core must be **zero violations**.
- **Performance**: Lighthouse **≥ 90** (Perf + A11y + Best-Practices + SEO) on key pages.
  Lazy-load heavy viz; code-split per route. Interactivity must not break the budget.
- **Responsive**: 375 / 768 / 1440.
- **Real data only** — no fake/demo data in production code paths.

## STEP 4 — Align to the REAL backend contracts (verify, don't assume)

Read the live spec + controllers; do not trust guessed URLs:
- OpenAPI: `GET /v3/api-docs` (and `swagger-ui.html`).
- Mira chatbot (already wired in `public-portal`): `POST /api/v1/chat/send`,
  `POST /api/v1/mira/chat?lang=en|ta`, `GET /api/v1/mira/suggestions`.
- Strapi 5 content via the nginx proxy `/api/strapi/*` (news, events, resources, faqs,
  testimonials) — **Strapi 5 flat shape** (`data[].documentId`, no `.attributes`
  wrapper). A `StrapiContentService` already exists — reuse/extend it.
- Auth = JWT `Authorization: Bearer`; uploads = multipart to `/api/documents/...`.

The **Mira chatbot a11y/escalation/context overhaul** is already done on branch
`phase-11/mira-accessibility-overhaul` (commit `628dbfd`) — rebase/merge it in, then
build `public-portal` and run axe-core on the widget. See `docs/PHASE-11-MIRA-OVERHAUL.md`.

## STEP 5 — Verify FOR REAL (you finally can)

```bash
pnpm nx run-many -t build --configuration=production   # all 3 portals + cms
pnpm nx run-many -t test
# axe-core per page (zero violations) + Lighthouse (>=90) + manual mobile + keyboard pass
osv-scanner -L pnpm-lock.yaml                          # confirm Angular/Strapi CVEs cleared
```

Only merge a frontend branch to `main` once **`nx build` is green and axe-core is clean.**

## RULES (carried over — do not relax)
- ❌ NO fabricated data / fake CVE numbers / "demo mode" stubs.
- ❌ NO `|| true`, `continue-on-error`, `--no-frozen-lockfile`, `-DskipTests`, `set +e`
  in CI/scripts (a `config-integrity` gate enforces this).
- ❌ NO new GPL/AGPL dependencies. License-check every new dep; justify bundle cost.
- ✅ Angular 19 + Material 19, standalone, **signals**, OnPush. ngx-translate v16.
- ✅ Atomic commits, one concern each. Honest verification status in every report.
- ✅ Backend stays untouched.

## REPORT (when you've done as much as a session allows)
```
=== FRONTEND OVERHAUL STATUS ===
Env real (uname=Linux, @angular/core=19.x): ✅/❌
Lockfile regenerated + overrides moved to pnpm-workspace.yaml: ✅/❌
Builds (real nx build):  public-portal ✅/❌ | applicant-portal ✅/❌ | admin-portal ✅/❌ | cms ✅/❌
Tests: ✅/❌    axe-core (0 violations): ✅/❌    Lighthouse >=90: <scores>
Mira widget built + axe-clean: ✅/❌
OSV (frontend) criticals: <N>
Design: list the innovative/interactive pieces actually built (not planned)
Branch: phase-11/frontend-overhaul   Commits: <count>
Remaining (honest): <list>
```

GO. Build it for real. Make it interactive and distinctly Tamil-Nadu — not a templated AI page.
