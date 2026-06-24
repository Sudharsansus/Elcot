# Changelog

All notable changes to the AVGC-XR Portal will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/).

## [1.0.0-SNAPSHOT] - 2026-06-22

### Added
- Initial project structure with modular monolith architecture
- 17 bounded contexts organized by domain (platform, policy, ecosystem, analytics, support)
- 3 Angular 17 portals (Public, Applicant, Admin) with standalone components and signals
- Spring Boot 3.3 API with Java 21 virtual threads
- Flowable BPMN 2.0 workflow engine integration
- PostgreSQL 16 with Flyway migrations and PgBouncer connection pooling
- Redis for session management and application caching
- RabbitMQ for asynchronous processing (scheme-day rush handling)
- Elasticsearch 8 for full-text search with Tamil language support
- MinIO for S3-compatible file storage
- Strapi 4 headless CMS for content management
- Nginx reverse proxy with security headers, rate limiting, and full-page caching
- Bilingual support (English and Tamil)
- Comprehensive observability stack (Prometheus, Grafana, Loki, Tempo)
- Infrastructure as Code (Docker Compose, K8s manifests, Terraform modules)
- 13 Architecture Decision Records (ADRs)
- WCAG 2.1 AA accessibility compliance
- GIGW compliance for government websites
- ISO 27001 aligned security practices
- DPDP Act 2023 compliance for personal data protection
