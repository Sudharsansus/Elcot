-- ══════════════════════════════════════════════════════════════
-- V1: Core Schema — Users, Roles, Permissions
-- ══════════════════════════════════════════════════════════════

-- Enable required extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";
CREATE EXTENSION IF NOT EXISTS "btree_gin";

-- ─── Roles ──────────────────────────────────────────────
CREATE TABLE roles (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ─── Permissions ────────────────────────────────────────
CREATE TABLE permissions (
    id          UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ─── Role-Permission Mapping ────────────────────────────
CREATE TABLE role_permissions (
    role_id       UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id UUID NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- ─── Users ──────────────────────────────────────────────
CREATE TABLE users (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email           VARCHAR(255) NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    phone           VARCHAR(20),
    full_name       VARCHAR(200) NOT NULL,
    tamil_name      VARCHAR(200),
    is_verified     BOOLEAN NOT NULL DEFAULT FALSE,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    last_login_at   TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);
CREATE INDEX IF NOT EXISTS idx_users_phone ON users (phone) WHERE phone IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_users_is_active ON users (is_active);

-- ─── User-Role Mapping ──────────────────────────────────
CREATE TABLE user_roles (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    assigned_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_id, role_id)
);

CREATE INDEX IF NOT EXISTS idx_user_roles_user ON user_roles (user_id);
CREATE INDEX IF NOT EXISTS idx_user_roles_role ON user_roles (role_id);

-- ─── User Profiles ──────────────────────────────────────
CREATE TABLE user_profiles (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    date_of_birth   DATE,
    gender          VARCHAR(10),
    address         TEXT,
    district        VARCHAR(100),
    state           VARCHAR(100) DEFAULT 'Tamil Nadu',
    pincode         VARCHAR(10),
    aadhaar_hash    VARCHAR(128),
    pan_number      VARCHAR(20),
    company_name    VARCHAR(300),
    gst_number      VARCHAR(20),
    designation     VARCHAR(200),
    experience_years INTEGER DEFAULT 0,
    skills          JSONB DEFAULT '[]'::jsonb,
    avatar_url      TEXT,
    bio             TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_user_profiles_user UNIQUE (user_id)
);

CREATE INDEX IF NOT EXISTS idx_user_profiles_district ON user_profiles (district);

-- ─── Seed Roles ─────────────────────────────────────────
INSERT INTO roles (name, description) VALUES
    ('SUPER_ADMIN', 'Full system access — ELCOT IT administrators'),
    ('ADMIN', 'Portal administration — manage schemes, applications, users'),
    ('DISTRICT_OFFICER', 'District-level scheme approval and review'),
    ('NODAL_OFFICER', 'State-level scheme approval and fund release'),
    ('APPLICANT', 'Registered user who can apply for schemes'),
    ('PUBLIC', 'Unauthenticated or read-only access');

-- ─── Seed Permissions ───────────────────────────────────
INSERT INTO permissions (name, description) VALUES
    ('SCHEME_READ', 'View scheme details'),
    ('SCHEME_WRITE', 'Create and modify schemes'),
    ('APPLICATION_READ', 'View applications'),
    ('APPLICATION_REVIEW', 'Review and comment on applications'),
    ('APPLICATION_APPROVE', 'Approve or reject applications'),
    ('USER_MANAGE', 'Manage user accounts and roles'),
    ('WORKFLOW_MANAGE', 'Manage BPMN workflows and task assignments'),
    ('REPORT_VIEW', 'View analytics and MIS reports'),
    ('ADMIN_FULL', 'Full administrative access'),
    ('ECOSYSTEM_MANAGE', 'Manage company, talent, and freelancer directories');

-- ─── Seed Role-Permission Mapping ───────────────────────
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p
WHERE r.name = 'SUPER_ADMIN';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p
WHERE r.name = 'ADMIN' AND p.name IN (
    'SCHEME_READ','SCHEME_WRITE','APPLICATION_READ','APPLICATION_REVIEW',
    'APPLICATION_APPROVE','USER_MANAGE','WORKFLOW_MANAGE','REPORT_VIEW',
    'ECOSYSTEM_MANAGE'
);

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p
WHERE r.name = 'DISTRICT_OFFICER' AND p.name IN (
    'APPLICATION_READ','APPLICATION_REVIEW','APPLICATION_APPROVE','REPORT_VIEW'
);

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p
WHERE r.name = 'NODAL_OFFICER' AND p.name IN (
    'APPLICATION_READ','APPLICATION_REVIEW','APPLICATION_APPROVE','REPORT_VIEW',
    'WORKFLOW_MANAGE'
);

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p
WHERE r.name = 'APPLICANT' AND p.name IN (
    'SCHEME_READ','APPLICATION_READ'
);

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p
WHERE r.name = 'PUBLIC' AND p.name IN ('SCHEME_READ');
