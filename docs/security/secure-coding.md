\
# Secure Coding Guidelines

## General Principles

1. **Never trust user input** — validate and sanitize all inputs on the server side.
2. **Defense in depth** — multiple layers of security controls.
3. **Least privilege** — components should only have the minimum permissions needed.
4. **Fail securely** — default deny; exceptions should not expose system details.

## OWASP Top 10 Mitigations

### A01: Broken Access Control
- Every API endpoint verifies JWT roles server-side.
- Resource-level authorization checks (users can only access their own data).
- `@HasRole` annotation on all non-public controller methods.

### A02: Cryptographic Failures
- AES-256-GCM for sensitive data at rest.
- BCrypt (strength 12) for password hashing.
- TLS 1.2+ enforced; TLS 1.0/1.1 disabled.
- Secrets stored in AWS Secrets Manager, never in code or config files.

### A03: Injection
- JPA/Hibernate with parameterized queries (no string concatenation for SQL).
- Input validation with Bean Validation annotations.
- Output encoding for HTML (Angular's built-in XSS protection).

### A04: Insecure Design
- Threat modeling performed per bounded context.
- Security requirements in sprint acceptance criteria.
- Abuse case testing in QA.

### A05: Security Misconfiguration
- Default credentials changed in all environments.
- Unnecessary endpoints disabled (Spring Boot actuator restricted).
- Security headers configured in Nginx (CSP, HSTS, X-Frame-Options).

### A06: Vulnerable Components
- OWASP Dependency Check in CI pipeline (fail on CVSS 7+).
- npm audit in CI pipeline (fail on high/critical).
- Dependabot enabled for automatic PR updates.

### A07: Authentication Failures
- Account lockout after 5 failed login attempts (15-minute lockout).
- Secure password policy (min 8 chars, mixed case, number, special char).
- MFA via OTP for admin roles.

### A08: Software and Data Integrity Failures
- Maven Central and npm registry checksum verification.
- SBOM (Software Bill of Materials) generated per build.
- Git commit signing enforced.

### A09: Security Logging Failures
- All authentication events logged (success and failure).
- Audit trail for all data mutations.
- Structured JSON logs for SIEM integration.

### A10: Server-Side Request Forgery
- URL allowlist for outbound HTTP requests.
- No user-controlled URLs passed to server-side HTTP clients.
