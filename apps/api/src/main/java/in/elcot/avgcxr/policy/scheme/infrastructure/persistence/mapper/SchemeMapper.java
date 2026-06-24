package in.elcot.avgcxr.policy.scheme.infrastructure.persistence.mapper;

import in.elcot.avgcxr.policy.scheme.domain.model.Scheme;
import in.elcot.avgcxr.policy.scheme.infrastructure.persistence.entity.SchemeEntity;

public final class SchemeMapper {
    private SchemeMapper() {}

    public static Scheme toDomain(SchemeEntity e) {
        if (e == null) return null;
        return Scheme.builder()
                .id(e.getId()).name(e.getName()).tamilName(e.getNameTa())
                .description(e.getDescription()).descriptionTa(e.getDescriptionTa())
                .category(Scheme.SchemeCategory.valueOf(e.getCategory()))
                .subCategory(e.getSubCategory()).ministry(e.getMinistry())
                .department(e.getDepartment()).fundingAmountMin(e.getFundingAmountMin())
                .fundingAmountMax(e.getFundingAmountMax())
                .fundingType(e.getFundingType() != null ? Scheme.FundingType.valueOf(e.getFundingType()) : null)
                .applicationStartDate(e.getApplicationStartDate())
                .applicationEndDate(e.getApplicationEndDate())
                .status(Scheme.SchemeStatus.valueOf(e.getStatus()))
                .active(e.isActive()).thumbnailUrl(e.getThumbnailUrl())
                .publishedAt(e.getPublishedAt())
                .createdAt(e.getCreatedAt()).updatedAt(e.getUpdatedAt())
                .build();
    }

    public static SchemeEntity toEntity(Scheme d) {
        if (d == null) return null;
        SchemeEntity e = new SchemeEntity();
        e.setId(d.getId()); e.setName(d.getName()); e.setNameTa(d.getTamilName());
        e.setDescription(d.getDescription()); e.setDescriptionTa(d.getDescriptionTa());
        e.setCategory(d.getCategory().name()); e.setSubCategory(d.getSubCategory());
        e.setMinistry(d.getMinistry()); e.setDepartment(d.getDepartment());
        e.setFundingAmountMin(d.getFundingAmountMin()); e.setFundingAmountMax(d.getFundingAmountMax());
        e.setFundingType(d.getFundingType() != null ? d.getFundingType().name() : null);
        e.setApplicationStartDate(d.getApplicationStartDate());
        e.setApplicationEndDate(d.getApplicationEndDate());
        e.setStatus(d.getStatus().name()); e.setActive(d.isActive());
        e.setThumbnailUrl(d.getThumbnailUrl()); e.setPublishedAt(d.getPublishedAt());
        e.setCreatedAt(d.getCreatedAt()); e.setUpdatedAt(d.getUpdatedAt());
        return e;
    }
}
