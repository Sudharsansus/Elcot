\
# Incident Runbooks

## How to Use

Each runbook follows this structure:
1. **Symptoms**: How to identify the incident
2. **Impact**: What users experience
3. **Diagnosis**: Steps to confirm root cause
4. **Mitigation**: Immediate actions to restore service
5. **Resolution**: Fix the root cause
6. **Prevention**: Avoid recurrence

## Runbooks

| Runbook | Severity | Est. Resolution Time |
|---------|----------|---------------------|
| [API Down](api-down.md) | P1 Critical | 15 minutes |
| [PostgreSQL Down](postgres-down.md) | P1 Critical | 30 minutes |
| [Redis Down](redis-down.md) | P2 High | 15 minutes |
| [RabbitMQ Broker Down](rabbitmq-broker-down.md) | P2 High | 20 minutes |
| [Elasticsearch Down](elasticsearch-down.md) | P3 Medium | 30 minutes |
| [MinIO Down](minio-down.md) | P3 Medium | 20 minutes |
| [Security Incident](security-incident.md) | P1 Critical | Variable |
| [Scheme Day Traffic Spike](scheme-day-traffic-spike.md) | P2 High | 15 minutes |

## Escalation

1. On-call engineer (PagerDuty)
2. Tech lead (5 minutes if no response)
3. Engineering manager (15 minutes)
4. ELCOT IT director (30 minutes for P1)
