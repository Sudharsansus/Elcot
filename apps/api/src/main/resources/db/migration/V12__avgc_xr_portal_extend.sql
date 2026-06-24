-- V09: Dashboard and Reporting materialized views
CREATE MATERIALIZED VIEW IF NOT EXISTS mv_application_stats AS
SELECT status, district, sub_sector, DATE_TRUNC('month', submission_date) AS month, COUNT(*) AS count, SUM(COALESCE(approved_amount, 0)) AS total_amount
FROM applications GROUP BY status, district, sub_sector, DATE_TRUNC('month', submission_date);

CREATE INDEX IF NOT EXISTS idx_mv_app_stats_status ON mv_application_stats(status);
CREATE INDEX IF NOT EXISTS idx_mv_app_stats_district ON mv_application_stats(district);

CREATE OR REPLACE FUNCTION refresh_application_stats() RETURNS VOID AS $$
BEGIN REFRESH MATERIALIZED VIEW CONCURRENTLY mv_application_stats; END;
$$ LANGUAGE plpgsql;
