# Terraform Infrastructure - AVGC-XR Portal

## Structure

```
infra/terraform/
├── modules/
│   ├── network/          # VPC, subnets, security groups, NAT
│   ├── database/         # RDS Aurora PostgreSQL 16
│   ├── redis/            # ElastiCache Redis 7
│   ├── elasticsearch/    # OpenSearch (Elasticsearch 8.11)
│   ├── rabbitmq/         # Amazon MQ for RabbitMQ
│   ├── object-storage/   # S3 buckets (documents, media, exports, backups)
│   └── secrets/          # AWS Secrets Manager + KMS
└── environments/
    ├── dev/              # Development (single AZ, smaller instances)
    ├── staging/          # Staging (multi-AZ, moderate sizing)
    └── prod/             # Production (multi-AZ, full sizing, encryption)
```

## Usage

```bash
# Initialize
cd infra/terraform/environments/dev
terraform init

# Plan
terraform plan

# Apply
terraform apply

# Destroy (dev only)
terraform destroy
```

## State Management

Terraform state is stored in S3 with encryption:
- Dev: `avgcxr-portal-tfstate-dev`
- Staging: `avgcxr-portal-tfstate-staging`
- Prod: `avgcxr-portal-tfstate-prod`

## Security

- All secrets are managed via AWS Secrets Manager
- KMS key rotation is enabled
- S3 buckets have public access blocked
- RDS and ElastiCache are in private subnets
- TLS 1.2+ enforced on all endpoints
