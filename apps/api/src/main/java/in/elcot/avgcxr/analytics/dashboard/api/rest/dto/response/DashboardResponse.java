package in.elcot.avgcxr.analytics.dashboard.api.rest.dto.response;

import java.util.Map;

public record DashboardResponse(
    long totalApplications,
    long pendingReviews,
    long approvedToday,
    long totalDisbursed,
    Map<String, Long> applicationsByStatus,
    Map<String, Long> applicationsByDistrict,
    Map<String, Long> applicationsBySubSector) {}
