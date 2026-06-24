\
# Runbook: API Down

## Symptoms
- API health endpoint returns 502/503/504
- Angular portals show "Service Unavailable" error
- Error rate > 5% in monitoring dashboard

## Impact
All portal functionality is unavailable. Users cannot browse schemes, submit applications, or log in.

## Diagnosis

```bash
# Check pod status
kubectl get pods -n avgcxr-prod -l app=avgcxr-api

# Check recent logs
kubectl logs -l app=avgcxr-api -n avgcxr-prod --tail=100

# Check events
kubectl describe pod -l app=avgcxr-api -n avgcxr-prod

# Check resource usage
kubectl top pods -n avgcxr-prod -l app=avgcxr-api
```

## Mitigation

### OOMKilled
```bash
# Increase memory limit temporarily
kubectl set resources deployment avgcxr-api -n avgcxr-prod \
  --limits=memory=6Gi --requests=memory=2Gi

# Rollout restart
kubectl rollout restart deployment avgcxr-api -n avgcxr-prod
```

### CrashLoopBackOff
```bash
# Check crash reason
kubectl logs -l app=avgcxr-api -n avgcxr-prod --previous

# If config error: rollback
kubectl rollout undo deployment avgcxr-api -n avgcxr-prod
```

### Database connection failure
```bash
# Check PgBouncer
kubectl logs -l app=avgcxr-pgbouncer -n avgcxr-prod --tail=50

# Restart PgBouncer
kubectl rollout restart deployment avgcxr-pgbouncer -n avgcxr-prod
```

## Resolution
- Fix root cause identified in diagnosis
- Deploy fix through normal deployment pipeline
- Add regression test

## Prevention
- Set up OOMKilled alerting (Alertmanager rule)
- Implement circuit breaker for database connections
- Add startup probe with longer initial delay
