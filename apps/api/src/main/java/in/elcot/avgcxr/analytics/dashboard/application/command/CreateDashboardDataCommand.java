package in.elcot.avgcxr.analytics.dashboard.application.command;

import java.util.Map;

public record CreateDashboardDataCommand(
    Map<String, Object> fields
) {}
