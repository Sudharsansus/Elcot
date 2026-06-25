package in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.mapper;

import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.model.FreelancerProfile;
import in.elcot.avgcxr.ecosystem.freelancerregistry.infrastructure.persistence.entity.FreelancerProfileEntity;

public final class FreelancerProfileMapper {
  private FreelancerProfileMapper() {}

  public static FreelancerProfile toDomain(FreelancerProfileEntity e) {
    if (e == null) return null;
    return FreelancerProfile.builder()
        .id(e.getId())
        .createdAt(e.getCreatedAt())
        .updatedAt(e.getUpdatedAt())
        .build();
  }

  public static FreelancerProfileEntity toEntity(FreelancerProfile d) {
    if (d == null) return null;
    FreelancerProfileEntity e = new FreelancerProfileEntity();
    e.setId(d.getId());
    e.setCreatedAt(d.getCreatedAt());
    e.setUpdatedAt(d.getUpdatedAt());
    return e;
  }
}
