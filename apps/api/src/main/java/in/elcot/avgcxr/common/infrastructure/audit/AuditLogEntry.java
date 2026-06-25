package in.elcot.avgcxr.common.infrastructure.audit;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record AuditLogEntry(
    UUID id,
    String actorId,
    String actorType,
    String action,
    String entityType,
    String entityId,
    Map<String, Object> oldValue,
    Map<String, Object> newValue,
    String ipAddress,
    String userAgent,
    LocalDateTime createdAt) {
  public static AuditLogEntry create(
      String action,
      String actorId,
      String entityType,
      String entityId,
      Map<String, Object> newValue,
      String ipAddress) {
    return new AuditLogEntry(
        UUID.randomUUID(),
        actorId,
        "USER",
        action,
        entityType,
        entityId,
        null,
        newValue,
        ipAddress,
        null,
        LocalDateTime.now());
  }
}
