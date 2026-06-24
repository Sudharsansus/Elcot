package in.elcot.avgcxr.platform.auth.api.rest;

import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import in.elcot.avgcxr.platform.auth.api.rest.dto.request.CreateUserRequest;
import in.elcot.avgcxr.platform.auth.api.rest.dto.request.UpdateUserRequest;
import in.elcot.avgcxr.platform.auth.api.rest.dto.response.AuthResponse;
import in.elcot.avgcxr.platform.auth.api.rest.dto.response.UserResponse;
import in.elcot.avgcxr.platform.auth.application.port.input.CreateUserUseCase;
import in.elcot.avgcxr.platform.auth.application.port.input.GetUserUseCase;
import in.elcot.avgcxr.platform.auth.application.command.RegisterUserCommand;
import in.elcot.avgcxr.platform.auth.application.command.UpdateProfileCommand;
import in.elcot.avgcxr.platform.auth.application.service.MfaService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final MfaService mfaService;
    private final in.elcot.avgcxr.platform.user.application.service.AccountSecurityService accountSecurityService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody CreateUserRequest request,
            HttpServletRequest httpRequest) {
        var command = new RegisterUserCommand(
                request.email(), request.password(), request.fullName(),
                request.tamilName(), request.phone(), request.role(), request.district(),
                request.consents(),
                extractIp(httpRequest),
                truncate(httpRequest.getHeader("User-Agent"), 500));
        AuthResponse response = createUserUseCase.create(command);
        return ResponseEntity.ok(ApiResponse.success(response, "Registration successful"));
    }

    /** Extract client IP, walking X-Forwarded-For for reverse-proxy setups. */
    private static String extractIp(HttpServletRequest req) {
        String xff = req.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            // First entry in comma-separated list is the original client
            return xff.split(",")[0].trim();
        }
        return req.getRemoteAddr();
    }

    private static String truncate(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @RequestBody CreateUserRequest.LoginRequest request) {
        AuthResponse response = getUserUseCase.authenticate(request.email(), request.password());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @RequestBody java.util.Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        if (refreshToken == null) refreshToken = body.get("refreshToken");
        AuthResponse response = getUserUseCase.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // Alias for /refresh-token — some frontends call /refresh
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @RequestBody java.util.Map<String, String> body) {
        return refreshToken(body);
    }

    // Password reset request — emails a one-time token
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @RequestBody java.util.Map<String, String> body) {
        String email = body.get("email");
        getUserUseCase.requestPasswordReset(email);
        return ResponseEntity.ok(ApiResponse.success(null,
                "If the email exists, a reset link has been sent"));
    }

    // Password reset confirmation
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @RequestBody java.util.Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("password");
        getUserUseCase.resetPassword(token, newPassword);
        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successful"));
    }

    // Alias for PUT /me — some frontends call /profile
    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfileAlias(
            @Valid @RequestBody UpdateUserRequest request) {
        return updateProfile(request);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        UserResponse user = getUserUseCase.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UpdateUserRequest request) {
        var command = new UpdateProfileCommand(getUserUseCase.getCurrentUserId(), request);
        UserResponse user = getUserUseCase.update(command);
        return ResponseEntity.ok(ApiResponse.success(user, "Profile updated"));
    }

    // ─── MFA / TOTP endpoints (RFC 6238) ───────────────────────────
    /** Start MFA enrollment — returns otpauth:// URL + secret */
    @PostMapping("/mfa/setup")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<MfaService.MfaEnrollment>> setupMfa() {
        java.util.UUID userId = getUserUseCase.getCurrentUserId();
        String email = getUserUseCase.getById(userId).email();
        var enrollment = mfaService.enroll(email);
        return ResponseEntity.ok(ApiResponse.success(enrollment,
                "Scan QR with Google Authenticator then call /mfa/verify"));
    }

    /** QR code as PNG for the current enrollment */
    @GetMapping(value = "/mfa/qr.png", produces = MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> qrPng() {
        java.util.UUID userId = getUserUseCase.getCurrentUserId();
        String email = getUserUseCase.getById(userId).email();
        var enrollment = mfaService.enroll(email);
        byte[] png = mfaService.generateQrCodePng(enrollment.otpAuthUrl(), 256, 256);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/png")
                .body(png);
    }

    /** Verify a 6-digit TOTP code */
    @PostMapping("/mfa/verify")
    public ResponseEntity<ApiResponse<java.util.Map<String, Object>>> verifyMfa(
            @RequestBody java.util.Map<String, String> body) {
        String secret = body.get("secret");
        String code = body.get("code");
        boolean valid = mfaService.verifyCode(secret, code);
        return ResponseEntity.ok(ApiResponse.success(
                java.util.Map.of("valid", valid),
                valid ? "MFA verified" : "Invalid or expired code"));
    }

    // ─── DPDP Act 2023 consent endpoints ──────────────────────
    /** List all consent records for the current authenticated user. */
    @GetMapping("/me/consents")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<java.util.List<in.elcot.avgcxr.platform.user.domain.model.UserConsent>>> myConsents() {
        var user = getUserUseCase.getCurrentUser();
        var consents = accountSecurityService.getConsents(user.id());
        return ResponseEntity.ok(ApiResponse.success(consents));
    }

    /**
     * Record a consent decision. Body: { "consentType": "DPDP_DATA_PROCESSING",
     *                                     "granted": true }
     */
    @PostMapping("/me/consents")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<in.elcot.avgcxr.platform.user.domain.model.UserConsent>> recordConsent(
            @RequestBody java.util.Map<String, Object> body,
            HttpServletRequest httpRequest) {
        String consentType = (String) body.get("consentType");
        Boolean granted = (Boolean) body.getOrDefault("granted", Boolean.TRUE);
        if (consentType == null || consentType.isBlank()) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("VALIDATION_FAILED", "consentType is required"));
        }
        var user = getUserUseCase.getCurrentUser();
        var consent = accountSecurityService.recordConsent(
                user.id(), consentType, granted,
                extractIp(httpRequest),
                truncate(httpRequest.getHeader("User-Agent"), 500));
        return ResponseEntity.ok(ApiResponse.success(consent, "Consent recorded"));
    }
}
