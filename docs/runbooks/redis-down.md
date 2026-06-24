\
# Runbook: Redis Down

## Symptoms
- Users are logged out unexpectedly
- Session-related errors in API logs
- Rate limiting not functioning

## Impact
Users cannot maintain authenticated sessions. CSRF protection may fail.

## Diagnosis

```bash
# Check Redis pod
kubectl get pods -n avgcxr-prod -l app=avgcxr-redis

# Check Redis logs
kubectl logs -l app=avgcxr-redis -n avgcxr-prod --tail=50

# Test connectivity from API pod
kubectl exec -it <api-pod> -- nc -zv avgcxr-redis 6379

# Check Redis memory
kubectl exec -it <redis-pod> -- redis-cli INFO memory
```

## Mitigation

### Redis OOM
```bash
# Check memory usage
kubectl exec -it <redis-pod> -- redis-cli INFO memory | grep used_memory_human

# Increase maxmemory if needed
kubectl set env deployment/avgcxr-redis -n avgcxr-prod MAXMEMORY=512mb
kubectl rollout restart deployment avgcxr-redis -n avgcxr-prod
```

### Connection Refused
```bash
# Restart Redis
kubectl rollout restart deployment avgcxr-redis -n avgcxr-prod

# Restart API to re-establish connections
kubectl rollout restart deployment avgcxr-api -n avgcxr-prod
```

## Prevention
- Monitor Redis memory usage (alert at 80% of maxmemory)
- Configure AOF persistence for session durability
- Consider Redis Cluster for high availability
