package in.elcot.avgcxr.ecosystem.talentconnect.application.port.input;

import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.Talentconnect;
import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.TalentconnectId;
import java.util.Optional;

public interface GetTalentconnectUseCase {
  Optional<Talentconnect> findById(TalentconnectId id);

  Talentconnect getById(TalentconnectId id);
}
