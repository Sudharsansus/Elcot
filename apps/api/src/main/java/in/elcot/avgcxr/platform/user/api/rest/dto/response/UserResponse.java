package in.elcot.avgcxr.platform.user.api.rest.dto.response;

import in.elcot.avgcxr.platform.user.domain.model.User;
import java.time.Instant;
import java.util.Set;

public record UserResponse(
    String id,
    String username,
    String email,
    String mobileNumber,
    String fullName,
    String fullNameTamil,
    Set<String> roles,
    String status,
    String district,
    Instant createdAt,
    Instant updatedAt,
    boolean profileCompleted) {
  public static UserResponse from(User u) {
    return new UserResponse(
        u.getId().toString(),
        u.getUsername(),
        u.getEmail(),
        u.getMobileNumber(),
        u.getFullName(),
        u.getFullNameTamil(),
        u.getRoles(),
        u.getStatus().name(),
        u.getDistrict(),
        u.getCreatedAt(),
        u.getUpdatedAt(),
        u.isProfileCompleted());
  }
}
