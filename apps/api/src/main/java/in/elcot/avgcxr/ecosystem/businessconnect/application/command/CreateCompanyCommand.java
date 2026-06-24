package in.elcot.avgcxr.ecosystem.businessconnect.application.command;

import java.util.Map;

public record CreateCompanyCommand(
    Map<String, Object> fields
) {}
