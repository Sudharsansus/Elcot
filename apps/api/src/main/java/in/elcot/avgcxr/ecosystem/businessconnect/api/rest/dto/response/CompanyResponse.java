package in.elcot.avgcxr.ecosystem.businessconnect.api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CompanyResponse(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt) {}
