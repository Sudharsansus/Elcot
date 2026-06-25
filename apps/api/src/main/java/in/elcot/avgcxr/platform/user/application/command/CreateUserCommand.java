package in.elcot.avgcxr.platform.user.application.command;

import java.util.Set;

public record CreateUserCommand(
    String username,
    String email,
    String mobileNumber,
    String fullName,
    String fullNameTamil,
    String password,
    String district,
    Set<String> roles) {}
