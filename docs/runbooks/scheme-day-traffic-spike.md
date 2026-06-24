\
# Runbook: Scheme Day Traffic Spike

## Symptoms
- API response times increase (p95 > 2 seconds)
- Error rates rise (5xx > 1%)
- CPU/memory usage on API pods > 80%
- PgBouncer connection pool near capacity

## Impact
Slower portal experience. Users may see timeout errors during peak traffic.

## Pre-emptive Actions (Before Deadline)

```bash
# Pre-scale API to 8 replicas
kubectl scale deployment avgcxr-api --replicas=8 -n avgcxr-prod

# Verify HPA is enabled and configured
kubectl get hpa avgcxr-api-hpa -n avgcxr-prod

# Check PgBouncer pool capacity
kubectl exec -it <pgbouncer-pod> -- psql -h localhost -p 6432 -U avgcxr_admin -c "SHOW POOLS;"

# Increase PgBouncer pool size temporarily
kubectl set env deployment/avgcxr-pgbouncer DEFAULT_POOL_SIZE=50 -n avgcxr-prod
kubectl rollout restart deployment avgcxr-pgbouncer -n avgcxr-prod

# Warm up Redis cache
curl -s http://avgcxr-api:8080/actuator/cache/warmup -X POST

# Verify Elasticsearch is healthy
curl -s http://avgcxr-elasticsearch:9200/_cluster/health
```

## During Spike

```bash
# Monitor in real-time
watch -n 5 "kubectl top pods -n avgcxr-prod -l app=avgcxr-api"

# Check HPA status
kubectl get hpa avgcxr-api-hpa -n avgcxr-prod -w

# If HPA max reached, manually increase
kubectl scale deployment avgcxr-api --replicas=15 -n avgcxr-prod
```

## Post-Spike

```bash
# Scale back to normal
kubectl scale deployment avgcxr-api --replicas=3 -n avgcxr-prod

# Reset PgBouncer pool size
kubectl set env deployment/avgcxr-pgbouncer DEFAULT_POOL_SIZE=25 -n avgcxr-prod
kubectl rollout restart deployment avgcxr-pgbouncer -n avgcxr-prod

# Review performance metrics
# Generate spike report for capacity planning
```

## Prevention
- Implement predictive scaling based on historical traffic data
- Add CDN (CloudFront) for static assets to reduce API load
- Consider read replicas for heavy read operations during spike
