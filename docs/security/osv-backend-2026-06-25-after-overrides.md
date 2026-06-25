# OSV Scan Evidence — Backend (Maven) — AFTER security overrides

**Date:** 2026-06-25 · **Tool:** osv-scanner 2.4.0 + OSV batch API · **Source:** Google OSV

Re-scan after the Phase-7 surgical version overrides on Spring Boot **3.4.13** (tomcat 10.1.50→10.1.55, thymeleaf 3.1.3→3.1.5.RELEASE, minio 8.5.9→8.6.0, poi 5.3.0→5.4.0). Build + 48 tests still pass.

## Before → After

| Severity | Before | After | Cleared |
|---|---|---|---|
| CRITICAL | 10 | 1 | 9 |
| HIGH | 32 | 25 | 7 |
| MEDIUM | 39 | 31 | 8 |
| LOW | 9 | 8 | 1 |
| **Findings** | **90** | **65** | **25** |

**Critical CVEs cleared (6):** CVE-2026-43515, CVE-2026-43512, CVE-2026-41293 (tomcat 10.1.55); CVE-2026-41901, CVE-2026-40477, CVE-2026-40478 (thymeleaf 3.1.5.RELEASE).

**Critical REMAINING (1):** CVE-2026-22732 — `spring-security-web:6.4.13`. No fix exists on the 6.4.x line (fixed only in 6.5.9 / 7.0.4 ⇒ requires Spring Boot 3.5.x). Decision deferred to user.

## Remaining findings (after)

Scanned 322 deps · 27 affected · 65 findings.

