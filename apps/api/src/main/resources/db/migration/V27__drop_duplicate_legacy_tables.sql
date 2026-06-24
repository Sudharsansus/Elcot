-- ─── V27: drop duplicate stub tables (audit fix Gap 5) ───
-- The earlier V23 migration created stub copies of business/freelancer/talent
-- tables. The canonical tables (with full columns) were created in V5/V10.
-- Drop the stubs to remove schema drift.

DROP TABLE IF EXISTS businessconnects CASCADE;
DROP TABLE IF EXISTS freelancerregistrys CASCADE;
DROP TABLE IF EXISTS talentconnects CASCADE;
DROP TABLE IF EXISTS audit_log CASCADE;     -- older name; audit_logs (plural) is canonical

COMMENT ON TABLE business_connect IS 'V27: legacy stub tables dropped';
COMMENT ON TABLE talent_connect IS 'V27: legacy stub tables dropped';
COMMENT ON TABLE freelancer_registry IS 'V27: legacy stub tables dropped';
COMMENT ON TABLE audit_logs IS 'V27: legacy audit_log dropped; audit_logs (plural) is canonical';
