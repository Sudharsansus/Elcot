# System Architecture

## Overview

The AVGC-XR Portal follows a **modular monolith** architecture with hexagonal design within each bounded context. The system is built for the Tamil Nadu government's AVGC-XR initiative under ELCOT.

## Architecture Diagram

```mermaid
graph TB
    subgraph "Angular Portals (Nx Monorepo)"
        PUB[Public Portal :4200]
        APP[Applicant Portal :4300]
        ADM[Admin Portal :4400]
    end

    subgraph "Shared Nx Libraries"
        UI[UI Kit]
        DS[Design System]
        DA[Data Access]
        AUTH[Auth]
        I18N[i18n]
        API[API Contracts]
        UTIL[Utilities]
        TEST[Testing]
    end

    subgraph "API Gateway (Nginx)"
        NX[nginx :443]
    end

    subgraph "Spring Boot Modular Monolith :8080"
        subgraph "Bounded Contexts"
            SCHEME[Scheme Management]
            APPMGR[Application Management]
            WORKFLOW[Workflow Engine]
            USER[User Management]
            AUTHSVC[Auth Service]
            PAYMENT[Payment]
            NOTIF[Notification]
            REPORT[Reporting]
            SEARCH[Search]
            DOC[Document Management]
            COMPLIANCE[Compliance]
            DASHBOARD[Dashboard]
            CMSB[Content Bridge]
            AUDIT[Audit]
            COMM[Communication]
            CONFIG[Configuration]
            MEDIA[Media]
        end
        subgraph "Platform Packages"
            CORE[Platform Core]
            PERSIST[Persistence]
            SEC[Security]
            EVENTS[Events]
            OBS[Observability]
            SEARCHC[Search Client]
            NOTIFC[Notification Client]
            CMSC[CMS Client]
            CODEGEN[Code Generation]
            TESTING[Testing]
        end
    end

    subgraph "Infrastructure"
        PG[(PostgreSQL 16)]
        REDIS[(Redis 7)]
        RMQ[RabbitMQ 3]
        ES[(Elasticsearch 8)]
        MINIO[(MinIO S3)]
        STRAPI[Strapi CMS]
    end

    PUB --> NX --> API
    APP --> NX
    ADM --> NX
    NX --> SPRING
    SPRING --> PG
    SPRING --> REDIS
    SPRING --> RMQ
    SPRING --> ES
    SPRING --> MINIO
    SPRING --> STRAPI
    STRAPI --> PG
    STRAPI --> MINIO
```

## Key Decisions

See `adr/` directory for Architecture Decision Records.