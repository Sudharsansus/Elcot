# Platform Notification Client

Multi-channel notification service for AVGC-XR Portal.

## Channels

- **Email**: SMTP-based email delivery with Tamil/English templates
- **SMS**: SMS gateway integration for OTP and status notifications

## Features

- Template-based notifications with bilingual support
- Async delivery via RabbitMQ
- Delivery status tracking
- Retry logic with exponential backoff

## Usage

```java
@Autowired
private NotificationGateway notificationGateway;

notificationGateway.sendEmail(
    new EmailRequest("user@example.com", "SCHEME_SUBMITTED", Map.of("schemeName", "AVGC Startup Fund"))
);
```
