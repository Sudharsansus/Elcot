\
# ISO 27001 Information Security Controls

## Applicable Controls

The AVGC-XR Portal implements controls aligned with ISO/IEC 27001:2022 Annex A. The following controls are applicable to this system.

## A.5 Organizational Controls

| Control | Description | Implementation |
|---------|-------------|----------------|
| A.5.1 | Policies for information security | Security policy document approved by ELCOT CISO |
| A.5.7 | Threat intelligence | Monitoring via Spring Boot Actuator + Prometheus |
| A.5.8 | Information security in project management | Security included in sprint definition of done |
| A.5.10 | Acceptable use of information assets | Acceptable use policy in employee onboarding |
| A.5.14 | Information transfer | TLS 1.2+ for all data in transit |
| A.5.15 | Access control | RBAC with 5 roles (SUPER_ADMIN, SCHEME_ADMIN, REVIEWER, APPLICANT, PUBLIC) |
| A.5.33 | Protection of records | Audit trail with tamper-evident logging |
| A.5.35 | Independent review | Quarterly security audits by ELCOT security team |

## A.6 People Controls

| Control | Description | Implementation |
|---------|-------------|----------------|
| A.6.1 | Screening | Background verification for all developers with access |
| A.6.3 | Information security awareness | Quarterly security training for all team members |
| A.6.4 | Disciplinary process | Security incident escalation procedure defined |

## A.7 Physical Controls

| Control | Description | Implementation |
|---------|-------------|----------------|
| A.7.1 | Physical security perimeters | Data center access controlled by ELCOT/NIC facility security |
| A.7.9 | Security of assets off-premises | Laptop encryption, VPN for remote access |

## A.8 Technological Controls

| Control | Description | Implementation |
|---------|-------------|----------------|
| A.8.2 | Privileged access rights | Separate admin credentials, least-privilege principle |
| A.8.5 | Secure authentication | JWT + refresh tokens, BCrypt password hashing |
| A.8.8 | Management of technical vulnerabilities | OWASP Dependency Check in CI, Trivy container scanning |
| A.8.9 | Configuration management | Infrastructure-as-code (Terraform + K8s), no manual config |
| A.8.10 | Information deletion | DPDP-compliant data retention and anonymization |
| A.8.11 | Data masking | Aadhaar displayed as XXXX-XXXX-1234 in UI |
| A.8.12 | Data leakage prevention | DLP rules on email/SMS to prevent PII exposure |
| A.8.20 | Networks security | VPC isolation, security groups, WAF |
| A.8.23 | Web filtering | Nginx security headers, CSP, rate limiting |
| A.8.24 | Use of cryptography | AES-256 at rest, TLS 1.2+ in transit |
| A.8.25 | Secure development lifecycle | Secure coding guidelines, SAST in CI pipeline |
| A.8.26 | Application security requirements | OWASP Top 10 addressed in design |
| A.8.28 | Secure coding | Secure coding guidelines in docs/security/secure-coding.md |
