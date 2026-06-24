#!/bin/bash
# AVGC-XR Portal - Reset Development Environment
# WARNING: Destroys all local data
set -euo pipefail

read -p "WARNING: This will delete all local data (database, cache, search index, files). Continue? [y/N] " confirm
if [ "$confirm" != "y" ] && [ "$confirm" != "Y" ]; then
    echo "Aborted."
    exit 0
fi

echo "Resetting AVGC-XR Portal development environment..."

# Stop all services
echo "  Stopping Docker services..."
docker compose -f infra/docker/docker-compose.yml down -v 2>/dev/null || true

# Clean Maven build
echo "  Cleaning Maven build..."
mvn clean -q

# Clean Angular build
echo "  Cleaning Angular build..."
rm -rf dist/
npx nx reset 2>/dev/null || true

# Clean node modules
echo "  Cleaning node_modules..."
rm -rf node_modules/.cache

echo "  Environment reset complete."
echo ""
echo "Run ./scripts/bootstrap.sh to set up again."
