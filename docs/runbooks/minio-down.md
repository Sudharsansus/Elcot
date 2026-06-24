\
# Runbook: MinIO Down

## Symptoms
- File upload returns 500 error
- Document download links return 403/404
- CMS media images not loading

## Impact
Users cannot upload documents or download files. Scheme browsing works but document preview fails.

## Diagnosis

```bash
# Check MinIO pod
kubectl get pods -n avgcxr-prod -l app=avgcxr-minio

# Check MinIO logs
kubectl logs -l app=avgcxr-minio -n avgcxr-prod --tail=100

# Test connectivity
kubectl exec -it <api-pod> -- curl -s http://avgcxr-minio:9000/minio/health/live

# Check disk space
kubectl exec -it <minio-pod> -- df -h /data
```

## Mitigation

### Disk Full
```bash
# Check bucket sizes
kubectl exec -it <minio-pod> -- du -sh /data/avgcxr-*

# Clean up old export files
mc rm --recursive --older-than 30d local/avgcxr-exports/

# Clean incomplete uploads
mc rm --recursive --incomplete local/avgcxr-documents/
```

### Connection Refused
```bash
# Restart MinIO
kubectl rollout restart deployment avgcxr-minio -n avgcxr-prod

# Re-run bucket initialization
kubectl exec -it <minio-pod> -- sh /usr/local/bin/init-buckets.sh
```

## Prevention
- Monitor disk usage (alert at 80%)
- Lifecycle policies to auto-delete old exports
- Regular backup of document bucket
