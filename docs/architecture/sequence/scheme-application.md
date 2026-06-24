# Sequence Diagram: Scheme Application Flow

## Overview

This diagram describes the end-to-end flow when an applicant submits a scheme application through the AVGC-XR Portal.

## Participants

- **Applicant**: Authenticated citizen using the Applicant Portal
- **Angular App**: Applicant Portal frontend
- **API Gateway**: Nginx reverse proxy
- **Application API**: Spring Boot application management bounded context
- **Workflow Engine**: Flowable BPMN process engine
- **Notification Service**: RabbitMQ async notification handler
- **Search Service**: Elasticsearch indexing service
- **MinIO**: Object storage for uploaded documents
- **PostgreSQL**: Primary database

## Flow

```
Applicant          Angular App       API Gateway      Application API      Workflow Engine     Notification      Search           MinIO          PostgreSQL
  |                    |                  |                   |                    |                   |               |               |               |
  |  Fill form         |                  |                   |                    |                   |               |               |               |
  |------------------->|                  |                   |                    |                   |               |               |               |
  |                    |  Upload docs     |                   |                    |                   |               |               |               |
  |                    |------------------>|                   |                    |                   |               |               |               |
  |                    |                  |  PUT /documents   |                    |                   |               |               |               |
  |                    |                  |------------------>|                    |                   |               |               |               |
  |                    |                  |                   |  Store file         |                   |               |               |               |
  |                    |                  |                   |-------------------------------------------->|               |               |               |
  |                    |                  |                   |  <--- pre-signed URL ----------------------|               |               |               |
  |                    |  <--- URL        |                   |                    |                   |               |               |               |
  |                    |  Upload to MinIO |                   |                    |                   |               |               |               |
  |                    |--------------------------------------------------------------------------------------------->|
  |                    |  <--- 200 OK     |                   |                    |                   |               |               |               |
  |                    |                  |                   |                    |                   |               |               |               |
  |  Submit            |                  |                   |                    |                   |               |               |               |
  |------------------->|                  |                   |                    |                   |               |               |               |
  |                    |  POST /applications                  |                    |                   |               |               |               |
  |                    |------------------>|                   |                    |                   |               |               |               |
  |                    |                  |  JWT validated     |                    |                   |               |               |               |
  |                    |                  |------------------>|                    |                   |               |               |               |
  |                    |                  |                   |  Validate data      |                   |               |               |               |
  |                    |                  |                   |  Save draft         |                   |               |               |               |
  |                    |                  |                   |------------------------------------------------------------>|               |
  |                    |                  |                   |  <--- application ID ---------------------------------------|               |
  |                    |                  |                   |                    |                   |               |               |               |
  |                    |                  |                   |  Start workflow     |                   |               |               |               |
  |                    |                  |                   |------------------->|                   |               |               |               |
  |                    |                  |                   |  <--- process ID    |                   |               |               |               |
  |                    |                  |                   |                    |                   |               |               |               |
  |                    |                  |                   |  Publish event      |                   |               |               |               |
  |                    |                  |                   |---(RabbitMQ)------->|                   |               |               |               |
  |                    |                  |                   |                    |  Index document   |               |               |               |
  |                    |                  |                   |                    |------------------>|               |               |               |
  |                    |                  |                   |                    |  Send email        |               |               |               |
  |                    |                  |                   |                    |------------------>|               |               |               |
  |                    |                  |                   |                    |                   |               |               |               |
  |  <--- 201 Created (application ID, status: SUBMITTED)   |                    |                   |               |               |               |
  |                    |                  |                   |                    |                   |               |               |               |
```

## Notes

1. Document upload happens before application submission to obtain file references.
2. Application is saved as DRAFT first, then workflow is started.
3. All side effects (indexing, notifications) are asynchronous via RabbitMQ.
4. The applicant receives email + SMS confirmation after successful submission.
5. The workflow engine assigns the first review task based on scheme rules.