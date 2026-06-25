package in.elcot.avgcxr.platform.audit.infrastructure.persistence.mapper;

import in.elcot.avgcxr.platform.audit.domain.model.AuditLog;
import in.elcot.avgcxr.platform.audit.infrastructure.persistence.entity.AuditLogEntity;

public final class AuditLogMapper {
  private AuditLogMapper() {}

  public static AuditLog toDomain(AuditLogEntity e) {
    if (e == null) return null;
    return AuditLog.builder()
        .id(e.getId())
        .createdAt(e.getCreatedAt())
        .updatedAt(e.getUpdatedAt())
        .build();
  }

  public static AuditLogEntity toEntity(AuditLog d) {
    if (d == null) return null;
    AuditLogEntity e = new AuditLogEntity();
    e.setId(d.getId());
    e.setCreatedAt(d.getCreatedAt());
    e.setUpdatedAt(d.getUpdatedAt());
    return e;
  }
}
