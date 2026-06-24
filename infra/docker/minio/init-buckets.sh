#!/bin/bash
# Initialize MinIO buckets for AVGC-XR Portal
# Run once after MinIO container starts

set -euo pipefail

MINIO_ROOT_USER="${MINIO_ROOT_USER:-minioadmin}"
MINIO_ROOT_PASSWORD="${MINIO_ROOT_PASSWORD:-minioadmin}"
MINIO_ENDPOINT="${MINIO_ENDPOINT:-http://minio:9000}"

# Wait for MinIO to be ready
echo "Waiting for MinIO to be ready..."
for i in $(seq 1 30); do
    if mc alias set local "${MINIO_ENDPOINT}" "${MINIO_ROOT_USER}" "${MINIO_ROOT_PASSWORD}" 2>/dev/null; then
        echo "MinIO is ready."
        break
    fi
    echo "Attempt $i/30 - MinIO not ready, waiting..."
    sleep 2
done

# Create buckets
echo "Creating buckets..."

mc mb --ignore-existing local/avgcxr-documents
mc mb --ignore-existing local/avgcxr-media
mc mb --ignore-existing local/avgcxr-exports
mc mb --ignore-existing local/avgcxr-backups
mc mb --ignore-existing local/avgcxr-profiles

# Set bucket policies
echo "Setting bucket policies..."

# Documents: private, accessed via pre-signed URLs
mc anonymous set none local/avgcxr-documents

# Media: public read for approved/published content
mc anonymous set download local/avgcxr-media

# Exports: private, admin-only
mc anonymous set none local/avgcxr-exports

# Backups: private
mc anonymous set none local/avgcxr-backups

# Profiles: public read for applicant profile photos
mc anonymous set download local/avgcxr-profiles

# Set lifecycle rules - purge incomplete uploads after 7 days
mc ilm rule add --prefix "" --tags "status=INCOMPLETE" --expiry-days 7 local/avgcxr-documents

echo "Bucket initialization complete."
echo ""
echo "Buckets created:"
mc ls local/
