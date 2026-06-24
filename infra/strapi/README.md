# Strapi CMS - Content Management for AVGC-XR Portal

## Purpose

Strapi 4 serves as the headless CMS for managing:
- Scheme descriptions and eligibility criteria (bilingual EN/TA)
- News and announcements
- FAQ content
- Banner images and promotional content
- Static pages (About, Terms, Privacy Policy)

## Architecture

```
Content Editors → Strapi Admin Panel (:1337/admin)
                → Strapi API (:1337/api)
                → MinIO (media storage)
Angular Portals → Strapi API → PostgreSQL (strapi database)
```

## Content Types

| Content Type | Description | Bilingual |
|-------------|-------------|-----------|
| Scheme | Scheme descriptions, eligibility, benefits | EN + TA |
| Article | News, announcements, press releases | EN + TA |
| Faq | Frequently asked questions | EN + TA |
| Banner | Homepage banners with CTA | Image + text |
| Page | Static pages (About, Terms, Privacy) | EN + TA |

## Configuration Files

| File | Purpose |
|------|---------|
| `config/server.js` | Server host, port, CORS |
| `config/database.js` | PostgreSQL connection |
| `config/admin.js` | Admin panel settings |
| `config/api.js` | API configuration |
| `config/plugins.js` | Plugin settings |
| `config/middlewares.js` | Security middleware |

## API Access

Content is served via Strapi REST API:
```
GET /api/schemes?locale=en&populate=*
GET /api/schemes?locale=ta&populate=*
GET /api/articles?locale=en&sort=createdAt:desc
```

## Development

```bash
cd apps/cms
npm run develop
```

Admin panel available at http://localhost:1337/admin
