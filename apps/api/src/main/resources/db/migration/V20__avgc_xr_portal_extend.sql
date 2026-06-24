-- V17: Performance indexes for high-traffic queries
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_applications_scheme_status ON applications(scheme_id, status);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_applications_applicant ON applications(applicant_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_applications_submitted ON applications(submission_date DESC);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_applications_district_status ON applications(district, status);

-- Partial indexes for common query patterns
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_applications_pending ON applications(submission_date) WHERE status = 'SUBMITTED';
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_applications_review ON applications(submission_date) WHERE status = 'UNDER_REVIEW';

-- Document type index
CREATE INDEX IF NOT EXISTS idx_documents_app ON documents(application_id, document_type_id);

-- Workflow task indexes for inbox queries
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_wt_pending ON workflow_tasks(assignee, status) WHERE status = 'PENDING';
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_wt_group ON workflow_tasks(status) WHERE assignee IS NULL;
