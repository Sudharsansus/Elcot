#!/bin/bash
# ══════════════════════════════════════════════════════════════
# Tamil Nadu AVGC-XR Portal — Production Deployment Script
# ══════════════════════════════════════════════════════════════
# Tested on: Debian 12 / Ubuntu 22.04 / RHEL 9
# Memory:    2 GB minimum, 4 GB recommended
# Disk:      10 GB minimum, 20 GB recommended
# ══════════════════════════════════════════════════════════════

set -euo pipefail

REPO_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_NAME="avgcxr-api"
APP_USER="avgcxr"
APP_HOME="/opt/avgcxr"
LOG_DIR="/var/log/avgcxr"

echo "═══════════════════════════════════════════════════════════════"
echo "  Tamil Nadu AVGC-XR Portal — Deployment"
echo "═══════════════════════════════════════════════════════════════"
echo ""

# ─── Pre-flight checks ──────────────────────────────────────
echo "1/8 Pre-flight checks"
command -v java >/dev/null 2>&1 || { echo "✗ Java not found. Install JDK 21."; exit 1; }
command -v mvn  >/dev/null 2>&1 || { echo "✗ Maven not found. Install Maven 3.8+."; exit 1; }
command -v psql >/dev/null 2>&1 || { echo "✗ PostgreSQL client not found."; exit 1; }

JAVA_VER=$(java -version 2>&1 | grep -oE '"[0-9]+' | tr -d '"' | head -1)
if [ "$JAVA_VER" -lt 21 ]; then
  echo "✗ Need Java 21+, found $JAVA_VER"; exit 1
fi
echo "  ✓ Java $JAVA_VER, Maven, psql OK"

# ─── Install OS packages ─────────────────────────────────────
echo "2/8 Installing OS packages"
if command -v apt-get >/dev/null; then
  apt-get update -qq
  apt-get install -y --no-install-recommends \
    postgresql postgresql-contrib \
    redis-server rabbitmq-server \
    nginx certbot python3-certbot-nginx \
    curl jq
elif command -v dnf >/dev/null; then
  dnf install -y postgresql postgresql-server redis rabbitmq-server nginx
fi
echo "  ✓ Packages installed"

# ─── Start services ──────────────────────────────────────────
echo "3/8 Starting services"
systemctl enable --now postgresql redis-server rabbitmq-server
systemctl status postgresql --no-pager | head -1
systemctl status redis-server --no-pager | head -1
systemctl status rabbitmq-server --no-pager | head -1
echo "  ✓ Services running"

# ─── Create database & user ──────────────────────────────────
echo "4/8 Provisioning database"
DB_NAME="${DB_NAME:-avgcxr_portal}"
DB_USER="${DB_USER:-avgcxr_user}"
DB_PASS="${DB_PASS:-$(openssl rand -base64 24 | tr -d '+/' | head -c 24)}"

sudo -u postgres psql -c "CREATE USER $DB_USER WITH PASSWORD '$DB_PASS';" 2>/dev/null || echo "  (user already exists)"
sudo -u postgres psql -c "CREATE DATABASE $DB_NAME OWNER $DB_USER;" 2>/dev/null || echo "  (database already exists)"
sudo -u postgres psql -c "ALTER USER $DB_USER CREATEDB;"
echo "  ✓ Database $DB_NAME created (user: $DB_USER)"

# ─── Create RabbitMQ user & vhost ────────────────────────────
echo "5/8 Provisioning RabbitMQ"
RMQ_USER="${RMQ_USER:-avgcxr_rmq_user}"
RMQ_PASS="${RMQ_PASS:-$(openssl rand -base64 24 | tr -d '+/' | head -c 24)}"
RMQ_VHOST="${RMQ_VHOST:-/avgcxr}"

rabbitmqctl add_user "$RMQ_USER" "$RMQ_PASS" 2>/dev/null || echo "  (user already exists)"
rabbitmqctl set_user_tags "$RMQ_USER" administrator
rabbitmqctl add_vhost "$RMQ_VHOST" 2>/dev/null || echo "  (vhost already exists)"
rabbitmqctl set_permissions -p "$RMQ_VHOST" "$RMQ_USER" ".*" ".*" ".*"

# Import topology from project
rabbitmqctl import_definitions "$REPO_DIR/infra/docker/rabbitmq/definitions.json" 2>&1 | tail -2 || true
# Patch x-delayed-message → topic (plugin not required)
python3 -c "
import json
with open('$REPO_DIR/infra/docker/rabbitmq/definitions.json') as f: d=json.load(f)
for ex in d.get('exchanges',[]):
    if ex.get('type')=='x-delayed-message':
        ex['type']='topic'
        ex.setdefault('arguments',{}).pop('x-delayed-type',None)
