\
# Disaster Recovery Plan

## Recovery Objectives

| Metric | Target |
|--------|--------|
| RPO (Recovery Point Objective) | 1 hour |
| RTO (Recovery Time Objective) | 4 hours |
| Maximum tolerable downtime | 8 hours |

## Backup Strategy

### Database Backups

| Type | Frequency | Retention | Storage |
|------|-----------|-----------|---------|
| Aurora automated backup | Continuous | 35 days | AWS-managed |
| Logical backup (pg_dump) | Daily at 03:00 IST | 30 days | S3 (avgcxr-backups) |
| Snapshot before migration | Per deployment | 7 days | S3 (avgcxr-backups) |

### Application Backups

| Type | Frequency | Retention | Storage |
|------|-----------|-----------|---------|
| Docker images | Per release | All versions | ECR |
| Kubernetes manifests | Version controlled | Indefinite | Git |
| MinIO data | Daily incremental | 90 days | S3 cross-region |
| Elasticsearch snapshots | Weekly | 4 weeks | S3 |

## Recovery Procedures

### Scenario 1: Database Failure

1. Aurora failover to reader node (automatic, < 30 seconds)
2. If Aurora cluster failure: restore from latest snapshot
3. If data corruption: restore from logical backup + replay WAL

### Scenario 2: Complete Data Center Failure

1. Promote DR region (ap-south-2) Terraform environment
2. Restore database from latest cross-region backup
3. Deploy application from ECR images
4. Update DNS to point to DR region
5. Verify all services via smoke tests

### Scenario 3: Ransomware Attack

1. Isolate affected systems (disconnect from network)
2. Assess scope of encryption/data exfiltration
3. Notify ELCOT security team and CERT-In
4. Restore from last known clean backup
5. Perform forensic analysis before reconnection

## DR Drills

DR drills are conducted quarterly:
- Q1: Database failover test
- Q2: Full DR simulation (region failover)
- Q3: Backup restoration test
- Q4: Ransomware response tabletop exercise
