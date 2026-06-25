package in.elcot.avgcxr.ecosystem.talentconnect.api.rest.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record TalentProfileResponse(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt) {}
