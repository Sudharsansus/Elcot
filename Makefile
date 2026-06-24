.PHONY: help setup dev build test lint clean docker-up docker-down db-migrate db-seed db-reset \
  api-build api-test api-run worker-build worker-run cms-build cms-run \
  security-scan load-test smoke-test release doctor

# ─── Defaults ───────────────────────────────────────────
.DEFAULT_GOAL := help
COMPOSE        := docker compose -f infra/docker/docker-compose.yml
COMPOSE_DEV    := $(COMPOSE) -f infra/docker/docker-compose.dev.yml
COMPOSE_PROD   := $(COMPOSE) -f infra/docker/docker-compose.prod.yml
PNPM           := pnpm
MAVEN          := ./mvnw -q
NPM            := pnpm

# ─── Help ───────────────────────────────────────────────
help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

# ─── Setup & Bootstrap ──────────────────────────────────
setup: ## Full first-time setup (install deps, start infra, migrate, seed)
	@echo "==> Installing Node dependencies..."
	$(PNPM) install --frozen-lockfile 2>/dev/null || $(PNPM) install
	@echo "==> Starting infrastructure services..."
	$(COMPOSE) up -d postgres redis rabbitmq elasticsearch minio pgbouncer
	@echo "==> Waiting for services to be healthy..."
	@sleep 15
	@echo "==> Running database migrations..."
	$(MAVEN) -pl apps/api flyway:migrate
	@echo "==> Seeding database..."
	$(MAVEN) -pl apps/api flyway:migrate -Dflyway.locations=filesystem:apps/api/src/main/resources/db/seed
	@echo "==> Setup complete! Run 'make dev' to start development."

bootstrap: setup ## Alias for setup

# ─── Development ────────────────────────────────────────
dev: ## Start all services in development mode
	$(COMPOSE_DEV) up -d
	$(PNPM) run start:dev &

build: ## Build everything (frontend + backend)
	$(MAVEN) clean package -DskipTests
	$(PNPM) run build:all

build:api: ## Build API only
	$(MAVEN) -pl apps/api -am clean package -DskipTests

build:worker: ## Build Worker only
	$(MAVEN) -pl apps/worker -am clean package -DskipTests

build:frontend: ## Build all Angular portals
	$(PNPM) run build:all

# ─── Testing ────────────────────────────────────────────
test: test:api test:frontend ## Run all tests

test:api: ## Run Java tests
	$(MAVEN) test

test:api:integration: ## Run Java integration tests (requires Docker)
	$(MAVEN) verify

test:frontend: ## Run Angular tests
	$(PNPM) run test:all

test:e2e: ## Run Playwright end-to-end tests
	npx playwright test --config=tests/e2e/playwright.config.ts

# ─── Linting ────────────────────────────────────────────
lint: lint:java lint:frontend ## Lint everything

lint:java: ## Spotless check (Java)
	$(MAVEN) spotless:check

lint:java:fix: ## Spotless format (Java)
	$(MAVEN) spotless:apply

lint:frontend: ## ESLint (Angular)
	$(PNPM) run lint:all

# ─── Docker ─────────────────────────────────────────────
docker-up: ## Start all Docker services
	$(COMPOSE_DEV) up -d

docker-down: ## Stop all Docker services
	$(COMPOSE) down

docker-logs: ## Tail Docker service logs (SERVICE=api)
	$(COMPOSE) logs -f $(SERVICE)

docker-reset: ## Stop services and remove all volumes (DESTRUCTIVE)
	$(COMPOSE) down -v
	@echo "==> All Docker volumes removed."

# ─── Database ───────────────────────────────────────────
db-migrate: ## Run Flyway database migrations
	$(MAVEN) -pl apps/api flyway:migrate

db-seed: ## Seed database with sample data
	bash scripts/seed.sh

db-reset: ## Drop and recreate database (DESTRUCTIVE)
	$(COMPOSE) exec postgres psql -U avgcxr_user -d avgcxr_portal -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"
	$(MAVEN) -pl apps/api flyway:migrate
	$(MAKE) db-seed

db-status: ## Show Flyway migration status
	$(MAVEN) -pl apps/api flyway:info

# ─── API Service ────────────────────────────────────────
api-run: ## Run API locally (requires infrastructure running)
	$(MAVEN) -pl apps/api spring-boot:run -Dspring-boot.run.profiles=dev

api-debug: ## Run API with remote debugging on port 5005
	$(MAVEN) -pl apps/api spring-boot:run -Dspring-boot.run.profiles=dev -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

# ─── Worker Service ─────────────────────────────────────
worker-run: ## Run Worker locally
	$(MAVEN) -pl apps/worker spring-boot:run -Dspring-boot.run.profiles=dev

# ─── CMS Service ────────────────────────────────────────
cms-run: ## Run Strapi CMS locally
	cd apps/cms && pnpm develop

# ─── Security ───────────────────────────────────────────
security-scan: ## Run all security scans
	bash scripts/security-scan.sh

# ─── Load Testing ───────────────────────────────────────
load-test: ## Run k6 load test
	bash scripts/load-test.sh

# ─── Quality Gates ──────────────────────────────────────
smoke-test: ## Run smoke tests against running services
	bash scripts/smoke.sh

doctor: ## Diagnose development environment
	bash scripts/doctor.sh

# ─── Release ────────────────────────────────────────────
release: ## Create release build
	bash scripts/release.sh

release-dry-run: ## Preview release without building
	@echo "==> Checking branch..."
	@git branch --show-current | grep -q '^main$$' || (echo "ERROR: Must be on main branch" && exit 1)
	@echo "==> Running tests..."
	$(MAVEN) test -q
	$(PNPM) run test:all
	@echo "==> Dry run complete. Run 'make release' to build."

# ─── Logs ───────────────────────────────────────────────
logs-api: ## Tail API logs
	$(COMPOSE) logs -f api

logs-worker: ## Tail Worker logs
	$(COMPOSE) logs -f worker

# ─── Clean ──────────────────────────────────────────────
clean: ## Clean all build artifacts
	$(MAVEN) clean
	rm -rf apps/*/dist apps/*/target
	rm -rf libs/*/dist
	$(PNPM) store prune
