package in.elcot.avgcxr.platform.auth.api.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Frontend contract (libs/api-contracts):
 *   { name, email, phone, role, password, district?, tamilName? }
 *
 * <p>Accepts multiple aliases via {@code @JsonAlias} so existing callers
 * that send {@code fullName} (legacy) or {@code mobileNumber} (legacy)
 * still work.</p>
 */
public record CreateUserRequest(
    @JsonProperty("name")
    @JsonAlias({"fullName", "full_name"})
    @NotBlank(message = "Name is required")
    @Size(max = 200)
    String fullName,

    @JsonProperty("tamilName")
    @JsonAlias({"tamil_name", "fullNameTamil"})
    String tamilName,

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128)
    String password,

    @JsonProperty("phone")
    @JsonAlias({"mobileNumber", "mobile_number", "mobile"})
    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 20)
    String phone,

    @NotNull(message = "Role is required")
    String role,

    String district,

    /**
     * DPDP Act 2023 consent decisions captured at signup. Map of consentType → granted.
     * Example: { "DPDP_DATA_PROCESSING": true, "MARKETING_COMMUNICATIONS": false }
     * Optional — if omitted, only DPDP_DATA_PROCESSING is recorded (granted=true is the default).
     */
    java.util.Map<String, Boolean> consents
) {
    public record LoginRequest(
        @Email @NotBlank String email,
        @NotBlank String password
    ) {}
}
