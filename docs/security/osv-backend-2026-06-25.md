# OSV Scan Evidence — Backend (Maven)

**Date:** 2026-06-25 · **Tool:** osv-scanner 2.4.0 + OSV batch API (https://api.osv.dev) · **Source:** Google OSV (no API key)

Method: full resolved dependency tree via `mvn -o dependency:list` (321 unique third-party Maven coordinates across all 13 reactor modules), queried against the OSV batch API. Aggregates all modules, so a few libraries appear at multiple versions; the deployed avgcxr-api carries one version each.

**Totals:** 321 deps scanned · 32 affected · 90 findings (package@version × advisory) · 70 unique advisories.

**Severity (database_specific):** CRITICAL 10 · HIGH 32 · MEDIUM 39 · LOW 9

| Severity | Advisory | CVE | Package | Version | Fix |
|---|---|---|---|---|---|
| CRITICAL | [GHSA-5m62-pw8w-7w9f](https://osv.dev/GHSA-5m62-pw8w-7w9f) | CVE-2026-43515 | org.apache.tomcat.embed:tomcat-embed-core | 10.1.50 |  |
| CRITICAL | [GHSA-h6fc-48rj-7qqh](https://osv.dev/GHSA-h6fc-48rj-7qqh) | CVE-2026-43512 | org.apache.tomcat.embed:tomcat-embed-core | 10.1.50 |  |
| CRITICAL | [GHSA-r29c-68gh-xp6x](https://osv.dev/GHSA-r29c-68gh-xp6x) | CVE-2026-41293 | org.apache.tomcat.embed:tomcat-embed-core | 10.1.50 |  |
| CRITICAL | [GHSA-mf92-479x-3373](https://osv.dev/GHSA-mf92-479x-3373) | CVE-2026-22732 | org.springframework.security:spring-security-web | 6.4.13 |  |
| CRITICAL | [GHSA-c9ph-gxww-7744](https://osv.dev/GHSA-c9ph-gxww-7744) | CVE-2026-41901 | org.thymeleaf:thymeleaf-spring6 | 3.1.3.RELEASE |  |
| CRITICAL | [GHSA-r4v4-5mwr-2fwr](https://osv.dev/GHSA-r4v4-5mwr-2fwr) | CVE-2026-40477 | org.thymeleaf:thymeleaf-spring6 | 3.1.3.RELEASE |  |
| CRITICAL | [GHSA-xjw8-8c5c-9r79](https://osv.dev/GHSA-xjw8-8c5c-9r79) | CVE-2026-40478 | org.thymeleaf:thymeleaf-spring6 | 3.1.3.RELEASE |  |
| CRITICAL | [GHSA-c9ph-gxww-7744](https://osv.dev/GHSA-c9ph-gxww-7744) | CVE-2026-41901 | org.thymeleaf:thymeleaf | 3.1.3.RELEASE |  |
| CRITICAL | [GHSA-r4v4-5mwr-2fwr](https://osv.dev/GHSA-r4v4-5mwr-2fwr) | CVE-2026-40477 | org.thymeleaf:thymeleaf | 3.1.3.RELEASE |  |
| CRITICAL | [GHSA-xjw8-8c5c-9r79](https://osv.dev/GHSA-xjw8-8c5c-9r79) | CVE-2026-40478 | org.thymeleaf:thymeleaf | 3.1.3.RELEASE |  |
| HIGH | [GHSA-j3rv-43j4-c7qm](https://osv.dev/GHSA-j3rv-43j4-c7qm) | CVE-2026-54512 | com.fasterxml.jackson.core:jackson-databind | 2.12.7.1 |  |
| HIGH | [GHSA-rmj7-2vxq-3g9f](https://osv.dev/GHSA-rmj7-2vxq-3g9f) | CVE-2026-54513 | com.fasterxml.jackson.core:jackson-databind | 2.12.7.1 |  |
| HIGH | [GHSA-j3rv-43j4-c7qm](https://osv.dev/GHSA-j3rv-43j4-c7qm) | CVE-2026-54512 | com.fasterxml.jackson.core:jackson-databind | 2.16.1 |  |
| HIGH | [GHSA-rmj7-2vxq-3g9f](https://osv.dev/GHSA-rmj7-2vxq-3g9f) | CVE-2026-54513 | com.fasterxml.jackson.core:jackson-databind | 2.16.1 |  |
| HIGH | [GHSA-j3rv-43j4-c7qm](https://osv.dev/GHSA-j3rv-43j4-c7qm) | CVE-2026-54512 | com.fasterxml.jackson.core:jackson-databind | 2.17.2 |  |
| HIGH | [GHSA-rmj7-2vxq-3g9f](https://osv.dev/GHSA-rmj7-2vxq-3g9f) | CVE-2026-54513 | com.fasterxml.jackson.core:jackson-databind | 2.17.2 |  |
| HIGH | [GHSA-j3rv-43j4-c7qm](https://osv.dev/GHSA-j3rv-43j4-c7qm) | CVE-2026-54512 | com.fasterxml.jackson.core:jackson-databind | 2.18.5 |  |
| HIGH | [GHSA-rmj7-2vxq-3g9f](https://osv.dev/GHSA-rmj7-2vxq-3g9f) | CVE-2026-54513 | com.fasterxml.jackson.core:jackson-databind | 2.18.5 |  |
| HIGH | [GHSA-m2cm-222f-qw44](https://osv.dev/GHSA-m2cm-222f-qw44) | CVE-2026-27727 | com.mchange:mchange-commons-java | 0.2.15 |  |
| HIGH | [GHSA-h7rh-xfpj-hpcm](https://osv.dev/GHSA-h7rh-xfpj-hpcm) | CVE-2025-59952 | io.minio:minio | 8.5.9 |  |
| HIGH | [GHSA-cm33-6792-r9fm](https://osv.dev/GHSA-cm33-6792-r9fm) | CVE-2026-42579 | io.netty:netty-codec-dns | 4.1.130.Final |  |
| HIGH | [GHSA-f6hv-jmp6-3vwv](https://osv.dev/GHSA-f6hv-jmp6-3vwv) | CVE-2026-42587 | io.netty:netty-codec-http2 | 4.1.130.Final |  |
| HIGH | [GHSA-w9fj-cfpg-grvv](https://osv.dev/GHSA-w9fj-cfpg-grvv) | CVE-2026-33871 | io.netty:netty-codec-http2 | 4.1.130.Final |  |
| HIGH | [GHSA-57rv-r2g8-2cj3](https://osv.dev/GHSA-57rv-r2g8-2cj3) | CVE-2026-42584 | io.netty:netty-codec-http | 4.1.130.Final |  |
| HIGH | [GHSA-f6hv-jmp6-3vwv](https://osv.dev/GHSA-f6hv-jmp6-3vwv) | CVE-2026-42587 | io.netty:netty-codec-http | 4.1.130.Final |  |
| HIGH | [GHSA-pwqr-wmgm-9rr8](https://osv.dev/GHSA-pwqr-wmgm-9rr8) | CVE-2026-33870 | io.netty:netty-codec-http | 4.1.130.Final |  |
| HIGH | [GHSA-mj4r-2hfc-f8p6](https://osv.dev/GHSA-mj4r-2hfc-f8p6) | CVE-2026-42583 | io.netty:netty-codec | 4.1.130.Final |  |
| HIGH | [GHSA-3qp7-7mw8-wx86](https://osv.dev/GHSA-3qp7-7mw8-wx86) | CVE-2026-44249 | io.netty:netty-handler | 4.1.130.Final |  |
| HIGH | [GHSA-c653-97m9-rcg9](https://osv.dev/GHSA-c653-97m9-rcg9) | CVE-2026-50010 | io.netty:netty-handler | 4.1.130.Final |  |
| HIGH | [GHSA-x4gw-5cx5-pgmh](https://osv.dev/GHSA-x4gw-5cx5-pgmh) | CVE-2026-45416 | io.netty:netty-handler | 4.1.130.Final |  |
| HIGH | [GHSA-5pvg-856g-cp85](https://osv.dev/GHSA-5pvg-856g-cp85) | CVE-2026-47691 | io.netty:netty-resolver-dns | 4.1.130.Final |  |
| HIGH | [GHSA-676x-f7gg-47vc](https://osv.dev/GHSA-676x-f7gg-47vc) | CVE-2026-45674 | io.netty:netty-resolver-dns | 4.1.130.Final |  |
| HIGH | [GHSA-563x-q5rq-57qp](https://osv.dev/GHSA-563x-q5rq-57qp) | CVE-2026-24880 | org.apache.tomcat.embed:tomcat-embed-core | 10.1.50 |  |
| HIGH | [GHSA-5mp6-jrq3-r938](https://osv.dev/GHSA-5mp6-jrq3-r938) | CVE-2026-43513 | org.apache.tomcat.embed:tomcat-embed-core | 10.1.50 |  |
| HIGH | [GHSA-fv25-8xcx-gqjc](https://osv.dev/GHSA-fv25-8xcx-gqjc) | CVE-2026-42498 | org.apache.tomcat.embed:tomcat-embed-core | 10.1.50 |  |
| HIGH | [GHSA-gx5v-xp9w-j4cg](https://osv.dev/GHSA-gx5v-xp9w-j4cg) | CVE-2026-41284 | org.apache.tomcat.embed:tomcat-embed-core | 10.1.50 |  |
| HIGH | [GHSA-mgp5-rv84-w37q](https://osv.dev/GHSA-mgp5-rv84-w37q) | CVE-2026-24734 | org.apache.tomcat.embed:tomcat-embed-core | 10.1.50 |  |
| HIGH | [GHSA-rv64-5gf8-9qq8](https://osv.dev/GHSA-rv64-5gf8-9qq8) | CVE-2026-34483 | org.apache.tomcat.embed:tomcat-embed-core | 10.1.50 |  |
| HIGH | [GHSA-98qh-xjc8-98pq](https://osv.dev/GHSA-98qh-xjc8-98pq) | CVE-2026-42198 | org.postgresql:postgresql | 42.7.8 |  |
| HIGH | [GHSA-8hfc-fq58-r658](https://osv.dev/GHSA-8hfc-fq58-r658) | CVE-2026-22731 | org.springframework.boot:spring-boot-starter-actuator | 3.4.13 |  |
| HIGH | [GHSA-mgvc-8q2h-5pgc](https://osv.dev/GHSA-mgvc-8q2h-5pgc) | CVE-2026-22733 | org.springframework.boot:spring-boot-starter-actuator | 3.4.13 |  |
| HIGH | [GHSA-wwpq-f5c3-7hvx](https://osv.dev/GHSA-wwpq-f5c3-7hvx) | CVE-2026-40973 | org.springframework.boot:spring-boot | 3.4.13 |  |
| MEDIUM | [GHSA-72hv-8253-57qq](https://osv.dev/GHSA-72hv-8253-57qq) | - | com.fasterxml.jackson.core:jackson-core | 2.18.5 |  |
| MEDIUM | [GHSA-3wrr-7qpf-2prh](https://osv.dev/GHSA-3wrr-7qpf-2prh) | CVE-2026-50193 | com.fasterxml.jackson.core:jackson-databind | 2.12.7.1 |  |
| MEDIUM | [GHSA-5jmj-h7xm-6q6v](https://osv.dev/GHSA-5jmj-h7xm-6q6v) | CVE-2026-54515 | com.fasterxml.jackson.core:jackson-databind | 2.12.7.1 |  |
| MEDIUM | [GHSA-hgj6-7826-r7m5](https://osv.dev/GHSA-hgj6-7826-r7m5) | CVE-2026-54514 | com.fasterxml.jackson.core:jackson-databind | 2.12.7.1 |  |
| MEDIUM | [GHSA-5jmj-h7xm-6q6v](https://osv.dev/GHSA-5jmj-h7xm-6q6v) | CVE-2026-54515 | com.fasterxml.jackson.core:jackson-databind | 2.16.1 |  |
| MEDIUM | [GHSA-hgj6-7826-r7m5](https://osv.dev/GHSA-hgj6-7826-r7m5) | CVE-2026-54514 | com.fasterxml.jackson.core:jackson-databind | 2.16.1 |  |
| MEDIUM | [GHSA-5jmj-h7xm-6q6v](https://osv.dev/GHSA-5jmj-h7xm-6q6v) | CVE-2026-54515 | com.fasterxml.jackson.core:jackson-databind | 2.17.2 |  |
| MEDIUM | [GHSA-hgj6-7826-r7m5](https://osv.dev/GHSA-hgj6-7826-r7m5) | CVE-2026-54514 | com.fasterxml.jackson.core:jackson-databind | 2.17.2 |  |
| MEDIUM | [GHSA-5jmj-h7xm-6q6v](https://osv.dev/GHSA-5jmj-h7xm-6q6v) | CVE-2026-54515 | com.fasterxml.jackson.core:jackson-databind | 2.18.5 |  |
| MEDIUM | [GHSA-hgj6-7826-r7m5](https://osv.dev/GHSA-hgj6-7826-r7m5) | CVE-2026-54514 | com.fasterxml.jackson.core:jackson-databind | 2.18.5 |  |
| MEDIUM | [GHSA-563q-j3cm-6jxm](https://osv.dev/GHSA-563q-j3cm-6jxm) | CVE-2026-50560 | io.netty:netty-codec-http2 | 4.1.130.Final |  |
| MEDIUM | [GHSA-5x3r-wrvg-rp6q](https://osv.dev/GHSA-5x3r-wrvg-rp6q) | CVE-2026-47244 | io.netty:netty-codec-http2 | 4.1.130.Final |  |
| MEDIUM | [GHSA-c2gf-v879-257j](https://osv.dev/GHSA-c2gf-v879-257j) | CVE-2026-48043 | io.netty:netty-codec-http2 | 4.1.130.Final |  |
| MEDIUM | [GHSA-38f8-5428-x5cv](https://osv.dev/GHSA-38f8-5428-x5cv) | CVE-2026-42585 | io.netty:netty-codec-http | 4.1.130.Final |  |
| MEDIUM | [GHSA-hvcg-qmg6-jm4c](https://osv.dev/GHSA-hvcg-qmg6-jm4c) | CVE-2026-50020 | io.netty:netty-codec-http | 4.1.130.Final |  |
| MEDIUM | [GHSA-m4cv-j2px-7723](https://osv.dev/GHSA-m4cv-j2px-7723) | CVE-2026-42580 | io.netty:netty-codec-http | 4.1.130.Final |  |
| MEDIUM | [GHSA-v8h7-rr48-vmmv](https://osv.dev/GHSA-v8h7-rr48-vmmv) | CVE-2026-41417 | io.netty:netty-codec-http | 4.1.130.Final |  |
| MEDIUM | [GHSA-xxqh-mfjm-7mv9](https://osv.dev/GHSA-xxqh-mfjm-7mv9) | CVE-2026-42581 | io.netty:netty-codec-http | 4.1.130.Final |  |
| MEDIUM | [GHSA-xmv7-r254-6q78](https://osv.dev/GHSA-xmv7-r254-6q78) | CVE-2026-45673 | io.netty:netty-resolver-dns | 4.1.130.Final |  |
| MEDIUM | [GHSA-w573-9ffj-6ff9](https://osv.dev/GHSA-w573-9ffj-6ff9) | CVE-2026-45536 | io.netty:netty-transport-native-epoll | 4.1.130.Final |  |
| MEDIUM | [GHSA-rcgg-9c38-7xpx](https://osv.dev/GHSA-rcgg-9c38-7xpx) | CVE-2026-45292 | io.opentelemetry:opentelemetry-api | 1.43.0 |  |
| MEDIUM | [GHSA-4265-ccf5-phj5](https://osv.dev/GHSA-4265-ccf5-phj5) | CVE-2024-26308 | org.apache.commons:commons-compress | 1.24.0 |  |
| MEDIUM | [GHSA-4g9r-vxhx-9pgx](https://osv.dev/GHSA-4g9r-vxhx-9pgx) | CVE-2024-25710 | org.apache.commons:commons-compress | 1.24.0 |  |
| MEDIUM | [GHSA-j288-q9x7-2f5v](https://osv.dev/GHSA-j288-q9x7-2f5v) | CVE-2025-48924 | org.apache.commons:commons-lang3 | 3.17.0 |  |
| MEDIUM | [GHSA-gmg8-593g-7mv3](https://osv.dev/GHSA-gmg8-593g-7mv3) | CVE-2025-31672 | org.apache.poi:poi-ooxml | 5.3.0 |  |
| MEDIUM | [GHSA-8mc5-53m5-3qj2](https://osv.dev/GHSA-8mc5-53m5-3qj2) | CVE-2026-32990 | org.apache.tomcat.embed:tomcat-embed-core | 10.1.50 |  |
| MEDIUM | [GHSA-9m3c-qcxr-9x87](https://osv.dev/GHSA-9m3c-qcxr-9x87) | CVE-2026-25854 | org.apache.tomcat.embed:tomcat-embed-core | 10.1.50 |  |
| MEDIUM | [GHSA-4h8f-2wvx-gg5w](https://osv.dev/GHSA-4h8f-2wvx-gg5w) | CVE-2024-34447 | org.bouncycastle:bcprov-jdk18on | 1.77 |  |
| MEDIUM | [GHSA-67mf-3cr5-8w23](https://osv.dev/GHSA-67mf-3cr5-8w23) | CVE-2025-8885 | org.bouncycastle:bcprov-jdk18on | 1.77 |  |
| MEDIUM | [GHSA-8xfc-gm6g-vgpv](https://osv.dev/GHSA-8xfc-gm6g-vgpv) | CVE-2024-29857 | org.bouncycastle:bcprov-jdk18on | 1.77 |  |
| MEDIUM | [GHSA-c3fc-8qff-9hwx](https://osv.dev/GHSA-c3fc-8qff-9hwx) | CVE-2026-0636 | org.bouncycastle:bcprov-jdk18on | 1.77 |  |
| MEDIUM | [GHSA-m44j-cfrm-g8qc](https://osv.dev/GHSA-m44j-cfrm-g8qc) | CVE-2024-30172 | org.bouncycastle:bcprov-jdk18on | 1.77 |  |
| MEDIUM | [GHSA-v435-xc8x-wvr9](https://osv.dev/GHSA-v435-xc8x-wvr9) | CVE-2024-30171 | org.bouncycastle:bcprov-jdk18on | 1.77 |  |
| MEDIUM | [GHSA-x2wq-9x2f-fhj7](https://osv.dev/GHSA-x2wq-9x2f-fhj7) | CVE-2026-22751 | org.springframework.security:spring-security-core | 6.4.13 |  |
| MEDIUM | [GHSA-cvc6-q2cp-2xhw](https://osv.dev/GHSA-cvc6-q2cp-2xhw) | CVE-2026-22748 | org.springframework.security:spring-security-oauth2-jose | 6.4.13 |  |
| MEDIUM | [GHSA-4773-3jfm-qmx3](https://osv.dev/GHSA-4773-3jfm-qmx3) | CVE-2026-22737 | org.springframework:spring-webflux | 6.2.15 |  |
| MEDIUM | [GHSA-6p4f-wcwh-5vvm](https://osv.dev/GHSA-6p4f-wcwh-5vvm) | CVE-2026-22745 | org.springframework:spring-webflux | 6.2.15 |  |
| MEDIUM | [GHSA-4773-3jfm-qmx3](https://osv.dev/GHSA-4773-3jfm-qmx3) | CVE-2026-22737 | org.springframework:spring-webmvc | 6.2.15 |  |
| MEDIUM | [GHSA-6p4f-wcwh-5vvm](https://osv.dev/GHSA-6p4f-wcwh-5vvm) | CVE-2026-22745 | org.springframework:spring-webmvc | 6.2.15 |  |
| LOW | [GHSA-qqpg-mvqg-649v](https://osv.dev/GHSA-qqpg-mvqg-649v) | CVE-2026-1225 | ch.qos.logback:logback-core | 1.5.22 |  |
| LOW | [GHSA-45q3-82m4-75jr](https://osv.dev/GHSA-45q3-82m4-75jr) | CVE-2026-42578 | io.netty:netty-handler-proxy | 4.1.130.Final |  |
| LOW | [GHSA-9m89-8frq-c98c](https://osv.dev/GHSA-9m89-8frq-c98c) | CVE-2026-43514 | org.apache.tomcat.embed:tomcat-embed-core | 10.1.50 |  |
| LOW | [GHSA-vxf7-qj7q-83fh](https://osv.dev/GHSA-vxf7-qj7q-83fh) | CVE-2026-22746 | org.springframework.security:spring-security-core | 6.4.13 |  |
| LOW | [GHSA-5843-p793-ghmm](https://osv.dev/GHSA-5843-p793-ghmm) | CVE-2026-22740 | org.springframework:spring-webflux | 6.2.15 |  |
| LOW | [GHSA-6hcq-hmm3-jj3c](https://osv.dev/GHSA-6hcq-hmm3-jj3c) | CVE-2026-22735 | org.springframework:spring-webflux | 6.2.15 |  |
| LOW | [GHSA-wg35-8jpf-2xv3](https://osv.dev/GHSA-wg35-8jpf-2xv3) | CVE-2026-22741 | org.springframework:spring-webflux | 6.2.15 |  |
| LOW | [GHSA-6hcq-hmm3-jj3c](https://osv.dev/GHSA-6hcq-hmm3-jj3c) | CVE-2026-22735 | org.springframework:spring-webmvc | 6.2.15 |  |
| LOW | [GHSA-wg35-8jpf-2xv3](https://osv.dev/GHSA-wg35-8jpf-2xv3) | CVE-2026-22741 | org.springframework:spring-webmvc | 6.2.15 |  |
