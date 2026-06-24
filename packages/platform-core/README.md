# Platform Core

Shared domain primitives and value objects for AVGC-XR Portal.

## Contents

- **Value Objects**: Email, PhoneNumber, AadhaarNumber, PAN, TamilText
- **Domain Exceptions**: DomainException, NotFoundError, ConflictError, ValidationError
- **Base Types**: BaseEntity, BaseAggregateRoot, DomainEvent
- **Constants**: Application-wide constants and enumerations

## Usage

```java
Email email = new Email("user@example.com");
AadhaarNumber aadhaar = new AadhaarNumber("123456789012");
TamilText description = new TamilText("தமிழ்நாடு அரசு திட்டம்");
```
