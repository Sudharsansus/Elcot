\
# Threat Model

## Threat Modeling Methodology

Using STRIDE (Spoofing, Tampering, Repudiation, Information Disclosure, Denial of Service, Elevation of Privilege).

## Threat Matrix

| Threat | Category | Severity | Mitigation |
|--------|----------|----------|------------|
| Attacker steals JWT token | Spoofing | High | HttpOnly + Secure cookies, short token TTL (15 min), token rotation |
| SQL injection via search | Tampering | High | Parameterized queries (JPA), input validation, WAF rules |
| Applicant modifies submitted application | Tampering | Medium | Workflow state machine prevents modification after submission |
| Admin action not traceable | Repudiation | Medium | Comprehensive audit logging with user ID, timestamp, action |
| PII exposed in API response | Information Disclosure | Critical | Field-level encryption, data masking in API responses, audit access |
| Aadhaar number leaked | Information Disclosure | Critical | AES-256-GCM encryption at rest, masked display (XXXX-XXXX-1234) |
| DDoS on public endpoints | Denial of Service | High | Nginx rate limiting, CloudFront CDN, AWS Shield |
| Applicant gains admin access | Elevation of Privilege | Critical | JWT role claims verified server-side, RBAC on every endpoint |
| CSRF on form submission | Elevation of Privilege | Medium | SameSite cookies, CSRF tokens, state parameter validation |
| XSS via scheme description | Tampering | Medium | Content Security Policy, input sanitization, output encoding |

## Trust Boundaries

```
Internet (Untrusted)
  ↓ [TLS 1.2+ / Nginx WAF / Rate Limiting]
Nginx Reverse Proxy
  ↓ [Internal Network]
Spring Boot API (Trusted Code)
  ↓ [PgBouncer / TLS]
PostgreSQL (Trusted Storage)
  ↓ [TLS]
Redis / RabbitMQ / Elasticsearch / MinIO (Trusted Infrastructure)
```

## Data Classification

| Classification | Examples | Encryption | Access |
|---------------|---------|------------|--------|
| Public | Scheme descriptions, FAQs | TLS in transit | All users |
| Internal | Application status, workflow state | AES-256 at rest | Authenticated users |
| Confidential | Personal data (name, DOB, address) | AES-256-GCM at rest | Role-based |
| Restricted | Aadhaar, PAN, bank details | AES-256-GCM at rest + field-level | Need-to-know |
