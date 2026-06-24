package in.elcot.avgcxr.platform.auth.api.rest.dto.request;

public record UpdateUserRequest(
    String fullName,
    String tamilName,
    String phone,
    String district,
    String address,
    String pincode
) {}
