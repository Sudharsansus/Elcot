\
# Incident Response Plan

## Team

| Role | Responsibility | Contact |
|------|---------------|---------|
| Incident Commander | Overall coordination, stakeholder communication | On-call rotation |
| Technical Lead | Root cause analysis, technical mitigation | On-call rotation |
| Security Engineer | Forensic analysis, evidence preservation | ELCOT security team |
| Communications Lead | User notification, media response | ELCOT communications |

## Phases

### Phase 1: Detection (0-15 minutes)

Sources of detection:
- Automated alerts (Prometheus/Alertmanager)
- User reports (support tickets)
- Security tools (WAF, SIEM)
- Monitoring dashboards

Actions:
1. Acknowledge the alert
2. Assess severity (P1-P4)
3. Activate incident channel (Slack/Diageo)

### Phase 2: Containment (15-60 minutes)

P1 actions:
1. Block attack vector (IP block, WAF rule, disable feature)
2. Rotate compromised credentials
3. Revoke active sessions
4. Isolate affected systems

P2/P3 actions:
1. Apply temporary fix
2. Monitor for escalation
3. Communicate status to stakeholders

### Phase 3: Eradication (1-4 hours)

1. Identify root cause
2. Develop permanent fix
3. Test fix in staging
4. Deploy fix to production

### Phase 4: Recovery (4-24 hours)

1. Verify fix is effective
2. Restore affected data from backups
3. Gradually re-enable features
4. Monitor for recurrence

### Phase 5: Post-Incident (24-72 hours)

1. Write incident report (template below)
2. Conduct blameless post-mortem
3. Update runbooks and detection rules
4. Track action items

## Incident Report Template

```markdown
# Incident Report: [Title]

- **Date**: YYYY-MM-DD
- **Severity**: P1/P2/P3/P4
- **Duration**: X hours Y minutes
- **Incident Commander**: Name
- **Impact**: Brief description of user impact
- **Root Cause**: What happened
- **Timeline**: Key events with timestamps
- **Actions Taken**: What we did
- **Lessons Learned**: What we can improve
- **Action Items**: Specific follow-ups with owners and deadlines
```

## CERT-In Reporting

Per Indian cybersecurity regulations, report incidents to CERT-In within 6 hours for:
- Data breaches involving personal data of Indian citizens
- Ransomware attacks
- Unauthorized access to government systems
