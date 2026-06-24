package in.elcot.avgcxr.ecosystem.talentconnect.application.port.input;

import in.elcot.avgcxr.ecosystem.talentconnect.api.rest.dto.response.TalentProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface GetTalentProfileUseCase {
    TalentProfileResponse getById(UUID id);
    Page<TalentProfileResponse> findAll(Pageable pageable);
}
