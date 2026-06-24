\
# Security Tests

Automated security testing to identify vulnerabilities.

## Tools

| Tool | Type | Schedule |
|------|------|----------|
| OWASP Dependency Check | SCA (Software Composition Analysis) | Every build |
| Trivy | Container scanning | Every build |
| npm audit | SCA (frontend) | Every build |
| OWASP ZAP | DAST (Dynamic Application Security Testing) | Weekly |
| SpotBugs + FindSecBugs | SAST (Static Analysis) | Every build |

## Running

```bash
# All security scans
./scripts/security-scan.sh

# OWASP Dependency Check (Java)
mvn org.owasp:dependency-check-maven:check

# npm audit (Angular)
npm audit --audit-level=high

# Trivy container scan
trivy image --severity HIGH,CRITICAL elcot/avgcxr-api:latest

# ZAP baseline scan
zap-cli quick-scan --self-contained http://localhost:8080
```

## Policies

- **Build fails** on HIGH or CRITICAL CVEs in dependencies
- ZAP scan must have 0 HIGH findings for production deployment
- All secrets must be rotated if found in logs or commits
