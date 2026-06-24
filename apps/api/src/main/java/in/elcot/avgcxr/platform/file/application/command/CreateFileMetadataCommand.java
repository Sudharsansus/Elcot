package in.elcot.avgcxr.platform.file.application.command;

import java.util.Map;

public record CreateFileMetadataCommand(
    Map<String, Object> fields
) {}
