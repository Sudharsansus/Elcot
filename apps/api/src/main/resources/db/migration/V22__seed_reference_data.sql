-- ══════════════════════════════════════════════════════════════
-- V22: Seed reference data — TN districts, AVGC-XR subspecialties, admin user
-- ══════════════════════════════════════════════════════════════
-- This migration creates dedicated `districts` and `subspecialties` tables
-- (matching glm-fix-prompt.md Task 4.4 spec) and seeds them with
-- canonical Tamil Nadu reference data. The existing `reference_data`
-- table (seeded in V11/V18) remains the operational source for these
-- lookups; the dedicated tables here provide a typed, ergonomic API
-- for app code that prefers per-category tables.
-- ══════════════════════════════════════════════════════════════

-- ─── Districts (dedicated table) ─────────────────────────────────
CREATE TABLE IF NOT EXISTS districts (
    code      VARCHAR(10) PRIMARY KEY,
    name_en   VARCHAR(100) NOT NULL,
    name_ta   VARCHAR(200) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

INSERT INTO districts (code, name_en, name_ta) VALUES
    ('CHE', 'Chennai', 'சென்னை'),
    ('CBE', 'Coimbatore', 'கோயம்புத்தூர்'),
    ('MDU', 'Madurai', 'மதுரை'),
    ('TRY', 'Tiruchirappalli', 'திருச்சிராப்பள்ளி'),
    ('SLM', 'Salem', 'சேலம்'),
    ('TPR', 'Tiruppur', 'திருப்பூர்'),
    ('VLR', 'Vellore', 'வேலூர்'),
    ('KRR', 'Karur', 'கரூர்'),
    ('NGL', 'Nagercoil', 'நாகர்கோயில்'),
    ('DPG', 'Dindigul', 'திண்டுக்கல்'),
    ('ERD', 'Erode', 'ஈரோடு'),
    ('VEL', 'Vellore', 'வேலூர்'),
    ('THJ', 'Thanjavur', 'தஞ்சாவூர்'),
    ('TNV', 'Tirunelveli', 'திருநெல்வேலி'),
    ('KKK', 'Kanyakumari', 'கன்னியாகுமரி')
ON CONFLICT (code) DO NOTHING;

-- ─── AVGC-XR Subspecialties (dedicated table) ────────────────────
CREATE TABLE IF NOT EXISTS subspecialties (
    code      VARCHAR(10) PRIMARY KEY,
    name_en   VARCHAR(100) NOT NULL,
    name_ta   VARCHAR(200) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

INSERT INTO subspecialties (code, name_en, name_ta) VALUES
    ('ANIM', 'Animation', 'அனிமேஷன்'),
    ('VFX',  'Visual Effects', 'விஷுவல் எஃபெக்ட்ஸ்'),
    ('GAME', 'Gaming', 'கேமிங்'),
    ('COMX', 'Comics', 'காமிக்ஸ்'),
    ('AR',   'Augmented Reality', 'ஆக்மெண்டட் ரியாலிட்டி'),
    ('VR',   'Virtual Reality', 'விர்ச்சுவல் ரியாலிட்டி'),
    ('MR',   'Mixed Reality', 'மிக்ஸ்டு ரியாலிட்டி'),
    ('META', 'Metaverse', 'மெட்டாவர்ஸ்')
ON CONFLICT (code) DO NOTHING;

-- ─── Default admin user (idempotent; V11__seed_data.sql may already exist) ────
-- Password hash is BCrypt of "Admin@TNAVGC2026" (strength 12).
-- If the row already exists (e.g. V11 seeded it), this is a no-op.
INSERT INTO users (id, email, password_hash, full_name, tamil_name, phone, is_verified, is_active)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'admin@elcot.tn.gov.in',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj/RqHpwQ5Ku',
    'ELCOT Administrator',
    'ELCOT நிர்வாகி',
    '+91-44-22200001',
    TRUE,
    TRUE
)
ON CONFLICT (id) DO NOTHING;

-- Ensure the admin user has all staff roles (idempotent)
-- The `roles` table is seeded by V11__seed_data.sql indirectly via the
-- SUPER_ADMIN role lookup; we use a SELECT FROM roles to avoid hardcoding
-- role IDs, and ON CONFLICT to make this safe to re-run.
INSERT INTO user_roles (user_id, role_id)
SELECT '00000000-0000-0000-0000-000000000001', r.id
FROM roles r
WHERE r.name IN ('SUPER_ADMIN', 'ELCOT_ADMIN', 'PUBLIC')
ON CONFLICT (user_id, role_id) DO NOTHING;
