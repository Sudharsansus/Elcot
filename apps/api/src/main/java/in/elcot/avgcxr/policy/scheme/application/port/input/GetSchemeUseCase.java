package in.elcot.avgcxr.policy.scheme.application.port.input;

import in.elcot.avgcxr.policy.scheme.api.rest.dto.response.SchemeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GetSchemeUseCase {
    SchemeResponse getById(UUID id);
    Page<SchemeResponse> findAll(String category, String status, String search, Pageable pageable);
}
