-- ══════════════════════════════════════════════════════════════
-- V4: Workflow Tracking Tables
-- (Flowable manages its own engine tables; these are our domain-level tracking)
-- ══════════════════════════════════════════════════════════════

CREATE TABLE workflow_instances (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    process_instance_id VARCHAR(64),
    application_id      UUID NOT NULL REFERENCES applications(id) ON DELETE CASCADE,
    current_state       VARCHAR(100) NOT NULL,
    assignee            VARCHAR(200),
    due_date            TIMESTAMPTZ,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_workflow_process ON workflow_instances (process_instance_id);
CREATE INDEX IF NOT EXISTS idx_workflow_app ON workflow_instances (application_id);
CREATE INDEX IF NOT EXISTS idx_workflow_state ON workflow_instances (current_state);
CREATE INDEX IF NOT EXISTS idx_workflow_assignee ON workflow_instances (assignee);

CREATE TABLE workflow_history (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workflow_instance_id UUID NOT NULL REFERENCES workflow_instances(id) ON DELETE CASCADE,
    from_state          VARCHAR(100),
    to_state            VARCHAR(100) NOT NULL,
    action              VARCHAR(100) NOT NULL,
    actor               VARCHAR(200) NOT NULL,
    comment             TEXT,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_wf_history_instance ON workflow_history (workflow_instance_id);
CREATE INDEX IF NOT EXISTS idx_wf_history_created ON workflow_history (created_at DESC);
