package in.elcot.avgcxr.ecosystem.talentconnect.application.port.output;

import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.Talentconnect;
import in.elcot.avgcxr.ecosystem.talentconnect.domain.model.TalentconnectId;
import java.util.Optional;

/** Output port for Talentconnect persistence - implemented by infrastructure adapter */
public interface TalentconnectRepositoryPort {
    Talentconnect save(Talentconnect entity);
    Optional<Talentconnect> findById(TalentconnectId id);
    void deleteById(TalentconnectId id);
    boolean existsById(TalentconnectId id);
}
