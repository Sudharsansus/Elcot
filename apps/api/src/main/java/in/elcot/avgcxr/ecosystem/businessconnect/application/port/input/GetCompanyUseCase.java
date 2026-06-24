package in.elcot.avgcxr.ecosystem.businessconnect.application.port.input;

import in.elcot.avgcxr.ecosystem.businessconnect.api.rest.dto.response.CompanyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface GetCompanyUseCase {
    CompanyResponse getById(UUID id);
    Page<CompanyResponse> findAll(Pageable pageable);
}
