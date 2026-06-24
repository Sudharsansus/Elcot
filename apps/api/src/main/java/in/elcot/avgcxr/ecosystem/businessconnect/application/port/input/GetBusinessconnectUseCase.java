package in.elcot.avgcxr.ecosystem.businessconnect.application.port.input;

import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.Businessconnect;
import in.elcot.avgcxr.ecosystem.businessconnect.domain.model.BusinessconnectId;
import java.util.Optional;

public interface GetBusinessconnectUseCase {
    Optional<Businessconnect> findById(BusinessconnectId id);
    Businessconnect getById(BusinessconnectId id);
}
