-- V16: Seed AVGC sub-sector reference data
INSERT INTO reference_data (category, code, name, name_tamil, sort_order) VALUES
('SUB_SECTOR', 'ANIMATION', 'Animation', 'அனிமேஷன்', 1),
('SUB_SECTOR', 'VFX', 'Visual Effects', 'காட்சி விளைவுகள்', 2),
('SUB_SECTOR', 'GAMING', 'Gaming', 'கேமிங்', 3),
('SUB_SECTOR', 'COMICS', 'Comics & Graphic Novels', 'காமிக்ஸ்', 4),
('SUB_SECTOR', 'XR', 'Extended Reality', 'நீட்டிக்கப்பட்ட யதார்த்தம்', 5),
('SUB_SECTOR', 'POST_PRODUCTION', 'Post Production', 'பிந்தைய தயாரிப்பு', 6),
('SUB_SECTOR', 'DIGITAL_MARKETING', 'Digital Marketing', 'டிஜிட்டல் மார்க்கெட்டிங்', 7),
('SUB_SECTOR', 'CONTENT_CREATION', 'Content Creation', 'உள்ளடக்கம் உருவாக்கம்', 8)
ON CONFLICT (category, code) DO NOTHING;

-- Seed entity types
INSERT INTO reference_data (category, code, name, name_tamil, sort_order) VALUES
('ENTITY_TYPE', 'SOLE_PROPRIETORSHIP', 'Sole Proprietorship', 'தனிநபர் நிறுவனம்', 1),
('ENTITY_TYPE', 'PARTNERSHIP', 'Partnership', 'கூட்டாண்மை', 2),
('ENTITY_TYPE', 'LLP', 'Limited Liability Partnership', 'வரையறுத்தப்பட்ட பொறுப்பு கூட்டாண்மை', 3),
('ENTITY_TYPE', 'PRIVATE_LIMITED', 'Private Limited', 'தனியார் லிமிடெட்', 4),
('ENTITY_TYPE', 'PUBLIC_LIMITED', 'Public Limited', 'பொது லிமிடெட்', 5)
ON CONFLICT (category, code) DO NOTHING;
