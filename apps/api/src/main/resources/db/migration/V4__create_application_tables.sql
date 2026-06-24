-- V4: Application and document tables
CREATE TABLE IF NOT EXISTS applications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    application_number VARCHAR(50) UNIQUE NOT NULL,
    scheme_id UUID NOT NULL REFERENCES schemes(id),
    applicant_id UUID NOT NULL REFERENCES users(id),
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    current_step INTEGER DEFAULT 1,
    total_steps INTEGER DEFAULT 5,
    form_data JSONB DEFAULT '{}',
    assigned_to UUID REFERENCES users(id),
    remarks TEXT,
    rejection_reason TEXT,
    approved_amount DECIMAL(15,2),
    applicant_name VARCHAR(200),
    district VARCHAR(100),
    sub_sector VARCHAR(50),
    submission_date TIMESTAMPTZ,
    last_reminder_sent_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS documents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    application_id UUID NOT NULL REFERENCES applications(id) ON DELETE CASCADE,
    document_type_id UUID,
    file_name VARCHAR(255) NOT NULL,
    original_file_name VARCHAR(255),
    mime_type VARCHAR(100),
    file_size BIGINT,
    minio_object_id VARCHAR(500),
    status VARCHAR(30) DEFAULT 'PENDING',
    verified_by UUID REFERENCES users(id),
    verified_at TIMESTAMPTZ,
    rejection_reason TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_app_scheme ON applications(scheme_id);
CREATE INDEX IF NOT EXISTS idx_app_applicant ON applications(applicant_id);
CREATE INDEX IF NOT EXISTS idx_app_status ON applications(status);
CREATE INDEX IF NOT EXISTS idx_app_district ON applications(district);
CREATE INDEX IF NOT EXISTS idx_app_number ON applications(application_number);
CREATE INDEX IF NOT EXISTS idx_doc_application ON documents(application_id);
CREATE INDEX IF NOT EXISTS idx_doc_status ON documents(status);
