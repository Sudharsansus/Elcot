package in.elcot.avgcxr.platform.audit.application.command;

import java.util.Map;

public record CreateAuditLogCommand(
    Map<String, Object> fields
) {}
