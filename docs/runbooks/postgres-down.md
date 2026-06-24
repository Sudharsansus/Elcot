\
# Runbook: PostgreSQL Down

## Symptoms
- API returns 500 with "Connection refused" or "Connection timed out"
- Flyway migrations fail
- PgBouncer shows no available server connections

## Impact
All data-dependent operations fail. Users cannot submit applications, log in, or view data.

## Diagnosis

```bash
# Check PostgreSQL pod
kubectl get pods -n avgcxr-prod -l app=avgcxr-postgres

# Check PostgreSQL logs
kubectl logs -l app=avgcxr-postgres -n avgcxr-prod --tail=100

# Check if PostgreSQL is running inside the pod
kubectl exec -it <postgres-pod> -n avgcxr-prod -- pg_isready

# Check disk space
kubectl exec -it <postgres-pod> -n avgcxr-prod -- df -h /var/lib/postgresql/data

# Check replication status (if Aurora)
aws rds describe-db-clusters --db-cluster-identifier avgcxr-prod
```

## Mitigation

### Aurora Failover (Automatic)
Aurora automatically fails over to the reader node. Verify:

```bash
# Check new writer
aws rds describe-db-clusters --db-cluster-identifier avgcxr-prod \
  --query 'DBClusters[0].DBClusterMembers'

# Update PgBouncer target if using direct connections
kubectl rollout restart deployment avgcxr-pgbouncer -n avgcxr-prod
```

### Disk Full
```bash
# Identify large tables
kubectl exec -it <postgres-pod> -- psql -U avgcxr_app -d avgcxr_portal -c "
  SELECT relname, pg_size_pretty(pg_total_relation_size(relid))
  FROM pg_stat_user_tables ORDER BY pg_total_relation_size(relid) DESC LIMIT 10;
"

# Vacuum large tables
kubectl exec -it <postgres-pod> -- psql -U avgcxr_app -d avgcxr_portal -c "VACUUM ANALYZE;"
```

### Connection Exhaustion
```bash
# Check active connections
kubectl exec -it <postgres-pod> -- psql -U avgcxr_app -d avgcxr_portal -c "
  SELECT count(*) FROM pg_stat_activity WHERE state = 'active';
"

# Terminate idle connections
kubectl exec -it <postgres-pod> -- psql -U avgcxr_app -d avgcxr_portal -c "
  SELECT pg_terminate_backend(pid) FROM pg_stat_activity
  WHERE state = 'idle' AND query_start < NOW() - INTERVAL '10 minutes';
"
```

## Prevention
- Set up disk usage alerting at 80% threshold
- Configure autovacuum aggressively for high-write tables
- Use PgBouncer to prevent connection exhaustion
