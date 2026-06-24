package in.elcot.avgcxr.platform.auth.infrastructure.persistence.mapper;

import in.elcot.avgcxr.platform.auth.domain.model.Auth;
import in.elcot.avgcxr.platform.auth.infrastructure.persistence.entity.AuthUserEntity;

public final class AuthMapper {

    private AuthMapper() {}

    public static Auth toDomain(AuthUserEntity entity) {
        if (entity == null) return null;
        Auth auth = new Auth();
        auth.setId(entity.getId());
        auth.setEmail(entity.getEmail());
        auth.setPasswordHash(entity.getPasswordHash());
        auth.setPhone(entity.getPhone());
        auth.setFullName(entity.getFullName());
        auth.setTamilName(entity.getTamilName());
        auth.setVerified(entity.isVerified());
        auth.setActive(entity.isActive());
        auth.setLastLoginAt(entity.getLastLoginAt());
        auth.setCreatedAt(entity.getCreatedAt());
        auth.setUpdatedAt(entity.getUpdatedAt());
        return auth;
    }

    public static AuthUserEntity toEntity(Auth domain) {
        if (domain == null) return null;
        AuthUserEntity entity = new AuthUserEntity();
        entity.setId(domain.getId());
        entity.setEmail(domain.getEmail());
        entity.setPasswordHash(domain.getPasswordHash());
        entity.setPhone(domain.getPhone());
        entity.setFullName(domain.getFullName());
        entity.setTamilName(domain.getTamilName());
        entity.setVerified(domain.isVerified());
        entity.setActive(domain.isActive());
        entity.setLastLoginAt(domain.getLastLoginAt());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
