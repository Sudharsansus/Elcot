# ADR-0012: MinIO S3-Compatible Object Storage

## Status

Accepted

## Date

2026-02-01

## Context

The portal handles file uploads (identity documents, project proposals, financial statements, media assets). Files range from 10KB (JPEG photos) to 50MB (video reels for AVGC applications). Storage must support pre-signed URLs for secure download, bucket-level access policies, and integration with Strapi CMS for media management.

## Decision

Use MinIO as an S3-compatible object storage service deployed on-premise. MinIO provides the S3 API interface, allowing future migration to AWS S3 or other S3-compatible storage without application changes. Files are organized into buckets: `avgcxr-documents` (applicant uploads), `avgcxr-media` (CMS media), `avgcxr-exports` (generated reports), `avgcxr-backups`, and `avgcxr-profiles`.

## Consequences

### Positive

- S3-compatible API allows future cloud migration without code changes.
- Pre-signed URLs enable secure, time-limited file access without proxying through the API.
- Bucket-level policies enforce access control (documents: private, media: public read).
- Lifecycle policies automate backup rotation and temporary file cleanup.
- No vendor lock-in to cloud object storage providers.

### Negative

- MinIO must be deployed, monitored, and backed up as part of infrastructure.
- Erasure coding for data redundancy requires at least 4 disks per server in distributed mode.
- S3 API features (versioning, event notifications) require MinIO specific configuration.

### Risks

- Risk: Disk failure causes data loss. Mitigation: Configure MinIO with erasure coding (EC:2) and regular backups to external storage.