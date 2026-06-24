package in.elcot.avgcxr.policy.document.application.command;

import java.util.Map;

public record CreateDocumentCommand(
    Map<String, Object> fields
) {}
