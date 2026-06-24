# Observability Stack — AVGC-XR Portal

## Stack Overview

The Tamil Nadu AVGC-XR Portal runs a full **metrics → logging → tracing** observability stack built on open-source CNCF tools:

| Component       | Purpose                     | Version | Port    |
|-----------------|-----------------------------|---------|---------|
| **Prometheus**  | Metrics collection & alerting | 2.53    | 9090    |
| **Grafana**     | Dashboards & visualization  | 10.4    | 3000    |
| **Loki**        | Log aggregation (logQL)     | 2.9     | 3100    |
| **Tempo**       | Distributed tracing (OTLP)  | 2.5     | 3200    |
| **Alertmanager**| Alert routing & escalation  | 0.27    | 9093    |
| **Promtail**    | Log shipper (Docker/journal)| 2.9     | 9080    |

---

## Architecture Diagram

```
                           ┌─────────────────────────────────────────────────┐
                           │              GRAFANA (:3000)                    │
                           │  ┌──────────┐ ┌───────┐ ┌───────┐ ┌──────────┐ │
                           │  │Prometheus│ │ Loki  │ │ Tempo │ │Elastic-  │ │
                           │  │   DS     │ │  DS   │ │  DS   │ │search DS │ │
                           │  └────┬─────┘ └───┬───┘ └───┬───┘ └────┬─────┘ │
                           └───────┼───────────┼─────────┼──────────┼───────┘
                                   │           │         │          │
              ┌────────────────────┼───────────┼─────────┼──────────┼───────┐
              │                    ▼           ▼         ▼          ▼       │
              │  ┌──────────────────────────────────────────────────────┐   │
              │  │              PROMETHEUS (:9090)                      │   │
              │  │  • Scrapes all /actuator/prometheus endpoints       │   │
              │  │  • Scrapes postgres_exporter, redis_exporter, etc.  │   │
              │  │  • Evaluates 20+ alert rules every 15s              │   │
              │  │  • Forwards alerts to Alertmanager                  │   │
              │  └──────────────────────┬───────────────────────────────┘   │
              │                         │                                    │
   D O C K E R│                         ▼                                    │
   C O M P O S E│  ┌──────────────────────────────────────┐                 │
              │  │          ALERTMANAGER (:9093)           │                 │
              │  │  • PagerDuty (critical, 0s repeat)     │                 │
              │  │  • Email (warning, every 4h)            │                 │
              │  │  • Slack #avgcxr-alerts (info, 4h)     │                 │
              │  └──────────────────────────────────────────┘                 │
              │                                                              │
              │  ┌────────────┐  ┌─────────────┐  ┌──────────────────────┐  │
              │  │   LOKI     │  │   TEMPO     │  │ postgres_exporter   │  │
              │  │  (:3100)   │  │  (:3200)    │  │ redis_exporter      │  │
              │  │  boltdb +  │  │  local +    │  │ rabbitmq_exporter   │  │
              │  │  tsdb      │  │  otlp/jaeger│  │ node_exporter       │  │
              │  └────▲───────┘  └────▲────────┘  │ cadvisor            │  │
              │       │               │            │ es_exporter         │  │
              │  ┌────┴───────┐       │            └──────────────────────┘  │
              │  │  PROMTAIL  │       │                                      │
              │  │  (:9080)   │       │                                      │
              │  │  Docker +  │       │                                      │
              │  │  journal   │       │                                      │
              │  └────────────┘       │                                      │
              └───────────────────────┼──────────────────────────────────────┘
                                      │
         ┌────────────────────────────┼────────────────────────────────────┐
         │  APPLICATION SERVICES      │     INFRASTRUCTURE                 │
         │                            │                                    │
         │  API (:8080) ◄── OTLP ────┤                                    │
         │  Worker (:8081)            │   PostgreSQL (:5432)               │
         │  CMS / Strapi (:1337)      │   Redis (:6379)                    │
         │  Public Portal (:4200)     │   RabbitMQ (:5672/:15672)          │
         │  Admin Portal (:4201)      │   Elasticsearch (:9200)            │
         │  Applicant Portal (:4202)  │   MinIO (:9000)                    │
         │                            │   PgBouncer (:6432)                │
         └────────────────────────────┴────────────────────────────────────┘
```

