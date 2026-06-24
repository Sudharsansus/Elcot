# API Documentation

## OpenAPI Specification

The AVGC-XR Portal API is documented via OpenAPI 3.0 specifications.

## Access

- **Swagger UI**: `http://localhost:8080/api/docs` (dev)
- **OpenAPI JSON**: `http://localhost:8080/api/docs/api-docs`

## API Versioning

All APIs are versioned under `/api/v1/`. Breaking changes will increment the version.

## Authentication

All protected endpoints require a JWT Bearer token in the `Authorization` header.

## Rate Limits

| Endpoint Type | Rate Limit |
|--------------|------------|
| Public read | 100 req/s |
| API general | 30 req/s |
| Authentication | 5 req/min |
| File upload | 2 req/min |
| Form submit | 3 req/min |

## Error Format

All errors follow a consistent JSON structure:
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/applications",
  "timestamp": "2026-06-23T10:30:00+05:30",
  "traceId": "abc-123"
}
```

Bilingual error messages are provided in both English and Tamil.