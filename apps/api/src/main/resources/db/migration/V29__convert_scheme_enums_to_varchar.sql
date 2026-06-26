-- V29: convert schemes enum columns to VARCHAR to match the JPA entity (String).
-- The Scheme entity maps category/status/funding_type as String, so Hibernate
-- generates predicates like "category = ?" binding a varchar parameter. Postgres
-- has no operator "scheme_category = character varying", so /api/v1/schemes failed
-- with: ERROR: operator does not exist: scheme_category = character varying.
-- Converting the columns to VARCHAR aligns the schema with the entity. The enum
-- TYPES are left in place (harmless) in case anything else still references them.

ALTER TABLE schemes ALTER COLUMN status DROP DEFAULT;

ALTER TABLE schemes ALTER COLUMN category     TYPE VARCHAR(50) USING category::text;
ALTER TABLE schemes ALTER COLUMN status       TYPE VARCHAR(30) USING status::text;
ALTER TABLE schemes ALTER COLUMN funding_type TYPE VARCHAR(30) USING funding_type::text;

ALTER TABLE schemes ALTER COLUMN status SET DEFAULT 'DRAFT';
