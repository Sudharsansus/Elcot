package in.elcot.avgcxr.platform.auth.application.port.input;

import in.elcot.avgcxr.platform.auth.application.command.CreateAuthCommand;

public interface CreateAuthUseCase {
  String authenticate(CreateAuthCommand cmd);
}
