package in.elcot.avgcxr.policy.application.infrastructure.persistence.mapper;

import in.elcot.avgcxr.policy.application.domain.model.Application;
import in.elcot.avgcxr.policy.application.infrastructure.persistence.entity.ApplicationEntity;

public final class ApplicationMapper {
  private ApplicationMapper() {}

  public static Application toDomain(ApplicationEntity e) {
    if (e == null) return null;
    return Application.builder()
        .id(e.getId())
        .applicationNumber(e.getApplicationNumber())
        .schemeId(e.getSchemeId())
        .applicantId(e.getApplicantId())
        .district(e.getDistrict())
        .status(Application.ApplicationStatus.valueOf(e.getStatus()))
        .submittedAt(e.getSubmittedAt())
        .reviewedAt(e.getReviewedAt())
        .approvedAt(e.getApprovedAt())
        .rejectedAt(e.getRejectedAt())
        .rejectionReason(e.getRejectionReason())
        .fundingApproved(e.getFundingApproved())
        .createdAt(e.getCreatedAt())
        .updatedAt(e.getUpdatedAt())
        .build();
  }

  public static ApplicationEntity toEntity(Application d) {
    if (d == null) return null;
    ApplicationEntity e = new ApplicationEntity();
    e.setId(d.getId());
    e.setApplicationNumber(d.getApplicationNumber());
    e.setSchemeId(d.getSchemeId());
    e.setApplicantId(d.getApplicantId());
    e.setDistrict(d.getDistrict());
    e.setStatus(d.getStatus().name());
    e.setSubmittedAt(d.getSubmittedAt());
    e.setReviewedAt(d.getReviewedAt());
    e.setApprovedAt(d.getApprovedAt());
    e.setRejectedAt(d.getRejectedAt());
    e.setRejectionReason(d.getRejectionReason());
    e.setFundingApproved(d.getFundingApproved());
    e.setCreatedAt(d.getCreatedAt());
    e.setUpdatedAt(d.getUpdatedAt());
    return e;
  }
}
