package in.elcot.avgcxr.platform.auth.api.rest.dto.request;



public record UpdateAuthRequest(String currentPassword, String newPassword, String confirmPassword) {}
