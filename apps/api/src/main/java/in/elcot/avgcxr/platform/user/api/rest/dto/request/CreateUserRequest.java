package in.elcot.avgcxr.platform.user.api.rest.dto.request;

import jakarta.validation.constraints.*;
import java.util.Set;

public record CreateUserRequest(
    @NotBlank @Size(min = 3, max = 100) String username,
    @NotBlank @Email String email,
    @NotBlank @Pattern(regexp = "^[6-9]\\d{9}$") String mobileNumber,
    @NotBlank @Size(min = 2, max = 200) String fullName,
    String fullNameTamil,
    @NotBlank @Size(min = 8, max = 128) String password,
    @NotBlank String district,
    @NotEmpty Set<String> roles) {}
