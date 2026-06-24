-- V7: Analytics and reporting tables
CREATE TABLE IF NOT EXISTS dashboard_snapshots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    snapshot_date DATE NOT NULL DEFAULT CURRENT_DATE,
    total_applications BIGINT DEFAULT 0,
    pending_reviews BIGINT DEFAULT 0,
    approved_today BIGINT DEFAULT 0,
    total_disbursed DECIMAL(15,2) DEFAULT 0,
    applications_by_status JSONB DEFAULT '{}',
    applications_by_district JSONB DEFAULT '{}',
    applications_by_sub_sector JSONB DEFAULT '{}',
    created_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_ds_date ON dashboard_snapshots(snapshot_date);
