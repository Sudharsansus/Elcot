#!/bin/bash
# AVGC-XR Portal - Security Scan Script
# Runs OWASP dependency check and SAST scanning
set -euo pipefail

echo "=== AVGC-XR Portal Security Scan ==="
echo ""

# OWASP Dependency Check
echo "[1/3] Running OWASP Dependency Check (Java)..."
mvn org.owasp:dependency-check-maven:check -DfailBuildOnCVSS=7 -q || {
    echo "  HIGH/CRITICAL vulnerabilities found in Java dependencies!"
}

# npm audit
echo "[2/3] Running npm audit (Angular)..."
npm audit --audit-level=high || {
    echo "  HIGH/CRITICAL vulnerabilities found in npm packages!"
}

# Trivy filesystem scan
echo "[3/3] Running Trivy filesystem scan..."
if command -v trivy >/dev/null 2>&1; then
    trivy fs --severity HIGH,CRITICAL --exit-code 1 . 2>/dev/null || {
        echo "  Vulnerabilities found by Trivy!"
    }
else
    echo "  Trivy not installed. Skipping filesystem scan."
fi

echo ""
echo "Security scan complete."
