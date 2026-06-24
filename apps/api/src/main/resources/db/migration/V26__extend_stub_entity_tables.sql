-- ─── V26: extend stub entity tables to match domain models ───
-- Audit fix HIGH-006: 14 of 27 JPA entities had only 3 fields.
-- This migration extends the underlying tables with the columns that
-- the application domain models expect.

-- District statistics (ReportData) — add district / period / metric / value
ALTER TABLE district_statistics
    ADD COLUMN IF NOT EXISTS district VARCHAR(100),
    ADD COLUMN IF NOT EXISTS period VARCHAR(50),
    ADD COLUMN IF NOT EXISTS metric VARCHAR(100),
    ADD COLUMN IF NOT EXISTS value NUMERIC(20,2);

-- Companies — add name / description / contact columns
ALTER TABLE companies
    ADD COLUMN IF NOT EXISTS name VARCHAR(200),
    ADD COLUMN IF NOT EXISTS description TEXT,
    ADD COLUMN IF NOT EXISTS cin VARCHAR(50),
    ADD COLUMN IF NOT EXISTS gstin VARCHAR(50),
    ADD COLUMN IF NOT EXISTS district VARCHAR(100),
    ADD COLUMN IF NOT EXISTS state VARCHAR(100) DEFAULT 'Tamil Nadu',
    ADD COLUMN IF NOT EXISTS contact_email VARCHAR(255),
    ADD COLUMN IF NOT EXISTS contact_phone VARCHAR(20),
    ADD COLUMN IF NOT EXISTS website VARCHAR(500),
    ADD COLUMN IF NOT EXISTS subsector VARCHAR(50);

-- Freelancer profiles
ALTER TABLE freelancer_profiles
    ADD COLUMN IF NOT EXISTS name VARCHAR(200),
    ADD COLUMN IF NOT EXISTS description TEXT,
    ADD COLUMN IF NOT EXISTS user_id UUID,
    ADD COLUMN IF NOT EXISTS skills TEXT,
    ADD COLUMN IF NOT EXISTS hourly_rate NUMERIC(10,2),
    ADD COLUMN IF NOT EXISTS district VARCHAR(100),
    ADD COLUMN IF NOT EXISTS available BOOLEAN DEFAULT true;

-- Talent profiles
ALTER TABLE talent_profiles
    ADD COLUMN IF NOT EXISTS name VARCHAR(200),
    ADD COLUMN IF NOT EXISTS description TEXT,
    ADD COLUMN IF NOT EXISTS user_id UUID,
    ADD COLUMN IF NOT EXISTS category VARCHAR(100),
    ADD COLUMN IF NOT EXISTS experience_years INTEGER,
    ADD COLUMN IF NOT EXISTS portfolio_url VARCHAR(500);

-- Audit logs (already has many columns; ensure they exist)
ALTER TABLE audit_logs
    ADD COLUMN IF NOT EXISTS action VARCHAR(100),
    ADD COLUMN IF NOT EXISTS entity_type VARCHAR(100),
    ADD COLUMN IF NOT EXISTS entity_id VARCHAR(100),
    ADD COLUMN IF NOT EXISTS actor_id VARCHAR(100),
    ADD COLUMN IF NOT EXISTS actor_type VARCHAR(50) DEFAULT 'USER',
    ADD COLUMN IF NOT EXISTS old_value JSONB,
    ADD COLUMN IF NOT EXISTS new_value JSONB,
    ADD COLUMN IF NOT EXISTS ip_address VARCHAR(50),
    ADD COLUMN IF NOT EXISTS user_agent VARCHAR(500);

-- File metadata
ALTER TABLE file_metadata
    ADD COLUMN IF NOT EXISTS name VARCHAR(255),
    ADD COLUMN IF NOT EXISTS mime_type VARCHAR(100),
    ADD COLUMN IF NOT EXISTS size_bytes BIGINT,
    ADD COLUMN IF NOT EXISTS checksum VARCHAR(100),
    ADD COLUMN IF NOT EXISTS uploaded_by VARCHAR(40),
    ADD COLUMN IF NOT EXISTS entity_type VARCHAR(50),
    ADD COLUMN IF NOT EXISTS entity_id VARCHAR(40),
    ADD COLUMN IF NOT EXISTS storage_key VARCHAR(500);

-- Notifications
ALTER TABLE notifications
    ADD COLUMN IF NOT EXISTS user_id UUID,
    ADD COLUMN IF NOT EXISTS type VARCHAR(50),
    ADD COLUMN IF NOT EXISTS title VARCHAR(255),
    ADD COLUMN IF NOT EXISTS body TEXT,
    ADD COLUMN IF NOT EXISTS channel VARCHAR(20) DEFAULT 'IN_APP',
    ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'PENDING',
    ADD COLUMN IF NOT EXISTS read_at TIMESTAMPTZ;

-- Search results
ALTER TABLE search_results
    ADD COLUMN IF NOT EXISTS title VARCHAR(500),
    ADD COLUMN IF NOT EXISTS description TEXT,
    ADD COLUMN IF NOT EXISTS url VARCHAR(500),
    ADD COLUMN IF NOT EXISTS score NUMERIC(5,2),
    ADD COLUMN IF NOT EXISTS type VARCHAR(50),
    ADD COLUMN IF NOT EXISTS entity_id VARCHAR(100);

