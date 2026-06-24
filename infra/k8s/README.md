# Kubernetes Deployment - AVGC-XR Portal

## Structure

```
infra/k8s/
├── base/              # Base resource definitions (environment-agnostic)
│   ├── api/           # Spring Boot API (Deployment, Service, ConfigMap)
│   ├── cms/           # Strapi CMS
│   ├── postgres/      # PostgreSQL 16
│   ├── redis/         # Redis session cache
│   ├── rabbitmq/      # RabbitMQ message broker
│   ├── elasticsearch/ # Elasticsearch 8
│   ├── minio/         # MinIO object storage
│   └── pgbouncer/     # PgBouncer connection pooler
├── overlays/
│   ├── dev/           # Development (1 replica, lower resources)
│   ├── staging/       # Staging (2 replicas, moderate resources)
│   └── prod/          # Production (3 replicas, full resources, LoadBalancer)
```

## Deployment

```bash
# Deploy to dev
kubectl apply -k infra/k8s/overlays/dev

# Deploy to staging
kubectl apply -k infra/k8s/overlays/staging

# Deploy to production
kubectl apply -k infra/k8s/overlays/prod
```

## Environment Sizing

| Component | Dev | Staging | Prod |
|-----------|-----|---------|------|
| API replicas | 1 | 2 | 3 |
| API memory | 256Mi–1Gi | 512Mi–2Gi | 1Gi–4Gi |
| Postgres memory | 512Mi–2Gi | 1Gi–4Gi | 2Gi–8Gi |
| Elasticsearch memory | 512Mi–2Gi | 1Gi–4Gi | 4Gi–16Gi |
| Redis memory | 128Mi–512Mi | 128Mi–512Mi | 256Mi–1Gi |

## Prerequisites

- Kubernetes 1.28+
- kubectl configured with cluster access
- Namespace `avgcxr` created (or managed via Kustomization)
- PersistentVolumeClaims for PostgreSQL and Elasticsearch (production)
- Secret objects for database passwords, API keys, and TLS certificates
