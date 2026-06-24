package in.elcot.avgcxr.platform.audit.api.rest.dto.response;

import in.elcot.avgcxr.platform.audit.domain.model.Audit;
import java.time.Instant;

public record AuditResponse(String id, String name, String description, Instant createdAt, Instant updatedAt) {
    public static AuditResponse from(Audit e) {
        return new AuditResponse(e.getId().toString(), "name", "description", e.getCreatedAt(), e.getUpdatedAt());
    }
}
