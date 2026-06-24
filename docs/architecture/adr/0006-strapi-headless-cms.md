# ADR-0006: Strapi Headless CMS

## Status

Accepted

## Date

2026-01-20

## Context

The portal needs content management for scheme descriptions, FAQs, news articles, banner images, and static pages (About, Terms, Privacy Policy). Content must be bilingual (English + Tamil) and manageable by non-technical government staff. Content changes should not require application redeployment.

## Decision

Use Strapi 4 as a headless CMS deployed as a separate service. Strapi provides a REST API for content retrieval, a visual admin panel for content editors, and built-in i18n support for bilingual content. Media files are stored in MinIO via a custom S3-compatible upload provider. The Angular portals consume Strapi's REST API directly for content pages.

## Consequences

### Positive

- Non-technical staff can manage content without developer involvement.
- Bilingual content is natively supported via Strapi's i18n plugin.
- Content changes are instant without redeployment.
- Strapi's role-based admin access allows granular editor permissions.
- Open-source with active community and MIT license.

### Negative

- Additional service to deploy, monitor, and maintain.
- Strapi's PostgreSQL database must be backed up separately from the main application database.
- Custom upload provider integration requires maintenance.
- Strapi admin panel is a separate attack surface that must be secured.

### Risks

- Risk: Strapi version upgrade breaks custom provider. Mitigation: Pin Strapi version, test upgrades in staging first.