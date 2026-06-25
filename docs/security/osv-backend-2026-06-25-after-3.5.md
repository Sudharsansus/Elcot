# OSV Scan Evidence — Backend (Maven) — AFTER Spring Boot 3.5.16

**Date:** 2026-06-25 · **Tool:** osv-scanner 2.4.0 + OSV batch API · **Source:** Google OSV

Spring Boot **3.4.13 → 3.5.16** (Phase 8). 3.4.13 was the last 3.4.x release; the
final backend critical (`spring-security-web` CVE-2026-22732) had no 6.4.x fix, so
the minor bump was required. 3.5.16 manages spring-security **6.5.11** (≥ 6.5.9
fix), tomcat 10.1.55, thymeleaf 3.1.5.RELEASE, and newer netty/jackson. **Build +
48 tests pass** on JDK 21 + Maven 3.9.9 (Flyway resolved to 11.7.2 via the BOM —
no breakage).

## Progression (real, all verified by re-scan)

| Stage | Critical | High | Medium | Low | Findings |
|---|---|---|---|---|---|
| Initial (3.4.13) | 10 | 32 | 39 | 9 | 90 |
| After overrides (3.4.13) | 1 | 25 | 31 | 8 | 65 |
| **After 3.5.16** | **0** | **6** | **13** | **0** | **19** |

**Backend critical CVEs: 0.** The last one (CVE-2026-22732) cleared by
spring-security 6.5.11.

## Remaining (high/medium) — follow-ups, no criticals

Counts aggregate all 13 reactor modules, so the same advisory appears at multiple
resolved versions (the deployed `avgcxr-api` carries one each).

| Severity | CVE | Package | Note |
|---|---|---|---|
| HIGH | CVE-2026-54512 | `jackson-databind` (2.12.7.1 / 2.17.2 / 2.19.1) | same CVE ×3 versions |
| HIGH | CVE-2026-54513 | `jackson-databind` (2.12.7.1 / 2.17.2 / 2.19.1) | same CVE ×3 versions |
| MEDIUM | CVE-2026-50193 / 54514 / 54515 | `jackson-databind` | |
| MEDIUM | CVE-2026-45292 | `io.opentelemetry:opentelemetry-api:1.49.0` | |
| MEDIUM | CVE-2024-26308 / 25710 | `org.apache.commons:commons-compress:1.24.0` | |
| MEDIUM | CVE-2025-48924 | `org.apache.commons:commons-lang3:3.17.0` | |
| MEDIUM | CVE-2026-0636 | `org.bouncycastle:bcprov-jdk18on:1.81` | |

> The `jackson-databind:2.12.7.1` entry is an old transitive version pulled by one
> dependency; a targeted override is the likely next step for the 2 highs — but
> these are HIGH, not CRITICAL, and out of scope for the zero-criticals goal.
