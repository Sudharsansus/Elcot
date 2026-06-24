\
# Rollback Guide

## Kubernetes Rollback

### Application Rollback

```bash
# Check deployment history
kubectl rollout history deployment/avgcxr-api -n avgcxr-prod

# Rollback to previous revision
kubectl rollout undo deployment/avgcxr-api -n avgcxr-prod

# Rollback to specific revision
kubectl rollout undo deployment/avgcxr-api -n avgcxr-prod --to-revision=5

# Verify rollback
kubectl rollout status deployment/avgcxr-api -n avgcxr-prod
```

### Database Rollback

Flyway migrations that are marked as undoable can be rolled back:

```bash
# Check migration status
mvn flyway:info -Dflyway.config.files=infra/docker/postgres/flyway-prod.conf

# Undo last migration (if undo script exists)
mvn flyway:undo -Dflyway.config.files=infra/docker/postgres/flyway-prod.conf
```

## Rollback Decision Criteria

| Severity | Action | Timeline |
|----------|--------|----------|
| Critical (payment failure, data loss) | Immediate rollback | < 5 minutes |
| High (login failure, 500 errors > 5%) | Rollback within 15 minutes | < 15 minutes |
| Medium (feature broken, partial degradation) | Assess, may fix forward | < 1 hour |
| Low (cosmetic, non-critical) | Fix forward in next release | Next sprint |

## Post-Rollback

1. Create incident ticket with root cause analysis
2. Notify stakeholders via email
3. Fix the issue in a hotfix branch
4. Deploy hotfix through normal deployment process
5. Add regression test to prevent recurrence
