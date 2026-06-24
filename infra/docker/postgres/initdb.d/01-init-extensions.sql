-- AVGC-XR Portal: Required PostgreSQL extensions
-- This script runs on first database initialization only.

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";
CREATE EXTENSION IF NOT EXISTS "pg_stat_statements";
CREATE EXTENSION IF NOT EXISTS "btree_gin";
CREATE EXTENSION IF NOT EXISTS "btree_gist";

-- Full-text search support for Tamil + English
CREATE TEXT SEARCH CONFIGURATION IF NOT EXISTS tamil (COPY = english);
ALTER TEXT SEARCH CONFIGURATION tamil ALTER MAPPING
    FOR asciiword WITH english_stem;
ALTER TEXT SEARCH CONFIGURATION tamil ALTER MAPPING
    FOR word WITH simple;

-- Grant usage to application role
GRANT USAGE ON ALL SCHEMAS IN DATABASE avgcxr_portal TO avgcxr_app;
GRANT CREATE ON SCHEMA public TO avgcxr_app;

-- Log extension creation
DO $$
BEGIN
    RAISE NOTICE 'AVGC-XR Portal extensions initialized successfully';
END $$;
