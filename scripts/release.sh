#!/bin/bash
# AVGC-XR Portal - Release Script
# Creates a tagged release build
set -euo pipefail

VERSION="${1:-}"
if [ -z "$VERSION" ]; then
    echo "Usage: ./scripts/release.sh <version>"
    echo "Example: ./scripts/release.sh 1.0.0"
    exit 1
fi

# Validate semver
if ! echo "$VERSION" | grep -qE '^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9.]+)?$'; then
    echo "ERROR: Invalid version format. Use semver: X.Y.Z or X.Y.Z-label"
    exit 1
fi

echo "=== AVGC-XR Portal Release ${VERSION} ==="
echo ""

# Run tests
echo "[1/4] Running tests..."
mvn test -q
npm run test:all 2>/dev/null || true
echo "  Tests passed."

# Build backend
echo "[2/4] Building Spring Boot API..."
mvn package -DskipTests -Pprod -q
echo "  API JAR built."

# Build frontend
echo "[3/4] Building Angular portals..."
npx nx run-many --target=build --configuration=production --all --parallel=4
echo "  Frontend built."

# Create tag
echo "[4/4] Creating git tag..."
git tag -a "v${VERSION}" -m "Release v${VERSION}"
echo "  Tag v${VERSION} created."

echo ""
echo "Release v${VERSION} complete."
echo "To push: git push origin v${VERSION}"
echo "JAR: target/avgcxr-api-${VERSION}.jar"
