package in.elcot.avgcxr.ecosystem.talentconnect.application.port.input;

import in.elcot.avgcxr.ecosystem.talentconnect.application.command.CreateTalentconnectCommand;
import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.Talentconnect;

public interface CreateTalentconnectUseCase {
  Talentconnect create(CreateTalentconnectCommand command);
}
