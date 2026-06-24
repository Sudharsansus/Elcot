# Security Policy

## Supported Versions

| Version | Supported |
|---------|-----------|
| 1.0.x | ✅ Active development |
| < 1.0 | ❌ Not supported |

## Reporting a Vulnerability

If you discover a security vulnerability in the AVGC-XR Portal:

1. **DO NOT** open a public GitHub issue
2. Email: security@elcot.tn.gov.in with:
   - Description of the vulnerability
   - Steps to reproduce
   - Potential impact
   - Suggested fix (if any)
3. ELCOT security team will acknowledge within 48 hours
4. A fix will be coordinated and a security advisory published

## Security Measures

- VAPT by TNeGA empanelled agency (pre-launch and annually)
- OWASP Top 10 compliance
- ISO 27001 aligned practices
- HTTPS-only, HSTS enabled
- JWT with short expiry + refresh token rotation
- RBAC with principle of least privilege
- Audit logging for all sensitive operations
- Rate limiting on all endpoints
- CSP headers to prevent XSS
- DPDP Act 2023 compliance for personal data