---

## How to Access Each Component

### Local Development

| Component     | URL                                  | Credentials              |
|---------------|--------------------------------------|--------------------------|
| Grafana       | http://localhost:3000                | admin / admin (change on first login) |
| Prometheus    | http://localhost:9090                | None (read-only UI)      |
| Alertmanager  | http://localhost:9093                | None                     |
| Loki          | http://localhost:3100                | None (API only)          |
| Tempo         | http://localhost:3200                | None (API only)          |

### Production (behind Nginx)

| Component     | URL                                           |
|---------------|-----------------------------------------------|
| Grafana       | https://monitoring.avgcxr.tn.gov.in           |
| Prometheus    | https://monitoring.avgcxr.tn.gov.in/prometheus|
| Alertmanager  | https://monitoring.avgcxr.tn.gov.in/alertmanager|

---

## Dashboard List

| # | Dashboard            | Datasource     | Purpose                                        |
|---|----------------------|----------------|------------------------------------------------|
| 1 | API Overview         | Prometheus     | Request rate, errors, latency percentiles, JVM  |
| 2 | PostgreSQL           | Prometheus     | Connections, throughput, locks, cache, replication |
| 3 | Redis                | Prometheus     | Memory, ops/sec, keys, evictions               |
| 4 | RabbitMQ             | Prometheus     | Queue depth, publish/consume rates, consumers  |
| 5 | Elasticsearch        | Prometheus     | Cluster health, indexing, search, JVM, cache   |
| 6 | Portal Frontend      | Prometheus/Loki| Page views, errors, i18n, browser/device       |
| 7 | SLA & Uptime         | Prometheus     | 99.5% target, downtime incidents, heatmap      |

All dashboards are provisioned via `observability/grafana/dashboards/*.json` and auto-loaded on container start.

---

## Alert Escalation Matrix

| Severity  | Response Time | Notification Channel      | Example Triggers                          |
|-----------|---------------|---------------------------|-------------------------------------------|
| **critical** | Immediate (< 2 min) | PagerDuty + Email + Slack | API down, DB down, ES cluster red, disk full |
| **warning**  | < 30 min     | Email (ops@elcot.tn.gov.in) + Slack | High error rate > 5%, slow queries, high memory > 80% |
| **info**     | Next business day | Slack #avgcxr-alerts only | Queue depth rising, cert expiry approaching |

### Alert Inhibition Rules

- If a **critical** alert is firing for a given `alertname`, any **warning** alert with the same `alertname` is suppressed.
- If `PostgresDown` is firing, all `PostgresHighConnections`, `PostgresSlowQueries` alerts are suppressed.

---

## Quick Start

```bash
# Start the full observability stack
docker compose -f infra/docker/docker-compose.yml up -d prometheus grafana loki promtail tempo alertmanager

# Or start everything including infrastructure
docker compose -f infra/docker/docker-compose.yml up -d

# Access Grafana
open http://localhost:3000
```

---

## Log Querying with LogQL

```logql
# All errors from API service
{service="api"} |= "ERROR" | json

# 5xx responses in last hour
{service="api"} | json | status >= 500

# Slow requests (> 2s)
{service="api"} | json | duration_ms > 2000

# Trace ID correlation
{service="api"} | json | trace_id="abc123"
```

---

## Trace Querying

Traces are collected via OTLP (gRPC on :4317, HTTP on :4318). Correlate with logs using the `trace_id` field.

```bash
# Query traces via Tempo API
curl "http://localhost:3200/api/search?tags=service.name=api"

# Trace by ID
curl "http://localhost:3200/api/traces/<trace-id>"
```

---

## Related Documentation

- [Runbooks](../docs/runbooks/README.md) — step-by-step incident response procedures
- [Deployment Guide](../docs/operations/deployment.md)
- [Security Hardening](../docs/security/secure-coding.md)