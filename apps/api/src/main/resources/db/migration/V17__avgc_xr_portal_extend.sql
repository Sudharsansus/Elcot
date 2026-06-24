-- V14: Full-text search index for Elasticsearch sync
CREATE INDEX IF NOT EXISTS idx_schemes_search ON schemes USING gin(to_tsvector('english', name));
CREATE INDEX IF NOT EXISTS idx_applications_search ON applications USING gin(to_tsvector('english', application_number));

-- Notification tracking table
CREATE TABLE IF NOT EXISTS notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), user_id UUID REFERENCES users(id) NOT NULL,
    type VARCHAR(50) NOT NULL, title VARCHAR(255) NOT NULL, title_tamil VARCHAR(300),
    body TEXT NOT NULL, body_tamil TEXT, channel VARCHAR(20) DEFAULT 'IN_APP',
    status VARCHAR(20) DEFAULT 'PENDING', read_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_notif_user ON notifications(user_id, status);
CREATE INDEX IF NOT EXISTS idx_notif_created ON notifications(created_at DESC);
