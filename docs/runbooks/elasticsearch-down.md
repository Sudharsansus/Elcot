\
# Runbook: Elasticsearch Down

## Symptoms
- Search returns empty results or errors
- Dashboard charts not loading data
- API logs show connection refused to Elasticsearch

## Impact
Search functionality unavailable. Portal remains functional for all other operations. Users can still browse and submit applications.

## Diagnosis

```bash
# Check ES pod
kubectl get pods -n avgcxr-prod -l app=avgcxr-elasticsearch

# Check ES logs
kubectl logs -l app=avgcxr-elasticsearch -n avgcxr-prod --tail=100

# Check cluster health
curl -s http://avgcxr-elasticsearch:9200/_cluster/health?pretty

# Check index status
curl -s http://avgcxr-elasticsearch:9200/_cat/indices?v
```

## Mitigation

### ES OOM
```bash
# Check JVM heap
kubectl exec -it <es-pod> -- curl -s localhost:9200/_nodes/stats/jvm?pretty | grep heap_used_percent

# Increase memory limit
kubectl set resources deployment avgcxr-elasticsearch -n avgcxr-prod \
  --limits=memory=16Gi --requests=memory=4Gi

# Restart
kubectl rollout restart deployment avgcxr-elasticsearch -n avgcxr-prod
```

### Index Corruption
```bash
# Reindex from PostgreSQL
# Trigger full reindex via API admin endpoint
curl -X POST http://avgcxr-api:8080/api/v1/admin/search/reindex -H "Authorization: Bearer <admin-token>"
```

## Prevention
- Monitor ES heap usage (alert at 85%)
- Configure index lifecycle management (ILM)
- Regular index snapshots to S3
