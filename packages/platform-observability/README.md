# Platform Observability

Centralized logging, metrics, and tracing for AVGC-XR Portal.

## Features

- Structured JSON logging (Logback + SLF4J)
- Micrometer metrics export to Prometheus
- Distributed tracing with correlation IDs
- MDC-based request context propagation
- Tamil Nadu government audit log format compliance

## Auto-Configuration

`ObservabilityAutoConfiguration` is loaded via `spring.factories` and configures:
- Structured JSON logger
- Micrometer registry
- Request correlation filter
- MDC cleanup interceptor

## Usage

All bounded contexts automatically get structured logging. Use `@Slf4j` from Lombok.

```java
@Slf4j
@Service
public class SchemeService {
    public void createScheme(CreateSchemeCommand cmd) {
        log.info("Creating scheme: name={}, category={}", cmd.getName(), cmd.getCategory());
    }
}
```
