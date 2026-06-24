-- ══════════════════════════════════════════════════════════════
-- V23: Create missing tables for entities that lack migrations
-- ══════════════════════════════════════════════════════════════
-- This migration creates tables for all JPA entities that have no
-- corresponding CREATE TABLE in earlier migrations. Fixes the
-- "13 entities have no migration" gap.
--
-- These tables are created to match the JPA entity definitions.
-- The columns are derived from each entity's @Column annotations
-- and Java field names (snake_case via SpringPhysicalNamingStrategy).

-- ─── businessconnects (BusinessconnectEntity) ─────────────────
CREATE TABLE IF NOT EXISTS businessconnects (
    id              VARCHAR(40) PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    description     TEXT,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_businessconnects_name ON businessconnects(name);

-- ─── companies (CompanyEntity) ───────────────────────────────
CREATE TABLE IF NOT EXISTS companies (
    id              VARCHAR(40) PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    description     TEXT,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_companies_name ON companies(name);

-- ─── freelancer_profiles (FreelancerProfileEntity) ──────────
CREATE TABLE IF NOT EXISTS freelancer_profiles (
    id              VARCHAR(40) PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    description     TEXT,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);

-- ─── freelancerregistrys (FreelancerregistryEntity) ─────────
CREATE TABLE IF NOT EXISTS freelancerregistrys (
    id              VARCHAR(40) PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    description     TEXT,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);

-- ─── talent_profiles (TalentProfileEntity) ───────────────────
CREATE TABLE IF NOT EXISTS talent_profiles (
    id              VARCHAR(40) PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    description     TEXT,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);

-- ─── talentconnects (TalentconnectEntity) ────────────────────
CREATE TABLE IF NOT EXISTS talentconnects (
    id              VARCHAR(40) PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    description     TEXT,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);

-- ─── audits (AuditEntity) ────────────────────────────────────
CREATE TABLE IF NOT EXISTS audits (
    id              VARCHAR(40) PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    description     TEXT,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);

-- ─── auth_tokens (AuthUserEntity / AuthEntity) ───────────────
CREATE TABLE IF NOT EXISTS auth_tokens (
    id              VARCHAR(40) PRIMARY KEY,
    email           VARCHAR(200) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    refresh_token   VARCHAR(500),
    expires_at      TIMESTAMPTZ,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_auth_tokens_email ON auth_tokens(email);

-- ─── daily_statistics (DashboardDataEntity) ──────────────────
CREATE TABLE IF NOT EXISTS daily_statistics (
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ NOT NULL
);

-- ─── application_documents (DocumentEntity) ──────────────────
CREATE TABLE IF NOT EXISTS application_documents (
    id              VARCHAR(40) PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    description     TEXT,
    document_type   VARCHAR(50),
    file_url        VARCHAR(500),
    application_id  UUID,
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_application_documents_app_id ON application_documents(application_id);

-- ─── files (FileEntity) ───────────────────────────────────────
CREATE TABLE IF NOT EXISTS files (
    id              VARCHAR(40) PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    content_type    VARCHAR(100),
    size_bytes      BIGINT,
    storage_key     VARCHAR(500),
    bucket_name     VARCHAR(100),
    uploaded_by     VARCHAR(40),
    entity_type     VARCHAR(50),
    entity_id       VARCHAR(40),
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_files_entity ON files(entity_type, entity_id);

-- ─── file_metadata (FileMetadataEntity) ──────────────────────
CREATE TABLE IF NOT EXISTS file_metadata (
    id              VARCHAR(40) PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    mime_type       VARCHAR(100),
    size_bytes      BIGINT,
    checksum        VARCHAR(100),
    uploaded_by     VARCHAR(40),
    entity_type     VARCHAR(50),
    entity_id       VARCHAR(40),
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);

-- ─── district_statistics (ReportDataEntity) ──────────────────
CREATE TABLE IF NOT EXISTS district_statistics (
    id          UUID PRIMARY KEY,
    district    VARCHAR(100) NOT NULL,
    period      VARCHAR(50),
    metric      VARCHAR(100),
    value       NUMERIC(20,2),
    created_at  TIMESTAMPTZ,
    updated_at  TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_district_statistics_district ON district_statistics(district);

-- ─── searchs (SearchEntity) ───────────────────────────────────
CREATE TABLE IF NOT EXISTS searchs (
    id          VARCHAR(40) PRIMARY KEY,
    query       TEXT,
    result_count INT,
    user_id     VARCHAR(40),
    created_at  TIMESTAMPTZ
);
CREATE INDEX IF NOT EXISTS idx_searchs_user ON searchs(user_id);

-- ─── search_results (SearchResultEntity) ─────────────────────
CREATE TABLE IF NOT EXISTS search_results (
    id              VARCHAR(40) PRIMARY KEY,
    title           VARCHAR(500) NOT NULL,
    description     TEXT,
    url             VARCHAR(500),
    score           NUMERIC(5,2),
    type            VARCHAR(50),
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);

-- ─── helpdesks (HelpdeskEntity) ───────────────────────────────
CREATE TABLE IF NOT EXISTS helpdesks (
    id              VARCHAR(40) PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    description     TEXT,
    contact_email   VARCHAR(200),
    contact_phone   VARCHAR(20),
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);

-- ─── workflows (WorkflowEntity) ──────────────────────────────
CREATE TABLE IF NOT EXISTS workflows (
    id              VARCHAR(40) PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    description     TEXT,
    bpmn_process_id VARCHAR(100),
    created_by      VARCHAR(40),
    created_at      TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ
);

-- ─── Extend users with phone/tamil_name if missing ───────────
ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(20);
ALTER TABLE users ADD COLUMN IF NOT EXISTS tamil_name VARCHAR(200);
ALTER TABLE users ADD COLUMN IF NOT EXISTS is_verified BOOLEAN DEFAULT FALSE;
ALTER TABLE users ADD COLUMN IF NOT EXISTS is_active BOOLEAN DEFAULT TRUE;
ALTER TABLE users ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE;
ALTER TABLE users ADD COLUMN IF NOT EXISTS verified BOOLEAN DEFAULT FALSE;

-- ─── Extend schemes with active flag ─────────────────────────
ALTER TABLE schemes ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE;

-- ─── Extend applications with audit + funding columns ────────
ALTER TABLE applications ADD COLUMN IF NOT EXISTS submitted_at TIMESTAMPTZ;
ALTER TABLE applications ADD COLUMN IF NOT EXISTS reviewed_at TIMESTAMPTZ;
ALTER TABLE applications ADD COLUMN IF NOT EXISTS approved_at TIMESTAMPTZ;
ALTER TABLE applications ADD COLUMN IF NOT EXISTS rejected_at TIMESTAMPTZ;
ALTER TABLE applications ADD COLUMN IF NOT EXISTS funding_approved NUMERIC(15,2);
