\
# Plop Code Generators

Scaffold new features quickly using Plop templates.

## Available Generators

| Generator | Command | Description |
|-----------|---------|-------------|
| Angular Feature | `npx plop angular-feature` | Creates a new Angular feature module |
| Spring Service | `npx plop spring-service` | Creates a new bounded context |

## Usage

```bash
npx plop
# Select generator from interactive menu
```

## Angular Feature Generator

Creates:
- Component (TS + HTML + SCSS + spec)
- Service (TS + spec)
- Routing configuration
- i18n keys stub

## Spring Service Generator

Creates:
- Bounded context package structure (api/application/domain/infrastructure)
- Domain entity
- Repository interface
- JPA implementation
- REST controller
- Flyway migration stub
- Unit test stub
