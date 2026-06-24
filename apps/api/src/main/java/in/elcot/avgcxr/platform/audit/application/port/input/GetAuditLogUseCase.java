package in.elcot.avgcxr.platform.audit.application.port.input;

import in.elcot.avgcxr.platform.audit.api.rest.dto.response.AuditLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface GetAuditLogUseCase {
    AuditLogResponse getById(UUID id);
    Page<AuditLogResponse> findAll(Pageable pageable);
}
