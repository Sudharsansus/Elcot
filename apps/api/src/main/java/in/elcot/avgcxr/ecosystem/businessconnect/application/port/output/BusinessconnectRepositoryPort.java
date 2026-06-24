package in.elcot.avgcxr.ecosystem.businessconnect.application.port.output;

import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.Businessconnect;
import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.BusinessconnectId;
import java.util.Optional;

/** Output port for Businessconnect persistence - implemented by infrastructure adapter */
public interface BusinessconnectRepositoryPort {
    Businessconnect save(Businessconnect entity);
    Optional<Businessconnect> findById(BusinessconnectId id);
    void deleteById(BusinessconnectId id);
    boolean existsById(BusinessconnectId id);
}
