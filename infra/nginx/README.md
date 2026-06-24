# Nginx Configuration - AVGC-XR Portal

## Overview

Nginx serves as the reverse proxy and static asset server for the AVGC-XR Portal.

## Architecture

```
Internet → Nginx (443/80) → Angular SPA (static files)
                         → Spring Boot API (proxy :8080)
                         → Strapi CMS (proxy :1337)
```

## Configuration Files

| File | Purpose |
|------|---------|
| `nginx.conf` | Main Nginx configuration |
| `conf.d/avgcxr-portal.conf` | Main portal server block (Angular SPA) |
| `conf.d/api.conf` | API reverse proxy (Spring Boot) |
| `conf.d/cms.conf` | CMS reverse proxy (Strapi) |
| `conf.d/security-headers.conf` | GIGW-mandated security headers |
| `conf.d/gzip.conf` | Brotli/Gzip compression |
| `conf.d/rate-limit.conf` | Rate limiting zones |
| `snippets/proxy-params.conf` | Shared upstream proxy settings |
| `snippets/ssl-params.conf` | TLS 1.2+ hardening |

## Features

- TLS 1.2+ with modern cipher suites
- GIGW-compliant security headers
- Rate limiting (API: 30r/s, Auth: 5r/m, Forms: 3r/m)
- Gzip compression for static assets
- 1-year cache for hashed Angular assets
- SPA fallback routing
- Bilingual error responses (EN/TA)

## Testing Configuration

```bash
nginx -t
nginx -s reload
```
