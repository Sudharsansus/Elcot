package in.elcot.avgcxr.analytics.reporting.infrastructure.persistence.mapper;

import in.elcot.avgcxr.analytics.reporting.domain.model.ReportData;
import in.elcot.avgcxr.analytics.reporting.infrastructure.persistence.entity.ReportDataEntity;

public final class ReportDataMapper {
  private ReportDataMapper() {}

  public static ReportData toDomain(ReportDataEntity e) {
    if (e == null) return null;
    return ReportData.builder()
        .id(e.getId())
        .createdAt(e.getCreatedAt())
        .updatedAt(e.getUpdatedAt())
        .build();
  }

  public static ReportDataEntity toEntity(ReportData d) {
    if (d == null) return null;
    ReportDataEntity e = new ReportDataEntity();
    e.setId(d.getId());
    e.setCreatedAt(d.getCreatedAt());
    e.setUpdatedAt(d.getUpdatedAt());
    return e;
  }
}
