package in.elcot.avgcxr.platform.user.infrastructure.persistence.mapper;

import in.elcot.avgcxr.platform.user.domain.model.User;
import in.elcot.avgcxr.platform.user.domain.model.UserId;
import in.elcot.avgcxr.platform.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(User d) {
        return toEntity(d, null);
    }

    public UserEntity toEntity(User d, String passwordHash) {
        UserEntity e = new UserEntity();
        e.setId(d.getId().value());
        e.setUsername(d.getUsername());
        e.setEmail(d.getEmail());
        e.setMobileNumber(d.getMobileNumber());
        e.setFullName(d.getFullName());
        e.setFullNameTamil(d.getFullNameTamil());
        e.setRoles(d.getRoles());
        e.setStatus(d.getStatus() != null ? d.getStatus().name() : "PENDING_VERIFICATION");
        e.setActive(d.getStatus() != null && d.getStatus().name().equals("ACTIVE"));
        e.setVerified(d.getStatus() != null && d.getStatus().name().equals("ACTIVE"));
        e.setLastLoginAt(d.getLastLoginAt());
        e.setFailedLoginAttempts(d.getFailedLoginAttempts());
        e.setLockedUntil(d.getLockedUntil());
        e.setLastFailedLoginAt(d.getLastFailedLoginAt());
        e.setDistrict(d.getDistrict());
        e.setProfileCompleted(d.isProfileCompleted());
        e.setCreatedAt(d.getCreatedAt());
        e.setUpdatedAt(d.getUpdatedAt());
        e.setPasswordHash(passwordHash);
        return e;
    }

    public User toDomain(UserEntity e) {
        User u = new User(
                UserId.of(e.getId().toString()),
                e.getUsername(),
                e.getEmail(),
                e.getMobileNumber(),
                e.getFullName(),
                e.getRoles());
        u.updateProfile(e.getFullName(), e.getFullNameTamil(), e.getDistrict());
        if ("ACTIVE".equals(e.getStatus())) {
            u.activate();
        } else if ("LOCKED".equals(e.getStatus())) {
            // Caller should use isAccountLocked() helper — we don't flip domain status here
        }
        return u;
    }
}