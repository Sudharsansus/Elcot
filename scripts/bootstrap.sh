#!/bin/bash
# AVGC-XR Portal - Bootstrap Script
# Sets up the complete local development environment
set -euo pipefail

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}=== AVGC-XR Portal Bootstrap ===${NC}"
echo ""

# Check prerequisites
echo -e "${YELLOW}[1/6] Checking prerequisites...${NC}"
command -v java >/dev/null 2>&1 || { echo "ERROR: Java 21 required. Install JDK 21."; exit 1; }
command -v mvn >/dev/null 2>&1 || { echo "ERROR: Maven 3.9+ required."; exit 1; }
command -v node >/dev/null 2>&1 || { echo "ERROR: Node.js 20+ required."; exit 1; }
command -v docker >/dev/null 2>&1 || { echo "ERROR: Docker required for infrastructure."; exit 1; }
command -v docker compose >/dev/null 2>&1 || { echo "ERROR: Docker Compose required."; exit 1; }

JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo "ERROR: Java 21+ required. Found: $JAVA_VERSION"
    exit 1
fi

echo "  Java: $(java -version 2>&1 | head -1)"
echo "  Maven: $(mvn -version | head -1)"
echo "  Node: $(node --version)"
echo "  npm: $(npm --version)"
echo "  Docker: $(docker --version)"
echo ""

# Start infrastructure
echo -e "${YELLOW}[2/6] Starting Docker infrastructure...${NC}"
docker compose -f infra/docker/docker-compose.yml up -d postgres redis rabbitmq elasticsearch minio
echo "  Waiting for PostgreSQL..."
for i in $(seq 1 30); do
    if docker compose -f infra/docker/docker-compose.yml exec -T postgres pg_isready -U avgcxr_app >/dev/null 2>&1; then
        echo "  PostgreSQL is ready."
        break
    fi
    sleep 2
done
echo ""

# Build backend
echo -e "${YELLOW}[3/6] Building Spring Boot API...${NC}"
mvn clean install -DskipTests -pl !apps/cms -q
echo "  Build complete."
echo ""

# Build frontend
echo -e "${YELLOW}[4/6] Building Angular portals...${NC}"
npm ci
npx nx run-many --target=build --all --parallel=4
echo "  Build complete."
echo ""

# Run database migrations
echo -e "${YELLOW}[5/6] Running database migrations...${NC}"
mvn flyway:migrate -q
echo "  Migrations complete."
echo ""

# Initialize MinIO buckets
echo -e "${YELLOW}[6/6] Initializing MinIO buckets...${NC}"
docker compose -f infra/docker/docker-compose.yml exec -T minio sh /usr/local/bin/init-buckets.sh 2>/dev/null || echo "  MinIO init skipped (not ready yet)"
echo ""

echo -e "${GREEN}=== Bootstrap Complete ===${NC}"
echo ""
echo "Services:"
echo "  API:        http://localhost:8080"
echo "  CMS Admin:  http://localhost:1337/admin"
echo "  Public:     http://localhost:4200"
echo "  Applicant:  http://localhost:4300"
echo "  Admin:      http://localhost:4400"
echo ""
echo "To start in development mode:"
echo "  Backend:  mvn spring-boot:run"
echo "  Frontend: npx nx run-many --target=serve --all"
