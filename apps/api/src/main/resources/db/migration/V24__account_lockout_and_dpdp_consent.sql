-- ══════════════════════════════════════════════════════════════
-- V24: Account lockout + DPDP consent capture
-- ══════════════════════════════════════════════════════════════

-- ─── Account lockout columns ─────────────────────────────────
ALTER TABLE users ADD COLUMN IF NOT EXISTS failed_login_attempts INTEGER NOT NULL DEFAULT 0;
ALTER TABLE users ADD COLUMN IF NOT EXISTS locked_until TIMESTAMPTZ;
ALTER TABLE users ADD COLUMN IF NOT EXISTS last_failed_login_at TIMESTAMPTZ;
CREATE INDEX IF NOT EXISTS idx_users_locked_until ON users(locked_until);

-- ─── DPDP consent capture ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS user_consents (
    id              UUID PRIMARY KEY,
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    consent_type    VARCHAR(64) NOT NULL,    -- DPDP_DATA_PROCESSING, MARKETING_COMMUNICATIONS, etc.
    granted         BOOLEAN NOT NULL,
    policy_version  VARCHAR(32) NOT NULL,    -- e.g. "v1.0", "v2.1" — for re-consent flows
    ip_address      VARCHAR(64),
    user_agent      VARCHAR(500),
    granted_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    revoked_at      TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_user_consent UNIQUE (user_id, consent_type, policy_version)
);
CREATE INDEX IF NOT EXISTS idx_user_consents_user_id ON user_consents(user_id);
CREATE INDEX IF NOT EXISTS idx_user_consents_type ON user_consents(consent_type);
CREATE INDEX IF NOT EXISTS idx_user_consents_granted_at ON user_consents(granted_at);

COMMENT ON TABLE user_consents IS
'DPDP Act 2023 compliant consent ledger. Each row is an immutable record of one user''s consent decision for a specific consent type and policy version.';
COMMENT ON COLUMN user_consents.policy_version IS
'Version of the privacy policy the user consented to. Bumping this version requires existing users to re-consent.';
COMMENT ON COLUMN user_consents.ip_address IS
'IP address captured at consent time for non-repudiation.';