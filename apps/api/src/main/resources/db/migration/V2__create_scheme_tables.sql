-- ══════════════════════════════════════════════════════════════
-- V2: Scheme Management Tables
-- ══════════════════════════════════════════════════════════════

CREATE TYPE scheme_category AS ENUM (
    'ANIMATION', 'VFX', 'GAMING', 'COMICS', 'XR'
);

CREATE TYPE scheme_status AS ENUM (
    'DRAFT', 'PUBLISHED', 'CLOSED', 'ARCHIVED'
);

CREATE TYPE funding_type AS ENUM (
    'GRANT', 'SUBSIDY', 'LOAN', 'EQUITY', 'TAX_REBATE', 'COMBINED'
);

CREATE TABLE schemes (
    id                UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name              VARCHAR(300) NOT NULL,
    name_ta           VARCHAR(300),
    slug              VARCHAR(350) NOT NULL UNIQUE,
    description       TEXT NOT NULL,
    description_ta    TEXT,
    category          scheme_category NOT NULL,
    sub_category      VARCHAR(100),
    ministry          VARCHAR(200),
    department        VARCHAR(200),
    eligibility_criteria JSONB DEFAULT '{}'::jsonb,
    required_documents   JSONB DEFAULT '[]'::jsonb,
    funding_amount_min  DECIMAL(15,2),
    funding_amount_max  DECIMAL(15,2),
    funding_type        funding_type,
    application_start_date DATE NOT NULL,
    application_end_date   DATE NOT NULL,
    max_applicants       INTEGER,
    status             scheme_status NOT NULL DEFAULT 'DRAFT',
    is_active          BOOLEAN NOT NULL DEFAULT TRUE,
    thumbnail_url      TEXT,
    guidelines_url     TEXT,
    terms_and_conditions TEXT,
    created_by         UUID REFERENCES users(id),
    published_at       TIMESTAMPTZ,
    created_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_scheme_dates CHECK (application_start_date < application_end_date),
    CONSTRAINT chk_scheme_funding CHECK (funding_amount_min <= funding_amount_max)
);

CREATE INDEX IF NOT EXISTS idx_schemes_category ON schemes (category);
CREATE INDEX IF NOT EXISTS idx_schemes_status ON schemes (status);
CREATE INDEX IF NOT EXISTS idx_schemes_dates ON schemes (application_start_date, application_end_date);
CREATE INDEX IF NOT EXISTS idx_schemes_is_active ON schemes (is_active);
CREATE INDEX IF NOT EXISTS idx_schemes_name_trgm ON schemes USING gin (name gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_schemes_name_ta_trgm ON schemes USING gin (name_ta gin_trgm_ops);

-- Full-text search index with Tamil language support
CREATE INDEX IF NOT EXISTS idx_schemes_name_fts ON schemes USING gin (
    to_tsvector('english', coalesce(name, ''))
);
