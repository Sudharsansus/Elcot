-- ═══════════════════════════════════════════════════════════════════════════
-- V35: AVGC-XR Bridge — make the directory functional
-- ═══════════════════════════════════════════════════════════════════════════
-- The V6 tables business_connect / talent_connect / freelancer_registry now
-- have real JPA entities (in.elcot.avgcxr.ecosystem.bridge). This migration:
--   1. Widens personal-contact PII columns to text so they can be stored
--      AES-256-GCM encrypted at rest (§IV.8) via EncryptedStringConverter.
--   2. Seeds a small set of CLEARLY ILLUSTRATIVE demo entries so the public
--      directory is not empty during the bid demo. These are placeholders —
--      replace/remove before production (see the DELETE block at the end,
--      commented out, for easy cleanup).
-- ───────────────────────────────────────────────────────────────────────────

-- ─── 1. Widen PII columns for encryption ────────────────────────────────────
ALTER TABLE business_connect ALTER COLUMN contact_email TYPE text;
ALTER TABLE business_connect ALTER COLUMN contact_phone TYPE text;
ALTER TABLE talent_connect   ALTER COLUMN email         TYPE text;
ALTER TABLE talent_connect   ALTER COLUMN mobile_number TYPE text;
ALTER TABLE freelancer_registry ALTER COLUMN email         TYPE text;
ALTER TABLE freelancer_registry ALTER COLUMN mobile_number TYPE text;

-- ─── 2. Demo seed (illustrative placeholders) ───────────────────────────────
-- Business Connect
INSERT INTO business_connect
    (id, company_name, company_name_tamil, sub_sector, entity_type, employee_count,
     turnover, district, contact_person, contact_email, contact_phone, website_url, status)
VALUES
    (gen_random_uuid(), 'Marina Animation Studios (Demo)', 'மெரினா அனிமேஷன் ஸ்டுடியோ', 'ANIM',
     'PRIVATE_LIMITED', 48, 35000000, 'Chennai', 'Studio Lead', 'hello@marina-demo.example',
     '+91-44-40000001', 'https://example.com/marina-demo', 'ACTIVE'),
    (gen_random_uuid(), 'Kovai VFX Labs (Demo)', 'கோவை விஎஃப்எக்ஸ் லேப்ஸ்', 'VFX',
     'LLP', 26, 18000000, 'Coimbatore', 'Studio Lead', 'hello@kovaivfx-demo.example',
     '+91-422-4000002', 'https://example.com/kovaivfx-demo', 'ACTIVE'),
    (gen_random_uuid(), 'Madurai GameWorks (Demo)', 'மதுரை கேம்வொர்க்ஸ்', 'GAME',
     'PRIVATE_LIMITED', 60, 52000000, 'Madurai', 'Studio Lead', 'hello@madurai-games-demo.example',
     '+91-452-4000003', 'https://example.com/madurai-games-demo', 'ACTIVE'),
    (gen_random_uuid(), 'Salem XR Collective (Demo)', 'சேலம் எக்ஸ்ஆர் கலெக்டிவ்', 'VR',
     'PARTNERSHIP', 14, 9000000, 'Salem', 'Studio Lead', 'hello@salemxr-demo.example',
     '+91-427-4000004', 'https://example.com/salemxr-demo', 'ACTIVE');

-- Talent Connect
INSERT INTO talent_connect
    (id, full_name, full_name_tamil, email, mobile_number, skills, experience_years,
     sub_sector, portfolio_url, district, status)
VALUES
    (gen_random_uuid(), 'Demo Animator A', 'டெமோ அனிமேட்டர் A', 'talentA-demo@example.com',
     '+91-90000-00001', ARRAY['Maya','Blender','Rigging'], 5, 'ANIM',
     'https://example.com/portfolio/a-demo', 'Chennai', 'ACTIVE'),
    (gen_random_uuid(), 'Demo Compositor B', 'டெமோ காம்போசிட்டர் B', 'talentB-demo@example.com',
     '+91-90000-00002', ARRAY['Nuke','After Effects'], 7, 'VFX',
     'https://example.com/portfolio/b-demo', 'Coimbatore', 'ACTIVE'),
    (gen_random_uuid(), 'Demo Game Designer C', 'டெமோ கேம் டிசைனர் C', 'talentC-demo@example.com',
     '+91-90000-00003', ARRAY['Unity','C#','Level Design'], 4, 'GAME',
     'https://example.com/portfolio/c-demo', 'Madurai', 'ACTIVE'),
    (gen_random_uuid(), 'Demo XR Developer D', 'டெமோ எக்ஸ்ஆர் டெவலப்பர் D', 'talentD-demo@example.com',
     '+91-90000-00004', ARRAY['Unreal','C++','AR Foundation'], 6, 'AR',
     'https://example.com/portfolio/d-demo', 'Tiruchirappalli', 'ACTIVE');

-- Freelancer Registry
INSERT INTO freelancer_registry
    (id, full_name, full_name_tamil, email, mobile_number, specialization, skills,
     hourly_rate, availability_status, district, status)
VALUES
    (gen_random_uuid(), 'Demo Freelancer E', 'டெமோ ஃப்ரீலான்சர் E', 'freelancerE-demo@example.com',
     '+91-90000-10001', 'Character Animation', ARRAY['Maya','Spline'], 1200, 'AVAILABLE', 'Chennai', 'ACTIVE'),
    (gen_random_uuid(), 'Demo Freelancer F', 'டெமோ ஃப்ரீலான்சர் F', 'freelancerF-demo@example.com',
     '+91-90000-10002', 'Concept Art', ARRAY['Photoshop','Procreate'], 900, 'AVAILABLE', 'Erode', 'ACTIVE'),
    (gen_random_uuid(), 'Demo Freelancer G', 'டெமோ ஃப்ரீலான்சர் G', 'freelancerG-demo@example.com',
     '+91-90000-10003', 'Motion Capture', ARRAY['Vicon','MotionBuilder'], 1500, 'BUSY', 'Tiruppur', 'ACTIVE');

-- ─── Cleanup (uncomment to remove demo data before production) ───────────────
-- DELETE FROM business_connect    WHERE company_name LIKE '%(Demo)%';
-- DELETE FROM talent_connect      WHERE full_name LIKE 'Demo %';
-- DELETE FROM freelancer_registry WHERE full_name LIKE 'Demo %';
