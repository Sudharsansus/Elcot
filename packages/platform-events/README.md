# Platform Events

Domain event infrastructure for AVGC-XR Portal using RabbitMQ.

## Features

- Domain event publishing with Spring AMQP
- Event routing via topic exchanges
- Dead letter queue handling
- Event serialization/deserialization
- Bounded context event isolation via routing keys

## Event Routing Convention

Events use routing key pattern: `{domain}.{entity}.{action}`
Examples:
- `scheme.application.submitted`
- `notification.email.sent`
- `search.index.updated`

## Usage

```java
@Autowired
private RabbitTemplate rabbitTemplate;

public void publishApplicationSubmitted(ApplicationSubmittedEvent event) {
    rabbitTemplate.convertAndSend("avgcxr.events", "scheme.application.submitted", event);
}
```
