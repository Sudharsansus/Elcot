package in.elcot.avgcxr.platform.user.infrastructure.persistence.mapper;

import in.elcot.avgcxr.platform.user.domain.model.UserProfile;
import in.elcot.avgcxr.platform.user.infrastructure.persistence.entity.UserProfileEntity;

public final class UserProfileMapper {
    private UserProfileMapper() {}

    public static UserProfile toDomain(UserProfileEntity e) {
        if (e == null) return null;
        return UserProfile.builder()
                .id(e.getId())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    public static UserProfileEntity toEntity(UserProfile d) {
        if (d == null) return null;
        UserProfileEntity e = new UserProfileEntity();
        e.setId(d.getId());
        e.setCreatedAt(d.getCreatedAt());
        e.setUpdatedAt(d.getUpdatedAt());
        return e;
    }
}
