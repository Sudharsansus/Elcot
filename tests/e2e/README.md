\
# End-to-End Tests

E2E tests verify complete user journeys across the AVGC-XR Portal.

## Framework

Using Playwright for E2E testing with TypeScript.

## Critical User Journeys

### Public Portal
1. Browse scheme list
2. Search schemes with Tamil text
3. View scheme detail (bilingual toggle)
4. Register new account
5. Login with OTP

### Applicant Portal
1. View dashboard
2. Start new application
3. Upload documents
4. Submit application
5. View application status
6. Download submitted documents

### Admin Portal
1. Login as admin
2. View application queue
3. Review application
4. Approve/reject with comments
5. View reports dashboard

## Running

```bash
# All E2E tests
npx nx run e2e --project=public-portal

# Specific test file
npx nx run e2e --project=applicant-portal --spec=tests/e2e/applicant/application-flow.spec.ts

# With UI (debugging)
npx nx run e2e:debug --project=public-portal
```

## Test Data

Use `libs/testing` MockApiService for seeded test data. Each test creates its own data and cleans up after.
