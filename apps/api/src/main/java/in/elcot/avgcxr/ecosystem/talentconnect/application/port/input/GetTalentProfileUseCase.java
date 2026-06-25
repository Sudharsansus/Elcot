package in.elcot.avgcxr.ecosystem.talentconnect.application.port.input;

import in.elcot.avgcxr.ecosystem.talentconnect.api.rest.dto.response.TalentProfileResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetTalentProfileUseCase {
  TalentProfileResponse getById(UUID id);

  Page<TalentProfileResponse> findAll(Pageable pageable);
}
