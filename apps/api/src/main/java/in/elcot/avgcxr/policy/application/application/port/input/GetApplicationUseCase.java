package in.elcot.avgcxr.policy.application.application.port.input;

import in.elcot.avgcxr.policy.application.api.rest.dto.response.ApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface GetApplicationUseCase {
    ApplicationResponse getById(UUID id);
    Page<ApplicationResponse> findAll(Pageable pageable);
    Page<ApplicationResponse> findAll(String schemeId, String status, String district, Pageable pageable);
    Page<ApplicationResponse> findMine(UUID applicantId, Pageable pageable);
    Page<ApplicationResponse> findByCurrentUser(String status, Pageable pageable);
}
