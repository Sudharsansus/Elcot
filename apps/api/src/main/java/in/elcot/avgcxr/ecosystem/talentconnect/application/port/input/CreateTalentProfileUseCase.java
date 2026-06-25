package in.elcot.avgcxr.ecosystem.talentconnect.application.port.input;

import in.elcot.avgcxr.ecosystem.talentconnect.api.rest.dto.response.TalentProfileResponse;
import in.elcot.avgcxr.ecosystem.talentconnect.application.command.CreateTalentProfileCommand;

public interface CreateTalentProfileUseCase {
  TalentProfileResponse create(CreateTalentProfileCommand command);
}
