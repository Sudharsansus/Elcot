package in.elcot.avgcxr.policy.application.domain.event;

import java.time.Instant;
import java.util.UUID;

public record ApplicationApprovedEvent(UUID applicationId, String approvedBy, double approvedAmount, Instant occurredAt) {
    public static ApplicationApprovedEvent from(UUID id, String by, double amt) { return new ApplicationApprovedEvent(id, by, amt, Instant.now()); }
}
