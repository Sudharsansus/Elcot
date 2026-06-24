package in.elcot.avgcxr.analytics.reporting.api.rest.dto.response;

import java.util.List;
import java.util.Map;

public record ReportingResponse(String reportType, String title, List<Map<String, Object>> rows, Map<String, Object> summary) {}
