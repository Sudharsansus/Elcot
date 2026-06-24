package in.elcot.avgcxr.platform.auth.api.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Frontend contract (libs/api-contracts user.model.ts):
 *   { id, username, email, mobileNumber, fullName, fullNameTamil?, roles, status,
 *     department?, designation?, district?, lastLoginAt?, createdAt, updatedAt,
 *     profileCompleted, avatarUrl? }
 *
 * <p>Now uses LOWER_CAMEL_CASE (default Jackson) so Java {@code mobileNumber}
 * maps to JSON {@code mobileNumber} directly.</p>
 */
public record UserResponse(
    UUID id,
    String username,
    String email,
    String mobileNumber,        // was 'phone' — matches frontend User model
    String fullName,
    @JsonProperty("fullNameTamil") String fullNameTamil,
    List<String> roles,
    String status,              // ACTIVE / INACTIVE / LOCKED / PENDING_VERIFICATION
    String department,
    String designation,
    String district,
    LocalDateTime lastLoginAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    boolean profileCompleted,
    String avatarUrl
) {
    // Legacy isActive getter for backward compat with old frontend code
    @JsonProperty("isActive")
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
}
