# Platform Security

Security infrastructure for AVGC-XR Portal.

## Features

- JWT token generation and validation
- Role-based access control (RBAC)
- DPDP Act 2023 compliant data handling
- Password hashing with BCrypt
- Session management via Redis
- Government audit logging

## Security Roles

| Role | Description |
|------|-------------|
| ROLE_SUPER_ADMIN | ELCOT system administrator |
| ROLE_SCHEME_ADMIN | Scheme management authority |
| ROLE_REVIEWER | Application reviewer |
| ROLE_APPLICANT | Registered applicant |
| ROLE_PUBLIC | Unauthenticated public user |

## Configuration

Security is auto-configured via Spring Security filters.
Configure roles and endpoints in application-{env}.yml.
