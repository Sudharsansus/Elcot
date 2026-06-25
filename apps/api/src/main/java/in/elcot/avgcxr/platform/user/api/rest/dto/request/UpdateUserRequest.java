package in.elcot.avgcxr.platform.user.api.rest.dto.request;

public record UpdateUserRequest(
    String fullName,
    String fullNameTamil,
    String district,
    String department,
    String designation) {}
