#!/bin/bash
set -e
echo "==> Installing Node dependencies..."
corepack enable
pnpm install --frozen-lockfile 2>/dev/null || pnpm install
echo "==> Verifying Java version..."
java -version
echo "==> Verifying Node version..."
node -v
echo "==> Verifying Maven..."
mvn -v
echo "==> Dev container ready. Run 'make setup' to start infrastructure."
