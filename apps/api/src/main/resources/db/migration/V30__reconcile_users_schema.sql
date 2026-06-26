-- ============================================================================
-- V30: Reconcile the live `users` / `user_roles` schema with the JPA entities.
--
-- The deployed users + user_roles tables were created by V1 (legacy schema:
-- phone, tamil_name, role_id FK). V3's corrected CREATE TABLE used
-- `CREATE TABLE IF NOT EXISTS` and therefore no-op'd against the already-
-- existing V1 tables, so the columns the application's UserEntity maps
-- (username, mobile_number, full_name_tamil, status, department, designation,
-- district, profile_completed) were never added — and user_roles never got its
-- plain `role` string column. Result: every register / login threw
--     ERROR: column users.department does not exist
-- and the subsequent role insert would also have failed.
--
-- This migration adds the missing columns and reconciles user_roles, with a
-- backfill for the pre-existing seed admin row. Idempotent and safe to re-run.
-- ============================================================================

-- ── users: add the columns UserEntity maps but the live table lacks ──────────
ALTER TABLE users ADD COLUMN IF NOT EXISTS username          VARCHAR(100);
ALTER TABLE users ADD COLUMN IF NOT EXISTS mobile_number     VARCHAR(20);
ALTER TABLE users ADD COLUMN IF NOT EXISTS full_name_tamil   VARCHAR(300);
ALTER TABLE users ADD COLUMN IF NOT EXISTS status            VARCHAR(30) NOT NULL DEFAULT 'PENDING_VERIFICATION';
ALTER TABLE users ADD COLUMN IF NOT EXISTS department        VARCHAR(100);
ALTER TABLE users ADD COLUMN IF NOT EXISTS designation       VARCHAR(100);
ALTER TABLE users ADD COLUMN IF NOT EXISTS district          VARCHAR(100);
ALTER TABLE users ADD COLUMN IF NOT EXISTS profile_completed BOOLEAN NOT NULL DEFAULT FALSE;

-- backfill the new columns from the legacy columns for any pre-existing rows
UPDATE users SET mobile_number   = phone                     WHERE mobile_number IS NULL AND phone IS NOT NULL;
UPDATE users SET mobile_number   = '0000000000'              WHERE mobile_number IS NULL OR mobile_number = '';
UPDATE users SET full_name_tamil = tamil_name                WHERE full_name_tamil IS NULL AND tamil_name IS NOT NULL;
UPDATE users SET username        = split_part(email, '@', 1) WHERE username IS NULL OR username = '';
-- pre-existing (seed) accounts are already active + complete
UPDATE users SET status            = 'ACTIVE' WHERE status = 'PENDING_VERIFICATION' AND is_active = TRUE;
UPDATE users SET profile_completed = TRUE     WHERE is_active = TRUE;

-- uniqueness the entity declares
CREATE UNIQUE INDEX IF NOT EXISTS ux_users_username      ON users (username);
CREATE UNIQUE INDEX IF NOT EXISTS ux_users_mobile_number ON users (mobile_number);

-- ── user_roles: UserEntity persists simple string roles via
--    @ElementCollection user_roles(user_id, role). The live table is the V1
--    shape (user_id, role_id NOT NULL [composite PK], assigned_at). ───────────
ALTER TABLE user_roles ADD COLUMN IF NOT EXISTS role VARCHAR(50);

-- the legacy composite PK (user_id, role_id) forces role_id NOT NULL, which
-- blocks the application's (user_id, role) inserts — drop it and relax role_id.
ALTER TABLE user_roles DROP CONSTRAINT IF EXISTS user_roles_pkey;
ALTER TABLE user_roles ALTER COLUMN role_id DROP NOT NULL;

-- backfill string role names for pre-existing rows from the roles reference table
UPDATE user_roles ur SET role = r.name
  FROM roles r WHERE ur.role_id = r.id AND ur.role IS NULL;

-- one row per (user, role) going forward
CREATE UNIQUE INDEX IF NOT EXISTS ux_user_roles_user_role ON user_roles (user_id, role);
