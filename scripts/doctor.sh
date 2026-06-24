#!/bin/bash
# AVGC-XR Portal - Environment Doctor
# Diagnoses common development environment issues
set -euo pipefail

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

ISSUES=0

check_cmd() {
    local name="$1" cmd="$2" min_version="$3"
    if ! command -v "$cmd" >/dev/null 2>&1; then
        echo -e "${RED}[MISSING]${NC} $name ($cmd) not found"
        ISSUES=$((ISSUES + 1))
        return
    fi
    local version
    version=$($cmd --version 2>&1 | head -1 || echo "unknown")
    echo -e "${GREEN}[OK]${NC} $name: $version"
}

check_port() {
    local name="$1" port="$2"
    if nc -z localhost "$port" 2>/dev/null; then
        echo -e "${GREEN}[OK]${NC} $name is running on port $port"
    else
        echo -e "${YELLOW}[WARN]${NC} $name is not running on port $port"
    fi
}

echo "=== AVGC-XR Portal Environment Doctor ==="
echo ""

echo "--- Prerequisites ---"
check_cmd "Java 21" "java" "21"
check_cmd "Maven" "mvn" "3.9"
check_cmd "Node.js" "node" "20"
check_cmd "npm" "npm" "10"
check_cmd "Docker" "docker" "24"
check_cmd "Docker Compose" "docker compose" "2"
check_cmd "Git" "git" "2"

echo ""
echo "--- Infrastructure Services ---"
check_port "PostgreSQL" 5432
check_port "Redis" 6379
check_port "RabbitMQ" 5672
check_port "Elasticsearch" 9200
check_port "MinIO" 9000

echo ""
if [ "$ISSUES" -eq 0 ]; then
    echo -e "${GREEN}Your environment is ready!${NC}"
else
    echo -e "${RED}Found $ISSUES issue(s). Please fix before proceeding.${NC}"
    exit 1
fi
