-- V13: Audit log table (immutable append-only)
CREATE TABLE IF NOT EXISTS audit_log (
    id UUID DEFAULT gen_random_uuid(), actor_id UUID, actor_username VARCHAR(100),
    action VARCHAR(100) NOT NULL, entity_type VARCHAR(100) NOT NULL, entity_id VARCHAR(255) NOT NULL,
    old_values JSONB, new_values JSONB, ip_address INET, user_agent TEXT,
    request_id VARCHAR(50), created_at TIMESTAMPTZ DEFAULT NOW(),
    -- a partitioned table's PK must include the partition key (created_at)
    PRIMARY KEY (id, created_at)
) PARTITION BY RANGE (created_at);

CREATE TABLE audit_log_2026_h1 PARTITION OF audit_log FOR VALUES FROM ('2026-01-01') TO ('2026-07-01');
CREATE TABLE audit_log_2026_h2 PARTITION OF audit_log FOR VALUES FROM ('2026-07-01') TO ('2027-01-01');
CREATE TABLE audit_log_2027_h1 PARTITION OF audit_log FOR VALUES FROM ('2027-01-01') TO ('2027-07-01');

CREATE INDEX IF NOT EXISTS idx_audit_entity ON audit_log(entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_audit_actor ON audit_log(actor_id, created_at);
CREATE INDEX IF NOT EXISTS idx_audit_action ON audit_log(action, created_at);