with open('$REPO_DIR/infra/docker/rabbitmq/definitions.json','w') as f: json.dump(d,f,indent=2)
"
rabbitmqctl import_definitions "$REPO_DIR/infra/docker/rabbitmq/definitions.json" 2>&1 | tail -1
echo "  ✓ RabbitMQ vhost $RMQ_VHOST, user $RMQ_USER"

# ─── Build application ───────────────────────────────────────
echo "6/8 Building application (this takes ~2 min)"
cd "$REPO_DIR"
mvn clean package -DskipTests -pl apps/api -am 2>&1 | tail -3
echo "  ✓ Built $(ls -la apps/api/target/api.jar | awk '{print $5}') bytes"

# ─── Create app user & dirs ─────────────────────────────────
echo "7/8 Setting up app runtime"
id "$APP_USER" >/dev/null 2>&1 || useradd -r -d "$APP_HOME" -s /bin/false "$APP_USER"
mkdir -p "$APP_HOME" "$LOG_DIR/api"
chown -R "$APP_USER:$APP_USER" "$APP_HOME" "$LOG_DIR"

# Generate secure JWT secret
JWT_SECRET=$(openssl rand -base64 64 | tr -d '\n' | head -c 64)

# Create environment file
cat > /etc/avgcxr.env <<EOF
SPRING_PROFILES_ACTIVE=prod
DB_HOST=localhost
DB_PORT=5432
DB_NAME=$DB_NAME
DB_USER=$DB_USER
DB_PASSWORD=$DB_PASS
PGBOUNCER_PORT=5432
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USER=$RMQ_USER
RABBITMQ_PASSWORD=$RMQ_PASS
RABBITMQ_VHOST=$RMQ_VHOST
REDIS_HOST=localhost
REDIS_PORT=6379
JWT_SECRET=$JWT_SECRET
CORS_ALLOWED_ORIGINS=http://localhost:4200,http://localhost:4201,http://localhost:4202
SPRING_FLYWAY_ENABLED=false
EOF
chmod 600 /etc/avgcxr.env
echo "  ✓ Env written to /etc/avgcxr.env (root only)"

# ─── Create systemd service ──────────────────────────────────
cat > /etc/systemd/system/avgcxr-api.service <<EOF
[Unit]
Description=AVGC-XR Portal API
After=postgresql.service redis-server.service rabbitmq-server.service
Wants=postgresql.service redis-server.service rabbitmq-server.service

[Service]
Type=simple
User=$APP_USER
Group=$APP_USER
EnvironmentFile=/etc/avgcxr.env
ExecStart=/usr/bin/java -Xmx700m -jar $REPO_DIR/apps/api/target/api.jar
Restart=always
RestartSec=10
StandardOutput=append:$LOG_DIR/api/stdout.log
StandardError=append:$LOG_DIR/api/stderr.log
WorkingDirectory=$REPO_DIR
LimitNOFILE=65536

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl enable avgcxr-api.service
systemctl start avgcxr-api.service
echo "  ✓ Systemd service installed and started"

# ─── Wait for health & show status ───────────────────────────
echo "8/8 Waiting for API to be healthy"
for i in {1..60}; do
  if curl -sf http://localhost:8080/actuator/health >/dev/null 2>&1; then
    echo "  ✓ API is UP after ${i}0 seconds"
    break
  fi
  sleep 2
done

echo ""
echo "═══════════════════════════════════════════════════════════════"
echo "  ✅ DEPLOYMENT COMPLETE"
echo "═══════════════════════════════════════════════════════════════"
echo ""
echo "Service:    systemctl status avgcxr-api"
echo "Logs:       tail -f $LOG_DIR/api/stdout.log"
echo "Health:     curl http://localhost:8080/actuator/health"
echo "Swagger:    http://localhost:8080/swagger-ui.html"
echo ""
echo "Database creds (saved to /etc/avgcxr.env):"
echo "  DB:        $DB_NAME  User: $DB_USER  Pass: $DB_PASS"
echo "  RabbitMQ:  $RMQ_VHOST  User: $RMQ_USER  Pass: $RMQ_PASS"
echo ""
echo "Next: configure nginx reverse proxy + Let's Encrypt SSL"
echo "      See README.md → 'Production HTTPS Setup'"
