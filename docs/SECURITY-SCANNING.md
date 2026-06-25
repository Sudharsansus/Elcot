# Security Scanning — OSV

**Vulnerability data source:** [OSV](https://osv.dev/) — Google's free, open-source
vulnerability database. **No API key. No rate limiting.** OSV aggregates GitHub
Security Advisories (GHSA) and NVD CVEs across npm, Maven, PyPI, Go, etc.

This replaces the earlier NVD/OWASP-dependency-check approach, which on a key-less
machine throttled to ~6 requests / 30 s and would have taken 4+ hours.

## Why OSV (and two things we deliberately did NOT do)

OSV is the right tool here, but two suggestions that circulated were **incorrect**
and were intentionally **not** implemented — recording them so they don't get
re-added:

1. ❌ **`<dataSource>OSV</dataSource>` in the OWASP dependency-check `pom.xml`
   plugin is not a real option.** dependency-check is fundamentally NVD-backed
   (plus analyzers like OSS Index / RetireJS); there is no config element that
   swaps its data source to OSV. Adding it would be invalid/ignored config. We use
   the standalone **osv-scanner** instead and left the dependency-check plugin as a
   separate, NVD-keyed path (`mvn -Psecurity verify`, see below).
2. ❌ **`osv-scanner` is not an npm package.** It is a Go binary published as a
   GitHub release. It must **not** be added to `package.json` `devDependencies`
   (on the real registry there is no such package; here the mock would resolve a
   phantom). Install it as a binary (below).

## Install osv-scanner

It is a single static binary — no Go toolchain needed at runtime:

```bash
# Linux/macOS/Windows prebuilt binaries:
#   https://github.com/google/osv-scanner/releases  (used here: v2.4.0)
# Or, if Go is available:
go install github.com/google/osv-scanner/v2/cmd/osv-scanner@latest
```

CI should use the official action: `google/osv-scanner-action` (see
`.github/workflows/osv-scan.yml`).

## Run

```bash
# Frontend lockfile (npm)
osv-scanner --format table -L pnpm-lock.yaml

# Backend — direct deps only via pom.xml (osv-scanner's transitive Maven
# resolution can be rate-limited (HTTP 429) by Maven Central):
osv-scanner --format table -L apps/api/pom.xml
```

### Backend: full transitive tree (the reliable method)

Because osv-scanner's live Maven resolution got HTTP 429 from Maven Central, the
**complete** backend picture was produced by resolving the tree with Maven (offline,
from the warm `.m2` cache — real versions) and querying the OSV **batch API**:

```bash
mvn -o dependency:list -DincludeScope=runtime          # 321 unique 3rd-party coords
# -> POST each {package:{name:"group:artifact",ecosystem:"Maven"},version} to
#    https://api.osv.dev/v1/querybatch
# -> GET https://api.osv.dev/v1/vulns/<id> for severity
```

The helper used for the 2026-06-25 run is reproducible; results are committed as
evidence under `docs/security/`.

## Results — 2026-06-25 (real)

| Stack | Scope | Affected | Findings | Critical | High | Medium | Low |
|---|---|---|---|---|---|---|---|
| Backend (Maven) | full reactor tree, 321 deps | 32 | 90 (70 unique advisories) | 10 | 32 | 39 | 9 |
| Frontend (npm) | committed Angular-17/Strapi-4 lockfile | 37 | 96 | 4 | 22 | 58 | 11 (+1 unknown) |

Detail: `docs/security/osv-backend-2026-06-25.md`,
`docs/security/osv-frontend-2026-06-25.md`. Headline summary and remediation
direction: `SECURITY_KNOWN_ISSUES.md`.

**Caveats (honest):**

- Backend counts aggregate all 13 modules → a few libraries appear at multiple
  versions; the deployed `avgcxr-api` carries one version each.
- Frontend is the **pre-upgrade** (Angular 17 / Strapi 4) lockfile — the Angular-19
  / Strapi-5 lockfile can't be regenerated against this machine's mock npm
  registry. Re-scan after a real `pnpm install` to confirm the upgrades clear the
  `@angular/*` and `@strapi/*` findings. A few mock-pinned overrides
  (`lodash 4.18.0`, …) are absent from OSV and don't appear.

## npm scripts

```bash
pnpm run scan:vulns   # osv-scanner --recursive .   (whole repo)
pnpm run scan:fe      # osv-scanner -L pnpm-lock.yaml
pnpm run scan:java    # osv-scanner -L apps/api/pom.xml
```

These require `osv-scanner` on `PATH` (binary install above) — they are thin
wrappers, **not** an npm dependency.
