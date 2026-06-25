# OSV Scan Evidence — Frontend (npm)

**Date:** 2026-06-25 · **Tool:** osv-scanner 2.4.0 · **Source:** Google OSV (no API key)

> ⚠️ Scanned the CURRENTLY COMMITTED `pnpm-lock.yaml` (Angular **17** / Strapi **4** — the pre-upgrade baseline, since the Angular-19/Strapi-5 lockfile cannot be regenerated against the mock registry here). A few pinned overrides (e.g. `lodash 4.18.0`) are mock-only versions absent from OSV and do not appear.

```
Starting filesystem walk for root: C:\
Scanned C:\avgc-xr-portal\pnpm-lock.yaml file and found 2331 packages
End status: 0 dirs visited, 1 inodes visited, 1 Extract calls, 170.1081ms elapsed, 170.1081ms wall time

Total 37 packages affected by 96 known vulnerabilities (4 Critical, 22 High, 58 Medium, 11 Low, 1 Unknown) from 1 ecosystem.
95 vulnerabilities can be fixed.

+-------------------------------------+------+-----------+----------------------------------+---------+---------------+----------------+
| OSV URL                             | CVSS | ECOSYSTEM | PACKAGE                          | VERSION | FIXED VERSION | SOURCE         |
+-------------------------------------+------+-----------+----------------------------------+---------+---------------+----------------+
| https://osv.dev/GHSA-39pv-4j6c-2g6v | 8.8  | npm       | @angular/common                  | 17.3.12 | 20.3.25       | pnpm-lock.yaml |
| https://osv.dev/GHSA-48r7-hpm6-gfxm | 8.2  | npm       | @angular/common                  | 17.3.12 | 20.3.25       | pnpm-lock.yaml |
| https://osv.dev/GHSA-58c5-g7wp-6w37 | 7.7  | npm       | @angular/common                  | 17.3.12 | 19.2.16       | pnpm-lock.yaml |
| https://osv.dev/GHSA-p3vc-36g9-x9gr | 8.2  | npm       | @angular/common                  | 17.3.12 | 19.2.23       | pnpm-lock.yaml |
| https://osv.dev/GHSA-q6f4-qqrg-jv6x | 8.2  | npm       | @angular/common                  | 17.3.12 | 19.2.23       | pnpm-lock.yaml |
| https://osv.dev/GHSA-58w9-8g37-x9v5 | 5.3  | npm       | @angular/compiler                | 17.3.12 | 20.3.25       | pnpm-lock.yaml |
| https://osv.dev/GHSA-f3m7-gqxr-g87x | 5.3  | npm       | @angular/compiler                | 17.3.12 | 19.2.22       | pnpm-lock.yaml |
| https://osv.dev/GHSA-g93w-mfhg-p222 | 9.0  | npm       | @angular/compiler                | 17.3.12 | 19.2.20       | pnpm-lock.yaml |
| https://osv.dev/GHSA-jrmj-c5cx-3cw6 | 8.5  | npm       | @angular/compiler                | 17.3.12 | 19.2.18       | pnpm-lock.yaml |
| https://osv.dev/GHSA-v4hv-rgfq-gp49 | 8.5  | npm       | @angular/compiler                | 17.3.12 | 19.2.17       | pnpm-lock.yaml |
| https://osv.dev/GHSA-692r-grfm-v8x7 | 5.3  | npm       | @angular/core                    | 17.3.12 | 19.2.23       | pnpm-lock.yaml |
| https://osv.dev/GHSA-f3m7-gqxr-g87x | 5.3  | npm       | @angular/core                    | 17.3.12 | 19.2.22       | pnpm-lock.yaml |
| https://osv.dev/GHSA-g93w-mfhg-p222 | 9.0  | npm       | @angular/core                    | 17.3.12 | 19.2.20       | pnpm-lock.yaml |
| https://osv.dev/GHSA-jrmj-c5cx-3cw6 | 8.5  | npm       | @angular/core                    | 17.3.12 | 19.2.18       | pnpm-lock.yaml |
| https://osv.dev/GHSA-prjf-86w9-mfqv | 7.0  | npm       | @angular/core                    | 17.3.12 | 19.2.19       | pnpm-lock.yaml |
| https://osv.dev/GHSA-rgjc-h3x7-9mwg | 8.6  | npm       | @angular/core                    | 17.3.12 | 20.3.25       | pnpm-lock.yaml |
| https://osv.dev/GHSA-45q2-gjvg-7973 | 8.7  | npm       | @angular/platform-server         | 17.3.12 | 19.2.21       | pnpm-lock.yaml |
| https://osv.dev/GHSA-68x2-mx4q-78m7 | 7.1  | npm       | @angular/platform-server         | 17.3.12 | 18.2.14       | pnpm-lock.yaml |
| https://osv.dev/GHSA-gxx4-3xcv-f8qx | 8.6  | npm       | @angular/platform-server         | 17.3.12 | 19.2.25       | pnpm-lock.yaml |
| https://osv.dev/GHSA-hqr9-c56f-3x7f | 8.6  | npm       | @angular/platform-server         | 17.3.12 | 19.2.25       | pnpm-lock.yaml |
| https://osv.dev/GHSA-rfh7-fxqc-q52v | 8.8  | npm       | @angular/platform-server         | 17.3.12 | 19.2.22       | pnpm-lock.yaml |
| https://osv.dev/GHSA-xrxm-cp7j-8xf6 | 8.8  | npm       | @angular/platform-server         | 17.3.12 | 19.2.23       | pnpm-lock.yaml |
| https://osv.dev/GHSA-68x2-mx4q-78m7 | 7.1  | npm       | @angular/ssr                     | 17.3.12 | 18.2.21       | pnpm-lock.yaml |
| https://osv.dev/GHSA-x288-3778-4hhx | 9.2  | npm       | @angular/ssr                     | 17.3.12 | 19.2.21       | pnpm-lock.yaml |
| https://osv.dev/GHSA-4x5r-pxfx-6jf8 | 3.2  | npm       | @babel/core                      | 7.23.9  | 7.29.6        | pnpm-lock.yaml |
| https://osv.dev/GHSA-4x5r-pxfx-6jf8 | 3.2  | npm       | @babel/core                      | 7.24.0  | 7.29.6        | pnpm-lock.yaml |
| https://osv.dev/GHSA-968p-4wvh-cqc8 | 6.2  | npm       | @babel/runtime                   | 7.24.0  | 7.26.10       | pnpm-lock.yaml |
| https://osv.dev/GHSA-hvp3-26wx-g2w4 | 2.1  | npm       | @strapi/admin                    | 4.26.2  | 5.33.3        | pnpm-lock.yaml |
| https://osv.dev/GHSA-7mqx-wwh4-f9fw | 6.9  | npm       | @strapi/plugin-users-permissions | 4.26.2  | 5.45.0        | pnpm-lock.yaml |
| https://osv.dev/GHSA-hvp3-26wx-g2w4 | 2.1  | npm       | @strapi/plugin-users-permissions | 4.26.2  | 5.33.3        | pnpm-lock.yaml |
| https://osv.dev/GHSA-4r8w-3jww-m2rp | 6.3  | npm       | @strapi/strapi                   | 4.26.2  | 5.24.1        | pnpm-lock.yaml |
| https://osv.dev/GHSA-rjg2-95x7-8qmx | 9.2  | npm       | @strapi/strapi                   | 4.26.2  | 5.37.0        | pnpm-lock.yaml |
| https://osv.dev/GHSA-2g4f-4pwh-qvx6 | 5.5  | npm       | ajv                              | 8.12.0  | 8.18.0        | pnpm-lock.yaml |
| https://osv.dev/GHSA-2g4f-4pwh-qvx6 | 5.5  | npm       | ajv                              | 8.13.0  | 8.18.0        | pnpm-lock.yaml |
| https://osv.dev/GHSA-2g4f-4pwh-qvx6 | 5.5  | npm       | ajv                              | 8.17.1  | 8.18.0        | pnpm-lock.yaml |
| https://osv.dev/GHSA-pxg6-pf52-xh8x |      | npm       | cookie                           | 0.4.2   | 0.7.0         | pnpm-lock.yaml |
| https://osv.dev/GHSA-848j-6mx2-7j84 | 5.6  | npm       | elliptic                         | 6.6.1   | --            | pnpm-lock.yaml |
| https://osv.dev/GHSA-67mh-4wv8-2f99 | 5.3  | npm       | esbuild                          | 0.16.17 | 0.25.0        | pnpm-lock.yaml |
| https://osv.dev/GHSA-67mh-4wv8-2f99 | 5.3  | npm       | esbuild                          | 0.19.11 | 0.25.0        | pnpm-lock.yaml |
| https://osv.dev/GHSA-67mh-4wv8-2f99 | 5.3  | npm       | esbuild                          | 0.20.1  | 0.25.0        | pnpm-lock.yaml |
| https://osv.dev/GHSA-67mh-4wv8-2f99 | 5.3  | npm       | esbuild                          | 0.20.2  | 0.25.0        | pnpm-lock.yaml |
| https://osv.dev/GHSA-67mh-4wv8-2f99 | 5.3  | npm       | esbuild                          | 0.21.5  | 0.25.0        | pnpm-lock.yaml |
| https://osv.dev/GHSA-4www-5p9h-95mh | 4.0  | npm       | http-proxy-middleware            | 2.0.7   | 2.0.8         | pnpm-lock.yaml |
| https://osv.dev/GHSA-64mm-vxmg-q3vj | 6.9  | npm       | http-proxy-middleware            | 2.0.7   | 2.0.10        | pnpm-lock.yaml |
| https://osv.dev/GHSA-9gqv-wp59-fq42 | 4.0  | npm       | http-proxy-middleware            | 2.0.7   | 2.0.9         | pnpm-lock.yaml |
| https://osv.dev/GHSA-h67p-54hq-rp68 | 5.3  | npm       | js-yaml                          | 3.14.2  | 4.2.0         | pnpm-lock.yaml |
| https://osv.dev/GHSA-h67p-54hq-rp68 | 5.3  | npm       | js-yaml                          | 4.1.0   | 4.2.0         | pnpm-lock.yaml |
| https://osv.dev/GHSA-mh29-5h37-fv8m | 5.3  | npm       | js-yaml                          | 4.1.0   | 4.1.1         | pnpm-lock.yaml |
| https://osv.dev/GHSA-6v5v-wf23-fmfq | 5.3  | npm       | markdown-it                      | 12.3.2  | 14.2.0        | pnpm-lock.yaml |
| https://osv.dev/GHSA-qx2v-qp2m-jg93 | 6.1  | npm       | postcss                          | 8.4.35  | 8.5.10        | pnpm-lock.yaml |
| https://osv.dev/GHSA-6rw7-vpxm-498p | 6.3  | npm       | qs                               | 6.11.1  | 6.14.1        | pnpm-lock.yaml |
| https://osv.dev/GHSA-q8mj-m7cp-5q26 | 6.3  | npm       | qs                               | 6.11.1  | 6.15.2        | pnpm-lock.yaml |
| https://osv.dev/GHSA-w7fw-mjwx-w883 | 3.7  | npm       | qs                               | 6.11.1  | 6.14.2        | pnpm-lock.yaml |
| https://osv.dev/GHSA-q8mj-m7cp-5q26 | 6.3  | npm       | qs                               | 6.15.1  | 6.15.2        | pnpm-lock.yaml |
| https://osv.dev/GHSA-qj8w-gfj5-8c6v | 5.9  | npm       | serialize-javascript             | 7.0.3   | 7.0.5         | pnpm-lock.yaml |
| https://osv.dev/GHSA-vmf3-w455-68vh | 6.9  | npm       | tar                              | 7.5.11  | 7.5.16        | pnpm-lock.yaml |
| https://osv.dev/GHSA-w5hq-g745-h8pq | 7.5  | npm       | uuid                             | 8.3.2   | 11.1.1        | pnpm-lock.yaml |
| https://osv.dev/GHSA-356w-63v5-8wf4 | 6.0  | npm       | vite                             | 5.0.13  | 5.4.18        | pnpm-lock.yaml |
| https://osv.dev/GHSA-4r4m-qw57-chr8 | 5.3  | npm       | vite                             | 5.0.13  | 5.4.16        | pnpm-lock.yaml |
| https://osv.dev/GHSA-4w7w-66w2-5vf9 | 6.3  | npm       | vite                             | 5.0.13  | 6.4.2         | pnpm-lock.yaml |
| https://osv.dev/GHSA-64vr-g452-qvp3 | 6.4  | npm       | vite                             | 5.0.13  | 5.1.8         | pnpm-lock.yaml |
| https://osv.dev/GHSA-859w-5945-r5v3 | 6.0  | npm       | vite                             | 5.0.13  | 5.4.19        | pnpm-lock.yaml |
| https://osv.dev/GHSA-93m4-6634-74q7 | 6.0  | npm       | vite                             | 5.0.13  | 5.4.21        | pnpm-lock.yaml |
| https://osv.dev/GHSA-9cwx-2883-4wfx | 6.9  | npm       | vite                             | 5.0.13  | 5.1.8         | pnpm-lock.yaml |
| https://osv.dev/GHSA-c27g-q93r-2cwf | 7.5  | npm       | vite                             | 5.0.13  | 5.4.9         | pnpm-lock.yaml |
| https://osv.dev/GHSA-fx2h-pf6j-xcff | 8.2  | npm       | vite                             | 5.0.13  | 6.4.3         | pnpm-lock.yaml |
| https://osv.dev/GHSA-g4jq-h2w9-997c | 2.3  | npm       | vite                             | 5.0.13  | 5.4.20        | pnpm-lock.yaml |
| https://osv.dev/GHSA-jqfw-vq24-v9c3 | 2.3  | npm       | vite                             | 5.0.13  | 5.4.20        | pnpm-lock.yaml |
| https://osv.dev/GHSA-v6wh-96g9-6wx3 | 5.5  | npm       | vite                             | 5.0.13  | 6.4.3         | pnpm-lock.yaml |
| https://osv.dev/GHSA-vg6x-rcgg-rjx6 | 6.5  | npm       | vite                             | 5.0.13  | 5.4.12        | pnpm-lock.yaml |
| https://osv.dev/GHSA-x574-m823-4x7w | 5.3  | npm       | vite                             | 5.0.13  | 5.4.15        | pnpm-lock.yaml |
| https://osv.dev/GHSA-xcj6-pq6g-qj4x | 5.3  | npm       | vite                             | 5.0.13  | 5.4.17        | pnpm-lock.yaml |
| https://osv.dev/GHSA-356w-63v5-8wf4 | 6.0  | npm       | vite                             | 5.4.14  | 5.4.18        | pnpm-lock.yaml |
| https://osv.dev/GHSA-4r4m-qw57-chr8 | 5.3  | npm       | vite                             | 5.4.14  | 5.4.16        | pnpm-lock.yaml |
| https://osv.dev/GHSA-4w7w-66w2-5vf9 | 6.3  | npm       | vite                             | 5.4.14  | 6.4.2         | pnpm-lock.yaml |
| https://osv.dev/GHSA-859w-5945-r5v3 | 6.0  | npm       | vite                             | 5.4.14  | 5.4.19        | pnpm-lock.yaml |
| https://osv.dev/GHSA-93m4-6634-74q7 | 6.0  | npm       | vite                             | 5.4.14  | 5.4.21        | pnpm-lock.yaml |
| https://osv.dev/GHSA-fx2h-pf6j-xcff | 8.2  | npm       | vite                             | 5.4.14  | 6.4.3         | pnpm-lock.yaml |
| https://osv.dev/GHSA-g4jq-h2w9-997c | 2.3  | npm       | vite                             | 5.4.14  | 5.4.20        | pnpm-lock.yaml |
| https://osv.dev/GHSA-jqfw-vq24-v9c3 | 2.3  | npm       | vite                             | 5.4.14  | 5.4.20        | pnpm-lock.yaml |
| https://osv.dev/GHSA-v6wh-96g9-6wx3 | 5.5  | npm       | vite                             | 5.4.14  | 6.4.3         | pnpm-lock.yaml |
| https://osv.dev/GHSA-x574-m823-4x7w | 5.3  | npm       | vite                             | 5.4.14  | 5.4.15        | pnpm-lock.yaml |
| https://osv.dev/GHSA-xcj6-pq6g-qj4x | 5.3  | npm       | vite                             | 5.4.14  | 5.4.17        | pnpm-lock.yaml |
| https://osv.dev/GHSA-4w7w-66w2-5vf9 | 6.3  | npm       | vite                             | 5.4.21  | 6.4.2         | pnpm-lock.yaml |
| https://osv.dev/GHSA-fx2h-pf6j-xcff | 8.2  | npm       | vite                             | 5.4.21  | 6.4.3         | pnpm-lock.yaml |
| https://osv.dev/GHSA-v6wh-96g9-6wx3 | 5.5  | npm       | vite                             | 5.4.21  | 6.4.3         | pnpm-lock.yaml |
| https://osv.dev/GHSA-38r7-794h-5758 | 3.7  | npm       | webpack                          | 5.94.0  | 5.104.0       | pnpm-lock.yaml |
| https://osv.dev/GHSA-8fgc-7cc6-rx7x | 3.7  | npm       | webpack                          | 5.94.0  | 5.104.1       | pnpm-lock.yaml |
| https://osv.dev/GHSA-4v9v-hfq4-rm2v | 5.3  | npm       | webpack-dev-server               | 4.15.1  | 5.2.1         | pnpm-lock.yaml |
| https://osv.dev/GHSA-79cf-xcqc-c78w | 5.3  | npm       | webpack-dev-server               | 4.15.1  | 5.2.4         | pnpm-lock.yaml |
| https://osv.dev/GHSA-9jgg-88mc-972h | 6.5  | npm       | webpack-dev-server               | 4.15.1  | 5.2.1         | pnpm-lock.yaml |
| https://osv.dev/GHSA-mx8g-39q3-5c79 | 5.3  | npm       | webpack-dev-server               | 4.15.1  | 5.2.5         | pnpm-lock.yaml |
| https://osv.dev/GHSA-4v9v-hfq4-rm2v | 5.3  | npm       | webpack-dev-server               | 4.15.2  | 5.2.1         | pnpm-lock.yaml |
| https://osv.dev/GHSA-79cf-xcqc-c78w | 5.3  | npm       | webpack-dev-server               | 4.15.2  | 5.2.4         | pnpm-lock.yaml |
| https://osv.dev/GHSA-9jgg-88mc-972h | 6.5  | npm       | webpack-dev-server               | 4.15.2  | 5.2.1         | pnpm-lock.yaml |
| https://osv.dev/GHSA-mx8g-39q3-5c79 | 5.3  | npm       | webpack-dev-server               | 4.15.2  | 5.2.5         | pnpm-lock.yaml |
+-------------------------------------+------+-----------+----------------------------------+---------+---------------+----------------+
```
