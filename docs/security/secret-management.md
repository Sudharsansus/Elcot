\
# Secret Management

## Principles

1. **No secrets in code** — no passwords, API keys, or tokens in source code.
2. **No secrets in config files** — environment-specific secrets come from secret stores.
3. **Rotate regularly** — database passwords rotated quarterly, API keys monthly.
4. **Audit access** — all secret access is logged.

## Secret Storage by Environment

| Environment | Storage | Access |
|-------------|---------|--------|
| Development | `.env` files (gitignored) + Docker secrets | Developer machine |
| Staging | AWS Secrets Manager | Staging IAM role |
| Production | AWS Secrets Manager + KMS | Production IAM role, audit-logged |

## Secret Inventory

| Secret | Location | Rotation Frequency |
|--------|----------|-------------------|
| Database password (avgcxr_app) | Secrets Manager: `avgcxr-prod/database` | Quarterly |
| Redis password | Secrets Manager: `avgcxr-prod/redis` | Quarterly |
| RabbitMQ password | Secrets Manager: `avgcxr-prod/rabbitmq` | Quarterly |
| JWT signing key | Secrets Manager: `avgcxr-prod/jwt` | Annually |
| Strapi admin JWT | Secrets Manager: `avgcxr-prod/strapi` | Quarterly |
| Email SMTP password | Secrets Manager: `avgcxr-prod/email` | Quarterly |
| SMS gateway API key | Secrets Manager: `avgcxr-prod/sms` | Monthly |
| MinIO access/secret keys | Secrets Manager: `avgcxr-prod/minio` | Quarterly |
| Aadhaar eKYC API key | Secrets Manager: `avgcxr-prod/ekyc` | Monthly |
| Payment gateway keys | Secrets Manager: `avgcxr-prod/payment` | Monthly |

## Access Pattern

```java
// Spring Boot reads secrets from environment variables
// AWS Secrets Manager injects them via External Secrets Operator

@Value("${avgcxr.database.password}")
private String dbPassword;

// Or use Spring Cloud AWS Secrets Manager
@Configuration
public class SecretsConfig {
    @Bean
    public DataSource dataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${avgcxr.database.password}") String password) {
        return DataSourceBuilder.create()
                .url(url).username(username).password(password).build();
    }
}
```

## Rotation Procedure

1. Generate new secret value
2. Update secret in AWS Secrets Manager
3. Restart affected services (PgBouncer, Redis, etc.)
4. Verify application connectivity
5. Revoke old secret
6. Document rotation in audit log
