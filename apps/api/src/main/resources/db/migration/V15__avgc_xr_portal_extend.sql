-- V12: Workflow tables (Flowable BPMN engine compatibility)
CREATE TABLE IF NOT EXISTS workflow_instances (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), process_definition_id VARCHAR(255) NOT NULL,
    process_definition_key VARCHAR(100) NOT NULL, business_key VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'RUNNING', started_by UUID REFERENCES users(id),
    started_at TIMESTAMPTZ DEFAULT NOW(), completed_at TIMESTAMPTZ,
    variables JSONB DEFAULT '{}', created_at TIMESTAMPTZ DEFAULT NOW(), updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_wi_business_key ON workflow_instances(business_key);
CREATE INDEX IF NOT EXISTS idx_wi_status ON workflow_instances(status);
CREATE INDEX IF NOT EXISTS idx_wi_process_key ON workflow_instances(process_definition_key);

CREATE TABLE IF NOT EXISTS workflow_tasks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), process_instance_id UUID REFERENCES workflow_instances(id),
    task_name VARCHAR(255) NOT NULL, task_name_tamil VARCHAR(300), description TEXT,
    assignee UUID REFERENCES users(id), candidate_groups TEXT[],
    status VARCHAR(30) DEFAULT 'PENDING', priority INTEGER DEFAULT 50,
    due_date TIMESTAMPTZ, completed_at TIMESTAMPTZ, comment TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(), updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_wt_assignee ON workflow_tasks(assignee);
CREATE INDEX IF NOT EXISTS idx_wt_status ON workflow_tasks(status);
