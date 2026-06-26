-- ─── V37: blind-index columns for email/phone (foundation only) ─────────────
-- Adds keyed-HMAC blind-index columns so that users.email / users.phone can be
-- encrypted at rest while still being looked up by exact match (tender §IV.8).
-- These columns are NULLABLE and UNUSED until the coordinated cutover described
-- in docs/security/pii-blind-index.md (set APP_BLIND_INDEX_KEY → backfill →
-- switch lookups → encrypt the value columns). Adding them now is additive and
-- changes no behaviour: existing plaintext email/phone lookups are untouched.
ALTER TABLE users ADD COLUMN IF NOT EXISTS email_bidx VARCHAR(64);
ALTER TABLE users ADD COLUMN IF NOT EXISTS phone_bidx VARCHAR(64);

CREATE INDEX IF NOT EXISTS idx_users_email_bidx ON users(email_bidx);
CREATE INDEX IF NOT EXISTS idx_users_phone_bidx ON users(phone_bidx);
