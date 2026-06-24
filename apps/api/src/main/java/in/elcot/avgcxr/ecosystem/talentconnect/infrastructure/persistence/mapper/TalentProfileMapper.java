package in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.mapper;

import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.TalentProfile;
import in.elcot.avgcxr.ecosystem.talentconnect.infrastructure.persistence.entity.TalentProfileEntity;

public final class TalentProfileMapper {
    private TalentProfileMapper() {}

    public static TalentProfile toDomain(TalentProfileEntity e) {
        if (e == null) return null;
        return TalentProfile.builder()
                .id(e.getId())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    public static TalentProfileEntity toEntity(TalentProfile d) {
        if (d == null) return null;
        TalentProfileEntity e = new TalentProfileEntity();
        e.setId(d.getId());
        e.setCreatedAt(d.getCreatedAt());
        e.setUpdatedAt(d.getUpdatedAt());
        return e;
    }
}
