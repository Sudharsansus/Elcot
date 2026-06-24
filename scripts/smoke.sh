#!/bin/bash
# AVGC-XR Portal - Smoke Test Script
# Verifies all services are up and responding
set -euo pipefail

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

PASSED=0
FAILED=0

check_endpoint() {
    local name="$1" url="$2" expected_code="${3:-200}"
    local http_code
    http_code=$(curl -s -o /dev/null -w "%{http_code}" "$url" 2>/dev/null || echo "000")

    if [ "$http_code" = "$expected_code" ]; then
        echo -e "${GREEN}[PASS]${NC} $name ($url) -> $http_code"
        PASSED=$((PASSED + 1))
    else
        echo -e "${RED}[FAIL]${NC} $name ($url) -> $http_code (expected $expected_code)"
        FAILED=$((FAILED + 1))
    fi
}

echo "=== AVGC-XR Portal Smoke Tests ==="
echo ""

check_endpoint "API Health" "http://localhost:8080/actuator/health" "200"
check_endpoint "API OpenAPI" "http://localhost:8080/api/docs" "200"
check_endpoint "Public Portal" "http://localhost:4200" "200"
check_endpoint "CMS Health" "http://localhost:1337/_health" "200"
check_endpoint "PostgreSQL" "http://localhost:8080/actuator/health/db" "200"
check_endpoint "Redis" "http://localhost:8080/actuator/health/redis" "200"
check_endpoint "RabbitMQ" "http://localhost:8080/actuator/health/rabbitmq" "200"
check_endpoint "Elasticsearch" "http://localhost:9200/_cluster/health" "200"

echo ""
echo "Results: $PASSED passed, $FAILED failed"

if [ "$FAILED" -gt 0 ]; then
    exit 1
fi
