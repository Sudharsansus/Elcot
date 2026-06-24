# Contributing to AVGC-XR Portal

## Development Workflow

1. Create a feature branch from `develop`: `git checkout -b feature/AVGCXR-123-scheme-search develop`
2. Make your changes following the architecture patterns
3. Run linting and tests: `make lint && make test`
4. Commit with conventional commits: `feat(policy/scheme): add district-wise scheme filtering`
5. Push and create a pull request

## Branch Naming

| Type | Pattern | Example |
|------|---------|---------|
| Feature | `feature/AVGCXR-{ticket}-{description}` | `feature/AVGCXR-42-scheme-search` |
| Bug fix | `fix/AVGCXR-{ticket}-{description}` | `fix/AVGCXR-88-login-redirect` |
| Chore | `chore/{description}` | `chore/upgrade-spring-boot-3.4` |
| Hotfix | `hotfix/{description}` | `hotfix/fix-memory-leak` |

## Commit Messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):
- `feat(policy/scheme): add filtering by category and district`
- `fix(platform/auth): resolve token refresh on concurrent requests`
- `refactor(infra/nginx): simplify rate-limit configuration`

Include the bounded context scope when applicable.

## Code Standards

### Java
- Google Java Format (enforced by Spotless)
- Use records for DTOs and value objects
- Hexagonal architecture: domain layer has NO Spring annotations
- MapStruct for entity-domain mapping
- Javadoc on all public APIs

### TypeScript
- Strict mode enabled, no `any` types
- Angular 17 standalone components with signals
- Use `input()`, `output()`, `computed()`, `effect()`
- All user-facing strings must have i18n (English + Tamil)

### SQL
- All schema changes via Flyway migrations
- Use UUIDs for primary keys
- Include proper indexes
- Tamil text search uses `tamil` text search configuration

## Pull Request Process

1. PR targets `develop` (or `main` for hotfixes)
2. CI must pass (lint, build, test)
3. At least 1 approval required
4. Squash merge preferred
