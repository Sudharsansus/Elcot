package in.elcot.avgcxr.support.helpdesk.api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record HelpdeskTicketResponse(
    UUID id,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
