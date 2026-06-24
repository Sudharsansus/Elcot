-- V10: Reference data and notification tables
CREATE TABLE IF NOT EXISTS reference_data (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category VARCHAR(50) NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    name_tamil VARCHAR(300),
    parent_code VARCHAR(50),
    sort_order INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    UNIQUE(category, code)
);
CREATE INDEX IF NOT EXISTS idx_ref_category ON reference_data(category);

CREATE TABLE IF NOT EXISTS notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), user_id UUID REFERENCES users(id) NOT NULL,
    type VARCHAR(50) NOT NULL, title VARCHAR(255) NOT NULL, title_tamil VARCHAR(300),
    body TEXT NOT NULL, body_tamil TEXT, channel VARCHAR(20) DEFAULT 'IN_APP',
    status VARCHAR(20) DEFAULT 'PENDING', read_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_notif_user ON notifications(user_id, status);
