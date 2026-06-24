\
# Runbook: Security Incident

## Severity Levels

| Level | Description | Response Time |
|-------|-------------|---------------|
| P1 Critical | Active breach, data exfiltration | Immediate |
| P2 High | Vulnerability exploited, unauthorized access | < 30 minutes |
| P3 Medium | Suspicious activity, potential vulnerability | < 2 hours |
| P4 Low | Failed attack attempt, no impact | < 24 hours |

## Immediate Actions (P1/P2)

### 1. Contain
```bash
# Block suspicious IPs via Nginx rate limiting or WAF
# If database breach suspected: revoke all active sessions
kubectl rollout restart deployment avgcxr-api -n avgcxr-prod

# If credentials compromised: rotate all secrets
aws secretsmanager update-secret --secret-id avgcxr-prod/database --secret-string "$(aws secretsmanager get-secret-value --secret-id avgcxr-prod/database --query SecretString --output text | jq '.password = "NEW_RANDOM_PASSWORD"')"
```

### 2. Preserve Evidence
```bash
# Collect logs before they rotate
kubectl logs -l app=avgcxr-api -n avgcxr-prod --since=24h > /tmp/api-logs-$(date +%s).txt
kubectl logs -l app=avgcxr-nginx -n avgcxr-prod --since=24h > /tmp/nginx-logs-$(date +%s).txt
```

### 3. Notify
- ELCOT CISO: [security@elcot.tn.gov.in]
- CERT-In: Report within 6 hours (mandatory)
- Affected users: Via email if personal data compromised

### 4. Investigate
- Review audit logs for unauthorized access
- Check database for unauthorized data modifications
- Analyze network traffic for data exfiltration

### 5. Remediate
- Patch vulnerability
- Rotate all compromised credentials
- Deploy fix to all environments

## Post-Incident
- Write incident report within 48 hours
- Conduct blameless post-mortem
- Update threat model
- Add detection rules for similar attacks
