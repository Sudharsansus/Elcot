# AVGC-XR Async Worker

RabbitMQ consumer service for async processing. Handles:
- Application submission queue processing
- Audit log writes
- Elasticsearch search indexing
- Notification dispatch (email, SMS, in-app)

## Architecture

Consumes messages from RabbitMQ queues and processes them asynchronously to keep the API responsive during traffic spikes (scheme last date: 15,000-20,000 concurrent users).

## Configuration

See `src/main/resources/application.yml` for full configuration.
