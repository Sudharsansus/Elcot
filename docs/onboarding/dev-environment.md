\
# Development Environment Setup

## Prerequisites

| Tool | Version | Installation |
|------|---------|-------------|
| JDK | 21+ | `sudo apt install openjdk-21-jdk` or SDKMAN |
| Maven | 3.9+ | `sudo apt install maven` or SDKMAN |
| Node.js | 20 LTS | `nvm install 20` |
| npm | 10+ | Included with Node.js |
| Docker | 24+ | `docker.com/get-docker` |
| Docker Compose | 2.20+ | Included with Docker Desktop |
| Git | 2.40+ | `sudo apt install git` |
| IDE | Recent | IntelliJ IDEA (backend) or VS Code (full-stack) |

## Setup Steps

### 1. Clone the Repository

```bash
git clone https://github.com/elcot/avgc-xr-portal.git
cd avgc-xr-portal
```

### 2. Run Bootstrap

```bash
chmod +x scripts/*.sh
./scripts/bootstrap.sh
```

This will:
- Verify all prerequisites
- Start Docker infrastructure (PostgreSQL, Redis, RabbitMQ, Elasticsearch, MinIO)
- Build the Spring Boot API
- Build all Angular portals
- Run Flyway migrations
- Initialize MinIO buckets

### 3. Verify

```bash
./scripts/doctor.sh
./scripts/smoke.sh
```

### 4. Start Development Servers

**Backend (terminal 1):**
```bash
mvn spring-boot:run
```

**Frontend (terminal 2):**
```bash
npx nx run-many --target=serve --all
```

### 5. Access

| Service | URL |
|---------|-----|
| Public Portal | http://localhost:4200 |
| Applicant Portal | http://localhost:4300 |
| Admin Portal | http://localhost:4400 |
| API | http://localhost:8080 |
| API Docs | http://localhost:8080/api/docs |
| CMS Admin | http://localhost:1337/admin |
| RabbitMQ Management | http://localhost:15672 |
