# Docker Compose - Local Development Stack

## Services

| Service | Image | Port | Description |
|---------|-------|------|-------------|
| postgres | postgres:16-alpine | 5432 | PostgreSQL 16 database |
| redis | redis:7-alpine | 6379 | Redis session cache |
| rabbitmq | rabbitmq:3-management-alpine | 5672/15672 | Message broker |
| elasticsearch | elasticsearch:8.11.0 | 9200 | Search engine |
| minio | minio/minio:latest | 9000/9001 | S3-compatible object storage |
| api | (built from Dockerfile) | 8080 | Spring Boot application |
| cms | strapi/strapi:4 | 1337 | Strapi headless CMS |

## Quick Start

```bash
docker compose -f docker-compose.yml up -d
```

## Volumes

Data is persisted in named Docker volumes:
- `avgcxr_pgdata` - PostgreSQL data
- `avgcxr_redis_data` - Redis persistence
- `avgcxr_es_data` - Elasticsearch indices
- `avgcxr_minio_data` - Object storage
- `avgcxr_rabbitmq_data` - RabbitMQ messages
