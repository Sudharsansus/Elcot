package in.elcot.avgcxr.analytics.reporting.domain.model;

import java.util.Map;

public record ReportingData(
    String reportType,
    String title,
    String titleTamil,
    Map<String, Object> data,
    String generatedAt) {}
