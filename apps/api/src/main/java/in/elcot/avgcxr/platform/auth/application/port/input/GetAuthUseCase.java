package in.elcot.avgcxr.platform.auth.application.port.input;

public interface GetAuthUseCase {
  boolean validateToken(String token);
}
