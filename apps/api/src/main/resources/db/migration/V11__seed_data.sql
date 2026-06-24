-- ══════════════════════════════════════════════════════════════
-- V10: Seed Data — Admin User, TN Districts, Sample Schemes
-- ══════════════════════════════════════════════════════════════

-- Default admin user (password: Admin@2026 — bcrypt hash)
INSERT INTO users (id, email, password_hash, full_name, tamil_name, phone, is_verified, is_active)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'admin@elcot.tn.gov.in',
    '$2a$12$LJ3m9q5F7YkHnO8v5Zz5Qex8R0K6YlJNnMmPpQqRrSsTtUuVvWwK',
    'AVGC-XR Portal Administrator',
    'AVGC-XR நுழைவு நிர்வாகி',
    '+91-44-22200001',
    TRUE,
    TRUE
);

INSERT INTO user_roles (user_id, role_id)
SELECT '00000000-0000-0000-0000-000000000001', r.id FROM roles r WHERE r.name = 'SUPER_ADMIN';

-- Tamil Nadu 38 Districts
-- NOTE: districts table does not exist in the V1 schema; the canonical
-- district reference data is seeded into the `reference_data` table
-- (category='DISTRICT') by migration V18__avgc_xr_portal_extend.sql.
-- Migration V22__seed_reference_data.sql additionally creates a dedicated
-- `districts` table with code/name_en/name_ta columns and re-seeds it.
INSERT INTO reference_data (category, code, name, name_tamil, sort_order)
SELECT 'DISTRICT', code, name_en, name_ta, ROW_NUMBER() OVER ()
FROM (VALUES
    ('AR', 'Ariyalur', 'அரியலூர்'),
    ('CL', 'Chengalpattu', 'செங்கல்பட்டு'),
    ('CH', 'Chennai', 'சென்னை'),
    ('CB', 'Coimbatore', 'கோயம்புத்தூர்'),
    ('CU', 'Cuddalore', 'கடலூர்'),
    ('DH', 'Dharmapuri', 'தர்மபுரி'),
    ('DI', 'Dindigul', 'திண்டுக்கல்'),
    ('ER', 'Erode', 'ஈரோடு'),
    ('KK', 'Kallakurichi', 'கள்ளக்குறிச்சி'),
    ('KA', 'Kanchipuram', 'காஞ்சிபுரம்'),
    ('KN', 'Kanyakumari', 'கன்னியாகுமரி'),
    ('KR', 'Karur', 'கரூர்'),
    ('KG', 'Krishnagiri', 'கிருஷ்ணகிரி'),
    ('MD', 'Madurai', 'மதுரை'),
    ('MY', 'Mayiladuthurai', 'மயிலாடுதுறை'),
    ('NG', 'Nagapattinam', 'நாகப்பட்டினம்'),
    ('NM', 'Namakkal', 'நாமக்கல்'),
    ('NL', 'Nilgiris', 'நீலகிரி'),
    ('PB', 'Perambalur', 'பெரம்பலூர்'),
    ('PK', 'Pudukkottai', 'புதுக்கோட்டை'),
    ('RM', 'Ramanathapuram', 'இராமநாதபுரம்'),
    ('RP', 'Ranipet', 'இராணிப்பேட்டை'),
    ('SL', 'Salem', 'சேலம்'),
    ('SG', 'Sivagangai', 'சிவகங்கை'),
    ('TK', 'Tenkasi', 'தேன்காசி'),
    ('TH', 'Thanjavur', 'தஞ்சாவூர்'),
    ('TE', 'Theni', 'தேனி'),
    ('TT', 'Thoothukudi', 'தூத்துக்குடி'),
    ('TC', 'Tiruchirappalli', 'திருச்சிராப்பள்ளி'),
    ('TN', 'Tirunelveli', 'திருநெல்வேலி'),
    ('TP', 'Tirupathur', 'திருப்பத்தூர்'),
    ('TV', 'Tiruvallur', 'திருவள்ளூர்'),
    ('TVM', 'Tiruvannamalai', 'திருவண்ணாமலை'),
    ('TR', 'Tiruvarur', 'திருவாரூர்'),
    ('VL', 'Vellore', 'வேலூர்'),
    ('VP', 'Viluppuram', 'விழுப்புரம்'),
    ('VN', 'Virudhunagar', 'விருதுநகர்')
) AS t(code, name_en, name_ta)
WHERE NOT EXISTS (
    SELECT 1 FROM reference_data rd WHERE rd.category = 'DISTRICT' AND rd.code = t.code
);

-- Sample Schemes (3 across categories)
INSERT INTO schemes (name, name_ta, slug, description, description_ta, category, ministry, department, funding_amount_min, funding_amount_max, funding_type, application_start_date, application_end_date, status, is_active, created_by) VALUES
(
    'Tamil Nadu Animation Startup Grant',
    'தமிழ்நாடு அனிமேஷன் ஸ்டார்ட்அப் மானியம்',
    'tn-animation-startup-grant',
    'Financial assistance for startups in the animation sector registered in Tamil Nadu. Covers equipment, software licenses, and operational costs for the first year.',
    'தமிழ்நாட்டில் பதிவு செய்யப்பட்ட அனிமேஷன் துறை ஸ்டார்ட்அப்களுக்கு நிதி உதவி. முதலாவது ஆண்டிற்கான உபகரணங்கள், மென்பொருள் உரிமங்கள் மற்றும் இயக்க செலவுகளை உள்ளடக்கியது.',
    'ANIMATION', 'Micro, Small and Medium Enterprises', 'ELCOT', 100000.00, 500000.00, 'GRANT', '2026-07-01', '2026-09-30', 'PUBLISHED', TRUE, '00000000-0000-0000-0000-000000000001'
),
(
    'VFX Production Infrastructure Support',
    'VFX தயாரிப்பு உள்கட்டமைப்பு ஆதரவு',
    'vfx-production-infrastructure-support',
    'Subsidy for setting up VFX production infrastructure including render farms, color grading suites, and motion capture studios in Tamil Nadu.',
    'தமிழ்நாட்டில் ரெண்டர் பண்ணைகள், நிற தரம் ஸ்யூட்கள் மற்றும் மோஷன் கேப்சர் ஸ்டுடியோக்கள் உட்பட VFX தயாரிப்பு உள்கட்டமைப்பை அமைப்பதற்கான மானியம்.',
    'VFX', 'Information Technology', 'ELCOT', 500000.00, 2000000.00, 'SUBSIDY', '2026-08-01', '2026-12-31', 'PUBLISHED', TRUE, '00000000-0000-0000-0000-000000000001'
),
(
    'Game Development Talent Incubation Programme',
    'கேம் டெவலப்மென்ட் திறமை இன்குபேஷன் திட்டம்',
    'game-development-talent-incubation-programme',
    '6-month incubation programme for game development teams with mentoring, workspace, and prototype funding. Open to teams of 2-5 members.',
    'வழிகாட்டுதல், பணியிடம் மற்றும் மாதிரி நிதியுடன் கேம் டெவலப்மென்ட் குழுக்களுக்கான 6-மாத இன்குபேஷன் திட்டம். 2-5 உறுப்பினர்கள் கொண்ட குழுக்களுக்கு திறந்திருக்கும்.',
    'GAMING', 'Information Technology', 'ELCOT', 200000.00, 1000000.00, 'GRANT', '2026-07-15', '2026-10-15', 'PUBLISHED', TRUE, '00000000-0000-0000-0000-000000000001'
);
