\
# Deployment Guide

## Environments

| Environment | URL | Purpose |
|-------------|-----|---------|
| Development | localhost | Local developer machines |
| Staging | staging.avgcxr.elcot.tn.gov.in | Pre-production testing |
| Production | avgcxr.elcot.tn.gov.in | Public-facing production |

## Deployment Strategy

**Blue-Green deployment** using Kubernetes. New pods are started alongside existing pods, health-checked, and traffic is switched via service update.

## Deployment Steps

### 1. Pre-deployment Checklist

- [ ] All tests pass (unit, integration, e2e)
- [ ] OWASP dependency scan clean (no HIGH/CRITICAL CVEs)
- [ ] Flyway migration tested on staging database backup
- [ ] API contract tests pass
- [ ] Accessibility audit score >= 90
- [ ] Release tag created (e.g., `v1.2.0`)

### 2. Build

```bash
# Backend
mvn clean package -Pprod -DskipTests

# Frontend
npx nx run-many --target=build --configuration=production --all
```

### 3. Docker Images

```bash
# Build and push API image
docker build -t elcot/avgcxr-api:v1.2.0 -f apps/api/Dockerfile .
docker push elcot/avgcxr-api:v1.2.0

# Build and push CMS image (if changed)
docker build -t elcot/avgcxr-cms:v1.2.0 -f apps/cms/Dockerfile .
docker push elcot/avgcxr-cms:v1.2.0
```

### 4. Deploy to Kubernetes

```bash
# Update image tags in Kustomization
kubectl set image deployment/avgcxr-api api=elcot/avgcxr-api:v1.2.0 -n avgcxr-prod

# Watch rollout
kubectl rollout status deployment/avgcxr-api -n avgcxr-prod

# Run smoke tests
./scripts/smoke.sh
```

### 5. Post-deployment

- [ ] Verify health endpoints return 200
- [ ] Check error rates in monitoring dashboard
- [ ] Verify bilingual content loads correctly
- [ ] Test critical user flows (login, scheme browse, application submit)

## Rollback

```bash
kubectl rollout undo deployment/avgcxr-api -n avgcxr-prod
```

See [Rollback Guide](rollback.md) for detailed procedures.
