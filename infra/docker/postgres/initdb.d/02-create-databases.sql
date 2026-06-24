-- AVGC-XR Portal: Database and role setup
-- Runs on first initialization only.

-- Create application user (password set via POSTGRES_PASSWORD env var
-- or changed immediately after first connection)
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'avgcxr_app') THEN
        CREATE ROLE avgcxr_app WITH LOGIN PASSWORD 'avgcxr_app_dev_pwd';
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'avgcxr_readonly') THEN
        CREATE ROLE avgcxr_readonly WITH LOGIN PASSWORD 'avgcxr_readonly_dev_pwd';
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'avgcxr_admin') THEN
        CREATE ROLE avgcxr_admin WITH LOGIN PASSWORD 'avgcxr_admin_dev_pwd' CREATEROLE;
    END IF;
END $$;

-- Create read-only schema for reporting
CREATE SCHEMA IF NOT EXISTS reporting AUTHORIZATION avgcxr_admin;

-- Default privileges
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO avgcxr_readonly;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON SEQUENCES TO avgcxr_readonly;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO avgcxr_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO avgcxr_app;

-- Flyway schema history table access
GRANT ALL ON SCHEMA public TO avgcxr_app;
GRANT ALL ON ALL TABLES IN SCHEMA public TO avgcxr_app;

DO $$
BEGIN
    RAISE NOTICE 'AVGC-XR Portal databases and roles created';
END $$;
