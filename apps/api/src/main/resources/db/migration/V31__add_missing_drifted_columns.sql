-- ============================================================================
-- V31: Add columns the JPA entities map but the live (V1-era) tables lack.
-- Same root cause as V30: tables created by the initial schema were never
-- reconciled because later CREATE TABLE IF NOT EXISTS migrations no-op'd.
-- ============================================================================

-- notifications: NotificationEntity maps updated_at, but the live table only
-- has created_at -> SELECT ... updated_at FROM notifications threw 42703.
ALTER TABLE notifications ADD COLUMN IF NOT EXISTS updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW();
