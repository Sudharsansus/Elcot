# Platform Testing

Shared test utilities and base classes for AVGC-XR Portal.

## Features

- Base integration test class with Testcontainers (PostgreSQL, Redis, RabbitMQ)
- Test data builders (scheme, application, user)
- API test helpers with JWT token generation
- Bilingual assertion helpers
- WireMock stubs for external services

## Usage

```java
class SchemeServiceTest extends AbstractIntegrationTest {

    @Test
    void shouldCreateScheme() {
        // Testcontainers handles PostgreSQL setup automatically
    }
}
```
