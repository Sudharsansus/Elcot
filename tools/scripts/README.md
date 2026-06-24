\
# Development Tools and Scripts

## Database Scripts

| Script | Purpose |
|--------|---------|
| `scripts/bootstrap.sh` | Full environment setup |
| `scripts/migrate.sh` | Run Flyway migrations |
| `scripts/seed.sh` | Seed reference data |
| `scripts/reset.sh` | Reset development environment |

## Operations Scripts

| Script | Purpose |
|--------|---------|
| `scripts/smoke.sh` | Post-deployment health checks |
| `scripts/doctor.sh` | Diagnose environment issues |
| `scripts/security-scan.sh` | Run security scanners |
| `scripts/load-test.sh` | Run k6 load tests |
| `scripts/release.sh` | Create a tagged release build |

## CI/CD Integration

All scripts are designed for both local use and CI/CD pipeline integration.
Each script returns appropriate exit codes for pipeline gating.
