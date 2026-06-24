\
# Test Strategy

## Test Pyramid

| Level | Scope | Tools | Coverage Target |
|-------|-------|-------|----------------|
| Unit | Individual classes/methods | JUnit 5, Mockito, Jest | 80%+ line coverage |
| Integration | Bounded context + DB | Testcontainers, SpringBootTest | Critical paths |
| Contract | API contract verification | Spring Cloud Contract, Pact | All public APIs |
| E2E | Full user flows | Cypress, Playwright | Critical user journeys |
| Accessibility | WCAG 2.1 AA | Axe, Lighthouse, NVDA | 0 violations |
| Load | Performance under load | k6 | p95 < 2s at 500 rps |
| Security | Vulnerability scanning | OWASP ZAP, Trivy, SAST | 0 HIGH/CRITICAL |

## Running Tests

```bash
# All unit tests
mvn test
npm run test:all

# Integration tests (requires Docker)
mvn verify

# E2E tests
npx nx run e2e:average --project=public-portal

# Accessibility audit
npx nx run accessibility:average --project=public-portal

# Load test (2 minutes, 50 users)
./scripts/load-test.sh 2m 50

# Security scan
./scripts/security-scan.sh
```

## Test Data

Test data generators are provided in `libs/testing/` (Angular) and `packages/platform-testing/` (Java).
These generate realistic Tamil Nadu data (districts, pincodes, names) for testing.
