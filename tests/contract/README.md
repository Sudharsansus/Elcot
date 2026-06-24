\
# Contract Tests

Contract tests verify that the Spring Boot API and Angular frontend agree on the same data contracts.

## Approach

Using Spring Cloud Contract for consumer-driven contract testing. The Angular `api-contracts` library defines TypeScript interfaces that must match the API's JSON responses.

## Running

```bash
# Generate contracts from API
mvn generateContracts

# Verify contracts in frontend
npm run test:contracts
```

## Contracts Defined

- `/api/v1/schemes` — Scheme list and detail responses
- `/api/v1/applications` — Application CRUD responses
- `/api/v1/auth/login` — Login and token responses
- `/api/v1/users/me` — Current user profile response
- `/api/v1/workflow/tasks` — Task list responses
