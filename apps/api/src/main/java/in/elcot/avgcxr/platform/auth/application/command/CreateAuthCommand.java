package in.elcot.avgcxr.platform.auth.application.command;



public record CreateAuthCommand(String username, String password, String ipAddress, String userAgent) {}
