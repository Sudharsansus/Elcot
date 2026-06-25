package in.elcot.avgcxr.ecosystem.businessconnect.application.port.input;

import in.elcot.avgcxr.ecosystem.businessconnect.application.command.CreateBusinessconnectCommand;
import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.Businessconnect;

public interface CreateBusinessconnectUseCase {
  Businessconnect create(CreateBusinessconnectCommand command);
}
