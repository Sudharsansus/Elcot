-- V11: Support tables (helpdesk, grievances)
CREATE TABLE IF NOT EXISTS helpdesk_tickets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), ticket_number VARCHAR(50) UNIQUE NOT NULL,
    subject VARCHAR(500) NOT NULL, description TEXT, category VARCHAR(50), priority VARCHAR(20) DEFAULT 'MEDIUM',
    status VARCHAR(30) DEFAULT 'OPEN', created_by UUID REFERENCES users(id),
    assigned_to UUID REFERENCES users(id), resolution TEXT, rating INTEGER,
    created_at TIMESTAMPTZ DEFAULT NOW(), updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_ht_status ON helpdesk_tickets(status);
CREATE INDEX IF NOT EXISTS idx_ht_assigned ON helpdesk_tickets(assigned_to);

CREATE TABLE IF NOT EXISTS grievances (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), grievance_number VARCHAR(50) UNIQUE NOT NULL,
    subject VARCHAR(500) NOT NULL, description TEXT, category VARCHAR(50), application_id UUID,
    raised_by UUID REFERENCES users(id), assigned_to UUID REFERENCES users(id),
    status VARCHAR(30) DEFAULT 'PENDING', priority VARCHAR(20) DEFAULT 'MEDIUM',
    resolution TEXT, escalation_level INTEGER DEFAULT 0, created_at TIMESTAMPTZ DEFAULT NOW(), updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_grv_status ON grievances(status);
CREATE INDEX IF NOT EXISTS idx_grv_raised ON grievances(raised_by);
