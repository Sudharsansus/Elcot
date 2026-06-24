#!/bin/bash
# AVGC-XR Portal - Load Test Script
# Uses k6 for HTTP load testing
set -euo pipefail

DURATION="${1:-2m}"
VUS="${2:-50}"
BASE_URL="${3:-http://localhost:8080}"

echo "=== AVGC-XR Portal Load Test ==="
echo "Duration: $DURATION, Virtual Users: $VUS, Target: $BASE_URL"
echo ""

if ! command -v k6 >/dev/null 2>&1; then
    echo "Installing k6..."
    sudo apt-get install -y k6 2>/dev/null || {
        echo "ERROR: k6 not available. Install from https://k6.io/docs/getting-started/installation/"
        exit 1
    }
fi

cat > /tmp/avgcxr-load-test.js << 'EOF'
import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const errorRate = new Rate('errors');
const apiLatency = new Trend('api_latency');

export const options = {
  thresholds: {
    errors: ['rate<0.05'],
    api_latency: ['p(95)<2000'],
  },
};

const BASE = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  // Health check
  const health = http.get(`${BASE}/actuator/health`);
  check(health, { 'health ok': (r) => r.status === 200 });

  // Public scheme list
  const schemes = http.get(`${BASE}/api/v1/schemes?page=0&size=20`);
  check(schemes, { 'schemes ok': (r) => r.status === 200 });
  apiLatency.add(schemes.timings.duration);
  errorRate.add(schemes.status !== 200);

  sleep(1);
}
EOF

k6 run -e BASE_URL="$BASE_URL" -d "$DURATION" -u "$VUS" /tmp/avgcxr-load-test.js

echo ""
echo "Load test complete."
