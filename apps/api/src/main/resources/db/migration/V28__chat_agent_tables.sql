-- ─── V28: AI Chat Agent (Mira) tables ──────────────────────
-- Stores chat sessions and messages for the bilingual AI chat agent
-- (English + Tamil) with RAG context. DPDP-compliant: messages
-- auto-purged after retention period (default 90 days).

CREATE TABLE IF NOT EXISTS chat_sessions (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id       UUID REFERENCES users(id) ON DELETE SET NULL,
    session_token VARCHAR(64) UNIQUE,  -- for anonymous sessions (cookie)
    title         VARCHAR(255),        -- auto-generated from first message
    language      VARCHAR(5) DEFAULT 'en',  -- 'en' or 'ta'
    message_count INTEGER NOT NULL DEFAULT 0,
    is_active     BOOLEAN NOT NULL DEFAULT true,
    metadata      JSONB DEFAULT '{}',
    created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    expires_at    TIMESTAMPTZ NOT NULL DEFAULT (NOW() + INTERVAL '90 days')
);

CREATE INDEX IF NOT EXISTS idx_chat_sessions_user_id ON chat_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_chat_sessions_token ON chat_sessions(session_token);
CREATE INDEX IF NOT EXISTS idx_chat_sessions_expires ON chat_sessions(expires_at);
CREATE INDEX IF NOT EXISTS idx_chat_sessions_active ON chat_sessions(is_active, updated_at DESC);

CREATE TABLE IF NOT EXISTS chat_messages (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id      UUID NOT NULL REFERENCES chat_sessions(id) ON DELETE CASCADE,
    role            VARCHAR(20) NOT NULL,  -- USER, ASSISTANT, SYSTEM
    content         TEXT NOT NULL,
    content_tamil   TEXT,                  -- pre-translated Tamil version
    language        VARCHAR(5) NOT NULL DEFAULT 'en',
    model_used      VARCHAR(100),          -- 'gpt-4o-mini', 'claude-haiku-4-5', etc.
    tokens_used     INTEGER,
    latency_ms      INTEGER,
    rag_context_ids JSONB DEFAULT '[]',    -- IDs of retrieved documents
    metadata        JSONB DEFAULT '{}',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_chat_messages_session_id ON chat_messages(session_id, created_at);
CREATE INDEX IF NOT EXISTS idx_chat_messages_role ON chat_messages(role);
CREATE INDEX IF NOT EXISTS idx_chat_messages_created ON chat_messages(created_at DESC);

COMMENT ON TABLE chat_sessions IS 'V28 audit fix: AI chat agent session storage (DPDP-compliant, 90-day retention)';
COMMENT ON TABLE chat_messages IS 'V28 audit fix: individual chat messages with RAG context tracking';
COMMENT ON COLUMN chat_messages.rag_context_ids IS 'Source documents retrieved for RAG grounding';
COMMENT ON COLUMN chat_sessions.expires_at IS 'Auto-purge date (DPDP data minimization)';