-- User profiles
ALTER TABLE user_profiles
    ADD COLUMN IF NOT EXISTS user_id UUID,
    ADD COLUMN IF NOT EXISTS date_of_birth DATE,
    ADD COLUMN IF NOT EXISTS gender VARCHAR(10),
    ADD COLUMN IF NOT EXISTS address TEXT,
    ADD COLUMN IF NOT EXISTS district VARCHAR(100),
    ADD COLUMN IF NOT EXISTS state VARCHAR(100) DEFAULT 'Tamil Nadu',
    ADD COLUMN IF NOT EXISTS pincode VARCHAR(10);

-- Application documents
ALTER TABLE application_documents
    ADD COLUMN IF NOT EXISTS name VARCHAR(200),
    ADD COLUMN IF NOT EXISTS description TEXT,
    ADD COLUMN IF NOT EXISTS document_type VARCHAR(50),
    ADD COLUMN IF NOT EXISTS file_url VARCHAR(500),
    ADD COLUMN IF NOT EXISTS application_id UUID;

-- Workflow instances (ensure all expected columns exist)
ALTER TABLE workflow_instances
    ADD COLUMN IF NOT EXISTS process_definition_id VARCHAR(255),
    ADD COLUMN IF NOT EXISTS process_definition_key VARCHAR(100),
    ADD COLUMN IF NOT EXISTS business_key VARCHAR(255),
    ADD COLUMN IF NOT EXISTS status VARCHAR(30) DEFAULT 'RUNNING',
    ADD COLUMN IF NOT EXISTS started_by UUID,
    ADD COLUMN IF NOT EXISTS started_at TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS completed_at TIMESTAMPTZ,
    ADD COLUMN IF NOT EXISTS variables JSONB DEFAULT '{}';

-- Grievances
ALTER TABLE grievances
    ADD COLUMN IF NOT EXISTS grievance_number VARCHAR(50),
    ADD COLUMN IF NOT EXISTS subject VARCHAR(500),
    ADD COLUMN IF NOT EXISTS description TEXT,
    ADD COLUMN IF NOT EXISTS category VARCHAR(50),
    ADD COLUMN IF NOT EXISTS application_id UUID,
    ADD COLUMN IF NOT EXISTS raised_by UUID,
    ADD COLUMN IF NOT EXISTS assigned_to UUID,
    ADD COLUMN IF NOT EXISTS status VARCHAR(30) DEFAULT 'PENDING',
    ADD COLUMN IF NOT EXISTS priority VARCHAR(20) DEFAULT 'MEDIUM',
    ADD COLUMN IF NOT EXISTS resolution TEXT,
    ADD COLUMN IF NOT EXISTS escalation_level INTEGER DEFAULT 0;

-- Helpdesk tickets
ALTER TABLE helpdesk_tickets
    ADD COLUMN IF NOT EXISTS ticket_number VARCHAR(50),
    ADD COLUMN IF NOT EXISTS subject VARCHAR(500),
    ADD COLUMN IF NOT EXISTS description TEXT,
    ADD COLUMN IF NOT EXISTS category VARCHAR(50),
    ADD COLUMN IF NOT EXISTS priority VARCHAR(20) DEFAULT 'MEDIUM',
    ADD COLUMN IF NOT EXISTS status VARCHAR(30) DEFAULT 'OPEN',
    ADD COLUMN IF NOT EXISTS created_by UUID,
    ADD COLUMN IF NOT EXISTS assigned_to UUID,
    ADD COLUMN IF NOT EXISTS resolution TEXT,
    ADD COLUMN IF NOT EXISTS rating INTEGER;

COMMENT ON TABLE companies IS 'V26 audit fix: extended columns for Company entity';
COMMENT ON TABLE freelancer_profiles IS 'V26 audit fix: extended columns for FreelancerProfile entity';
COMMENT ON TABLE talent_profiles IS 'V26 audit fix: extended columns for TalentProfile entity';
COMMENT ON TABLE audit_logs IS 'V26 audit fix: extended columns for AuditLog entity';
COMMENT ON TABLE file_metadata IS 'V26 audit fix: extended columns for FileMetadata entity';
COMMENT ON TABLE notifications IS 'V26 audit fix: extended columns for Notification entity';
COMMENT ON TABLE search_results IS 'V26 audit fix: extended columns for SearchResult entity';
COMMENT ON TABLE user_profiles IS 'V26 audit fix: extended columns for UserProfile entity';
COMMENT ON TABLE application_documents IS 'V26 audit fix: extended columns for Document entity';
COMMENT ON TABLE workflow_instances IS 'V26 audit fix: extended columns for WorkflowInstance entity';
COMMENT ON TABLE grievances IS 'V26 audit fix: extended columns for Grievance entity';
COMMENT ON TABLE helpdesk_tickets IS 'V26 audit fix: extended columns for HelpdeskTicket entity';
