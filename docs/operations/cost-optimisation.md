\
# Cost Optimisation

## Monthly Cost Estimates (Production)

| Component | Service | Monthly Cost (INR) |
|-----------|---------|-------------------|
| EKS Cluster | 3 nodes (m6i.2xlarge) | 45,000 |
| Aurora PostgreSQL | db.r6g.xlarge, 2 nodes | 60,000 |
| ElastiCache Redis | cache.r6g.xlarge | 15,000 |
| OpenSearch | r6g.xlarge.search | 35,000 |
| Amazon MQ | mq.r6g.xlarge | 20,000 |
| S3 (all buckets) | ~500 GB | 5,000 |
| ALB + NAT Gateway | Traffic-based | 10,000 |
| CloudWatch + Logs | Monitoring | 8,000 |
| Route 53 | DNS | 500 |
| ACM | TLS certificates | 0 (free) |
| Secrets Manager | 10 secrets | 1,000 |
| **Total** | | **~2,00,000/month** |

## Optimisation Strategies

### 1. Right-sizing

- Use `t4g` instances for dev/staging (Graviton2, 40% cheaper)
- Implement HPA to scale down during off-peak hours (night: 0-6 AM)
- Use spot instances for non-critical workloads (export generation)

### 2. Reserved Instances

- Purchase 1-year reserved instances for steady-state workloads (30% savings)
- Savings Plans for compute (20% savings on EKS nodes)

### 3. Storage Optimisation

- S3 Intelligent-Tiering for backup buckets
- Lifecycle policies: Standard → IA (30 days) → Glacier (90 days)
- Elasticsearch index lifecycle: hot (7 days) → warm (30 days) → delete

### 4. Network Optimisation

- VPC endpoints for AWS services (eliminate NAT gateway costs)
- CloudFront CDN for Angular static assets (reduce ALB bandwidth)
- Compress API responses with gzip (Nginx handles this)

### 5. Database Optimisation

- Aurora Serverless v2 for staging (pay-per-use)
- Read replicas for reporting queries (avoid overloading primary)
- Query optimization via `pg_stat_statements` analysis
