#!/bin/bash
# AVGC-XR Portal - Database Seeding Script
# Populates reference data: districts, categories, scheme types
set -euo pipefail

echo "Seeding reference data into AVGC-XR Portal database..."

PGHOST="${PGHOST:-localhost}"
PGPORT="${PGPORT:-5432}"
PGDATABASE="${PGDATABASE:-avgcxr_portal}"
PGUSER="${PGUSER:-avgcxr_app}"
PGPASSWORD="${PGPASSWORD:-avgcxr_app_dev_pwd}"
export PGPASSWORD

# Seed Tamil Nadu districts
psql -h "$PGHOST" -p "$PGPORT" -U "$PGUSER" -d "$PGDATABASE" -f scripts/sql/seed-districts.sql
echo "  Districts seeded."

# Seed scheme categories
psql -h "$PGHOST" -p "$PGPORT" -U "$PGUSER" -d "$PGDATABASE" -f scripts/sql/seed-categories.sql
echo "  Categories seeded."

# Seed document types
psql -h "$PGHOST" -p "$PGPORT" -U "$PGUSER" -d "$PGDATABASE" -f scripts/sql/seed-document-types.sql
echo "  Document types seeded."

# Seed workflow definitions
psql -h "$PGHOST" -p "$PGPORT" -U "$PGUSER" -d "$PGDATABASE" -f scripts/sql/seed-workflows.sql
echo "  Workflow definitions seeded."

echo "Seeding complete."
