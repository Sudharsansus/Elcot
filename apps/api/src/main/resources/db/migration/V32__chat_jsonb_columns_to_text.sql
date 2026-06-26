-- ─── V32: fix chat_* JSONB columns that broke every insert ──────────────────
-- ChatSessionEntity / ChatMessageEntity map metadata, rag_context_ids and
-- metadata as String. @Column(columnDefinition = "jsonb") is only a DDL hint
-- (ignored under ddl-auto: none), so at runtime Hibernate binds these String
-- fields as `character varying`. PostgreSQL rejects varchar -> jsonb without a
-- cast, so EVERY chat insert failed with:
--   column "metadata" is of type jsonb but expression is of type character varying
-- This 500'd the whole /api/v1/chat agent (including /chat/health), which is the
-- endpoint the public Mira widget calls.
--
-- These columns are opaque storage (never queried with jsonb operators), so the
-- safe, minimal fix is to align the column type to the binding: jsonb -> text.

ALTER TABLE chat_sessions ALTER COLUMN metadata DROP DEFAULT;
ALTER TABLE chat_sessions ALTER COLUMN metadata TYPE text USING metadata::text;
ALTER TABLE chat_sessions ALTER COLUMN metadata SET DEFAULT '{}';

ALTER TABLE chat_messages ALTER COLUMN rag_context_ids DROP DEFAULT;
ALTER TABLE chat_messages ALTER COLUMN rag_context_ids TYPE text USING rag_context_ids::text;
ALTER TABLE chat_messages ALTER COLUMN rag_context_ids SET DEFAULT '[]';

ALTER TABLE chat_messages ALTER COLUMN metadata DROP DEFAULT;
ALTER TABLE chat_messages ALTER COLUMN metadata TYPE text USING metadata::text;
ALTER TABLE chat_messages ALTER COLUMN metadata SET DEFAULT '{}';
