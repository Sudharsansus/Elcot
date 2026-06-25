package in.elcot.avgcxr.support.grievance.infrastructure.persistence.mapper;

import in.elcot.avgcxr.support.grievance.domain.model.Grievance;
import in.elcot.avgcxr.support.grievance.infrastructure.persistence.entity.GrievanceEntity;

public final class GrievanceMapper {
  private GrievanceMapper() {}

  public static Grievance toDomain(GrievanceEntity e) {
    if (e == null) return null;
    return Grievance.builder()
        .id(e.getId())
        .createdAt(e.getCreatedAt())
        .updatedAt(e.getUpdatedAt())
        .build();
  }

  public static GrievanceEntity toEntity(Grievance d) {
    if (d == null) return null;
    GrievanceEntity e = new GrievanceEntity();
    e.setId(d.getId());
    e.setCreatedAt(d.getCreatedAt());
    e.setUpdatedAt(d.getUpdatedAt());
    return e;
  }
}
