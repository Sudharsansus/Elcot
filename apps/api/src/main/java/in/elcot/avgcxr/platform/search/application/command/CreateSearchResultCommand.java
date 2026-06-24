package in.elcot.avgcxr.platform.search.application.command;

import java.util.Map;

public record CreateSearchResultCommand(
    Map<String, Object> fields
) {}
