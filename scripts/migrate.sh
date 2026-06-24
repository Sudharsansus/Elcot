#!/bin/bash
# AVGC-XR Portal - Database Migration Script
set -euo pipefail

ENV="${1:-dev}"
echo "Running Flyway migrations for environment: $ENV"

case "$ENV" in
    dev)
        mvn flyway:migrate -Dflyway.config.files=infra/docker/postgres/flyway-dev.conf
        ;;
    staging)
        mvn flyway:migrate -Dflyway.config.files=infra/docker/postgres/flyway-staging.conf
        ;;
    prod)
        mvn flyway:migrate -Dflyway.config.files=infra/docker/postgres/flyway-prod.conf             -Dflyway.outOfOrder=false
        ;;
    *)
        echo "Unknown environment: $ENV. Use: dev, staging, prod"
        exit 1
        ;;
esac

echo "Migration complete."
