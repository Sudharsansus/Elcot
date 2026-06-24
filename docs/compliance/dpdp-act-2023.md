\
# DPDP Act 2023 Compliance

## Digital Personal Data Protection Act, 2023

The AVGC-XR Portal handles personal data of Tamil Nadu citizens (applicants) and must comply with the Digital Personal Data Protection Act, 2023.

## Data Fiduciary Obligations

### 1. Lawful Processing (Section 4)

All personal data is collected only for specified, lawful purposes related to scheme application processing. The purposes are clearly stated in the application form and privacy policy.

### 2. Consent Management (Section 6)

- **Explicit consent** is obtained before collecting personal data via a consent checkbox on the registration form.
- Consent is recorded with timestamp and IP address in the `user_consent` table.
- Users can withdraw consent via the Applicant Portal profile settings.
- Consent withdrawal triggers data anonymization (not deletion, due to government audit requirements).

### 3. Data Minimization (Section 5)

Only data fields necessary for the specific scheme application are collected. Sensitive personal data (Aadhaar, PAN) is collected only when required by the scheme eligibility criteria.

### 4. Data Accuracy (Section 8)

Users can view and update their personal data through the Applicant Portal. Data corrections are audit-logged with the previous and new values.

### 5. Data Storage Limitation

Personal data is retained for the duration specified in the Tamil Nadu government records retention schedule (typically 7 years for financial records). Automated cleanup jobs archive expired data.

### 6. Data Security (Section 9)

- Encryption at rest (AES-256 for PostgreSQL, MinIO)
- Encryption in transit (TLS 1.2+)
- Access control via RBAC (role-based access control)
- Audit logging of all data access
- PII fields are encrypted in the database using AES-256-GCM

### 7. Data Breach Notification (Section 8(7))

In case of a personal data breach, ELCOT will be notified within 72 hours. The incident response procedure is documented in `docs/security/incident-response.md`.

## Data Categories

| Category | Examples | Sensitivity | Retention |
|----------|---------|-------------|-----------|
| Identity | Name, DOB, gender, photo | High | 7 years after application closure |
| Contact | Email, phone, address | Medium | 7 years after application closure |
| Government ID | Aadhaar (masked), PAN | Very High | 7 years after application closure |
| Financial | Bank details, ITR, turnover | Very High | 7 years after application closure |
| Application | Scheme selections, documents | High | 7 years after application closure |
| Technical | Login history, session data | Low | 1 year |

## Grievance Officer

As per DPDP Act requirements, the Data Protection Officer contact details are published on the portal footer and privacy policy page.
