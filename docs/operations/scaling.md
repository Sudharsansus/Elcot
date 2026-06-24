\
# Scaling Guide

## Current Sizing

| Component | Dev | Staging | Prod |
|-----------|-----|---------|------|
| API replicas | 1 | 2 | 3 (HPA up to 10) |
| API memory per pod | 512Mi | 2Gi | 4Gi |
| PostgreSQL | 1 node | 1 node | 2 nodes (Aurora) |
| Redis | 256MB | 256MB | 1GB |
| Elasticsearch | 2GB | 4GB | 16GB |
| RabbitMQ | t3.micro | mq.r6g.large | mq.r6g.xlarge |

## Horizontal Pod Autoscaler (Production)

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: avgcxr-api-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: avgcxr-api
  minReplicas: 3
  maxReplicas: 10
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
    - type: Resource
      resource:
        name: memory
        target:
          type: Utilization
          averageUtilization: 80
  behavior:
    scaleUp:
      stabilizationWindowSeconds: 60
      policies:
        - type: Percent
          value: 50
          periodSeconds: 60
    scaleDown:
      stabilizationWindowSeconds: 300
      policies:
        - type: Percent
          value: 10
          periodSeconds: 60
```

## Traffic Spike Handling

During scheme application deadline days, traffic can spike 10x. Pre-scaling is done 24 hours before known deadlines:

```bash
# Pre-scale API to 8 replicas before scheme deadline
kubectl scale deployment avgcxr-api --replicas=8 -n avgcxr-prod
```

See [Scheme Day Traffic Spike Runbook](../runbooks/scheme-day-traffic-spike.md).

## Database Scaling

- Read replicas for reporting queries (Aurora reader endpoint)
- PgBouncer connection pooling (1000 client connections, 35 PG connections)
- Query optimization via `pg_stat_statements` monitoring
- Index tuning based on slow query analysis
