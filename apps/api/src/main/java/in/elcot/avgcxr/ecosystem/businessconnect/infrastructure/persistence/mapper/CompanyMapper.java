package in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.mapper;

import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.Company;
import in.elcot.avgcxr.ecosystem.businessconnect.infrastructure.persistence.entity.CompanyEntity;

public final class CompanyMapper {
  private CompanyMapper() {}

  public static Company toDomain(CompanyEntity e) {
    if (e == null) return null;
    return Company.builder()
        .id(e.getId())
        .createdAt(e.getCreatedAt())
        .updatedAt(e.getUpdatedAt())
        .build();
  }

  public static CompanyEntity toEntity(Company d) {
    if (d == null) return null;
    CompanyEntity e = new CompanyEntity();
    e.setId(d.getId());
    e.setCreatedAt(d.getCreatedAt());
    e.setUpdatedAt(d.getUpdatedAt());
    return e;
  }
}