| Severity | Advisory | CVE | Package | Version |
|---|---|---|---|---|
| CRITICAL | [GHSA-mf92-479x-3373](https://osv.dev/GHSA-mf92-479x-3373) | CVE-2026-22732 | org.springframework.security:spring-security-web | 6.4.13 |
| HIGH | [GHSA-j3rv-43j4-c7qm](https://osv.dev/GHSA-j3rv-43j4-c7qm) | CVE-2026-54512 | com.fasterxml.jackson.core:jackson-databind | 2.12.7.1 |
| HIGH | [GHSA-rmj7-2vxq-3g9f](https://osv.dev/GHSA-rmj7-2vxq-3g9f) | CVE-2026-54513 | com.fasterxml.jackson.core:jackson-databind | 2.12.7.1 |
| HIGH | [GHSA-j3rv-43j4-c7qm](https://osv.dev/GHSA-j3rv-43j4-c7qm) | CVE-2026-54512 | com.fasterxml.jackson.core:jackson-databind | 2.17.2 |
| HIGH | [GHSA-rmj7-2vxq-3g9f](https://osv.dev/GHSA-rmj7-2vxq-3g9f) | CVE-2026-54513 | com.fasterxml.jackson.core:jackson-databind | 2.17.2 |
| HIGH | [GHSA-j3rv-43j4-c7qm](https://osv.dev/GHSA-j3rv-43j4-c7qm) | CVE-2026-54512 | com.fasterxml.jackson.core:jackson-databind | 2.18.5 |
| HIGH | [GHSA-rmj7-2vxq-3g9f](https://osv.dev/GHSA-rmj7-2vxq-3g9f) | CVE-2026-54513 | com.fasterxml.jackson.core:jackson-databind | 2.18.5 |
| HIGH | [GHSA-j3rv-43j4-c7qm](https://osv.dev/GHSA-j3rv-43j4-c7qm) | CVE-2026-54512 | com.fasterxml.jackson.core:jackson-databind | 2.19.1 |
| HIGH | [GHSA-rmj7-2vxq-3g9f](https://osv.dev/GHSA-rmj7-2vxq-3g9f) | CVE-2026-54513 | com.fasterxml.jackson.core:jackson-databind | 2.19.1 |
| HIGH | [GHSA-m2cm-222f-qw44](https://osv.dev/GHSA-m2cm-222f-qw44) | CVE-2026-27727 | com.mchange:mchange-commons-java | 0.2.15 |
| HIGH | [GHSA-cm33-6792-r9fm](https://osv.dev/GHSA-cm33-6792-r9fm) | CVE-2026-42579 | io.netty:netty-codec-dns | 4.1.130.Final |
| HIGH | [GHSA-f6hv-jmp6-3vwv](https://osv.dev/GHSA-f6hv-jmp6-3vwv) | CVE-2026-42587 | io.netty:netty-codec-http2 | 4.1.130.Final |
| HIGH | [GHSA-w9fj-cfpg-grvv](https://osv.dev/GHSA-w9fj-cfpg-grvv) | CVE-2026-33871 | io.netty:netty-codec-http2 | 4.1.130.Final |
| HIGH | [GHSA-57rv-r2g8-2cj3](https://osv.dev/GHSA-57rv-r2g8-2cj3) | CVE-2026-42584 | io.netty:netty-codec-http | 4.1.130.Final |
| HIGH | [GHSA-f6hv-jmp6-3vwv](https://osv.dev/GHSA-f6hv-jmp6-3vwv) | CVE-2026-42587 | io.netty:netty-codec-http | 4.1.130.Final |
| HIGH | [GHSA-pwqr-wmgm-9rr8](https://osv.dev/GHSA-pwqr-wmgm-9rr8) | CVE-2026-33870 | io.netty:netty-codec-http | 4.1.130.Final |
| HIGH | [GHSA-mj4r-2hfc-f8p6](https://osv.dev/GHSA-mj4r-2hfc-f8p6) | CVE-2026-42583 | io.netty:netty-codec | 4.1.130.Final |
| HIGH | [GHSA-3qp7-7mw8-wx86](https://osv.dev/GHSA-3qp7-7mw8-wx86) | CVE-2026-44249 | io.netty:netty-handler | 4.1.130.Final |
| HIGH | [GHSA-c653-97m9-rcg9](https://osv.dev/GHSA-c653-97m9-rcg9) | CVE-2026-50010 | io.netty:netty-handler | 4.1.130.Final |
| HIGH | [GHSA-x4gw-5cx5-pgmh](https://osv.dev/GHSA-x4gw-5cx5-pgmh) | CVE-2026-45416 | io.netty:netty-handler | 4.1.130.Final |
| HIGH | [GHSA-5pvg-856g-cp85](https://osv.dev/GHSA-5pvg-856g-cp85) | CVE-2026-47691 | io.netty:netty-resolver-dns | 4.1.130.Final |
| HIGH | [GHSA-676x-f7gg-47vc](https://osv.dev/GHSA-676x-f7gg-47vc) | CVE-2026-45674 | io.netty:netty-resolver-dns | 4.1.130.Final |
| HIGH | [GHSA-98qh-xjc8-98pq](https://osv.dev/GHSA-98qh-xjc8-98pq) | CVE-2026-42198 | org.postgresql:postgresql | 42.7.8 |
| HIGH | [GHSA-8hfc-fq58-r658](https://osv.dev/GHSA-8hfc-fq58-r658) | CVE-2026-22731 | org.springframework.boot:spring-boot-starter-actuator | 3.4.13 |
| HIGH | [GHSA-mgvc-8q2h-5pgc](https://osv.dev/GHSA-mgvc-8q2h-5pgc) | CVE-2026-22733 | org.springframework.boot:spring-boot-starter-actuator | 3.4.13 |
| HIGH | [GHSA-wwpq-f5c3-7hvx](https://osv.dev/GHSA-wwpq-f5c3-7hvx) | CVE-2026-40973 | org.springframework.boot:spring-boot | 3.4.13 |
| MEDIUM | [GHSA-72hv-8253-57qq](https://osv.dev/GHSA-72hv-8253-57qq) | - | com.fasterxml.jackson.core:jackson-core | 2.18.5 |
| MEDIUM | [GHSA-3wrr-7qpf-2prh](https://osv.dev/GHSA-3wrr-7qpf-2prh) | CVE-2026-50193 | com.fasterxml.jackson.core:jackson-databind | 2.12.7.1 |
| MEDIUM | [GHSA-5jmj-h7xm-6q6v](https://osv.dev/GHSA-5jmj-h7xm-6q6v) | CVE-2026-54515 | com.fasterxml.jackson.core:jackson-databind | 2.12.7.1 |
| MEDIUM | [GHSA-hgj6-7826-r7m5](https://osv.dev/GHSA-hgj6-7826-r7m5) | CVE-2026-54514 | com.fasterxml.jackson.core:jackson-databind | 2.12.7.1 |
| MEDIUM | [GHSA-5jmj-h7xm-6q6v](https://osv.dev/GHSA-5jmj-h7xm-6q6v) | CVE-2026-54515 | com.fasterxml.jackson.core:jackson-databind | 2.17.2 |
| MEDIUM | [GHSA-hgj6-7826-r7m5](https://osv.dev/GHSA-hgj6-7826-r7m5) | CVE-2026-54514 | com.fasterxml.jackson.core:jackson-databind | 2.17.2 |
| MEDIUM | [GHSA-5jmj-h7xm-6q6v](https://osv.dev/GHSA-5jmj-h7xm-6q6v) | CVE-2026-54515 | com.fasterxml.jackson.core:jackson-databind | 2.18.5 |
| MEDIUM | [GHSA-hgj6-7826-r7m5](https://osv.dev/GHSA-hgj6-7826-r7m5) | CVE-2026-54514 | com.fasterxml.jackson.core:jackson-databind | 2.18.5 |
| MEDIUM | [GHSA-5jmj-h7xm-6q6v](https://osv.dev/GHSA-5jmj-h7xm-6q6v) | CVE-2026-54515 | com.fasterxml.jackson.core:jackson-databind | 2.19.1 |
| MEDIUM | [GHSA-hgj6-7826-r7m5](https://osv.dev/GHSA-hgj6-7826-r7m5) | CVE-2026-54514 | com.fasterxml.jackson.core:jackson-databind | 2.19.1 |
| MEDIUM | [GHSA-563q-j3cm-6jxm](https://osv.dev/GHSA-563q-j3cm-6jxm) | CVE-2026-50560 | io.netty:netty-codec-http2 | 4.1.130.Final |
| MEDIUM | [GHSA-5x3r-wrvg-rp6q](https://osv.dev/GHSA-5x3r-wrvg-rp6q) | CVE-2026-47244 | io.netty:netty-codec-http2 | 4.1.130.Final |
| MEDIUM | [GHSA-c2gf-v879-257j](https://osv.dev/GHSA-c2gf-v879-257j) | CVE-2026-48043 | io.netty:netty-codec-http2 | 4.1.130.Final |
| MEDIUM | [GHSA-38f8-5428-x5cv](https://osv.dev/GHSA-38f8-5428-x5cv) | CVE-2026-42585 | io.netty:netty-codec-http | 4.1.130.Final |
| MEDIUM | [GHSA-hvcg-qmg6-jm4c](https://osv.dev/GHSA-hvcg-qmg6-jm4c) | CVE-2026-50020 | io.netty:netty-codec-http | 4.1.130.Final |
| MEDIUM | [GHSA-m4cv-j2px-7723](https://osv.dev/GHSA-m4cv-j2px-7723) | CVE-2026-42580 | io.netty:netty-codec-http | 4.1.130.Final |
| MEDIUM | [GHSA-v8h7-rr48-vmmv](https://osv.dev/GHSA-v8h7-rr48-vmmv) | CVE-2026-41417 | io.netty:netty-codec-http | 4.1.130.Final |
| MEDIUM | [GHSA-xxqh-mfjm-7mv9](https://osv.dev/GHSA-xxqh-mfjm-7mv9) | CVE-2026-42581 | io.netty:netty-codec-http | 4.1.130.Final |
| MEDIUM | [GHSA-xmv7-r254-6q78](https://osv.dev/GHSA-xmv7-r254-6q78) | CVE-2026-45673 | io.netty:netty-resolver-dns | 4.1.130.Final |
| MEDIUM | [GHSA-w573-9ffj-6ff9](https://osv.dev/GHSA-w573-9ffj-6ff9) | CVE-2026-45536 | io.netty:netty-transport-native-epoll | 4.1.130.Final |
| MEDIUM | [GHSA-rcgg-9c38-7xpx](https://osv.dev/GHSA-rcgg-9c38-7xpx) | CVE-2026-45292 | io.opentelemetry:opentelemetry-api | 1.43.0 |
| MEDIUM | [GHSA-4265-ccf5-phj5](https://osv.dev/GHSA-4265-ccf5-phj5) | CVE-2024-26308 | org.apache.commons:commons-compress | 1.24.0 |
| MEDIUM | [GHSA-4g9r-vxhx-9pgx](https://osv.dev/GHSA-4g9r-vxhx-9pgx) | CVE-2024-25710 | org.apache.commons:commons-compress | 1.24.0 |
| MEDIUM | [GHSA-j288-q9x7-2f5v](https://osv.dev/GHSA-j288-q9x7-2f5v) | CVE-2025-48924 | org.apache.commons:commons-lang3 | 3.17.0 |
| MEDIUM | [GHSA-c3fc-8qff-9hwx](https://osv.dev/GHSA-c3fc-8qff-9hwx) | CVE-2026-0636 | org.bouncycastle:bcprov-jdk18on | 1.81 |
| MEDIUM | [GHSA-x2wq-9x2f-fhj7](https://osv.dev/GHSA-x2wq-9x2f-fhj7) | CVE-2026-22751 | org.springframework.security:spring-security-core | 6.4.13 |
| MEDIUM | [GHSA-cvc6-q2cp-2xhw](https://osv.dev/GHSA-cvc6-q2cp-2xhw) | CVE-2026-22748 | org.springframework.security:spring-security-oauth2-jose | 6.4.13 |
| MEDIUM | [GHSA-4773-3jfm-qmx3](https://osv.dev/GHSA-4773-3jfm-qmx3) | CVE-2026-22737 | org.springframework:spring-webflux | 6.2.15 |
| MEDIUM | [GHSA-6p4f-wcwh-5vvm](https://osv.dev/GHSA-6p4f-wcwh-5vvm) | CVE-2026-22745 | org.springframework:spring-webflux | 6.2.15 |
| MEDIUM | [GHSA-4773-3jfm-qmx3](https://osv.dev/GHSA-4773-3jfm-qmx3) | CVE-2026-22737 | org.springframework:spring-webmvc | 6.2.15 |
| MEDIUM | [GHSA-6p4f-wcwh-5vvm](https://osv.dev/GHSA-6p4f-wcwh-5vvm) | CVE-2026-22745 | org.springframework:spring-webmvc | 6.2.15 |
| LOW | [GHSA-qqpg-mvqg-649v](https://osv.dev/GHSA-qqpg-mvqg-649v) | CVE-2026-1225 | ch.qos.logback:logback-core | 1.5.22 |
| LOW | [GHSA-45q3-82m4-75jr](https://osv.dev/GHSA-45q3-82m4-75jr) | CVE-2026-42578 | io.netty:netty-handler-proxy | 4.1.130.Final |
| LOW | [GHSA-vxf7-qj7q-83fh](https://osv.dev/GHSA-vxf7-qj7q-83fh) | CVE-2026-22746 | org.springframework.security:spring-security-core | 6.4.13 |
| LOW | [GHSA-5843-p793-ghmm](https://osv.dev/GHSA-5843-p793-ghmm) | CVE-2026-22740 | org.springframework:spring-webflux | 6.2.15 |
| LOW | [GHSA-6hcq-hmm3-jj3c](https://osv.dev/GHSA-6hcq-hmm3-jj3c) | CVE-2026-22735 | org.springframework:spring-webflux | 6.2.15 |
| LOW | [GHSA-wg35-8jpf-2xv3](https://osv.dev/GHSA-wg35-8jpf-2xv3) | CVE-2026-22741 | org.springframework:spring-webflux | 6.2.15 |
| LOW | [GHSA-6hcq-hmm3-jj3c](https://osv.dev/GHSA-6hcq-hmm3-jj3c) | CVE-2026-22735 | org.springframework:spring-webmvc | 6.2.15 |
| LOW | [GHSA-wg35-8jpf-2xv3](https://osv.dev/GHSA-wg35-8jpf-2xv3) | CVE-2026-22741 | org.springframework:spring-webmvc | 6.2.15 |
