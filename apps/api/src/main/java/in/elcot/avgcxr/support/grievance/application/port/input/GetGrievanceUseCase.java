package in.elcot.avgcxr.support.grievance.application.port.input;

import in.elcot.avgcxr.support.grievance.api.rest.dto.response.GrievanceResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetGrievanceUseCase {
  GrievanceResponse getById(UUID id);

  Page<GrievanceResponse> findAll(Pageable pageable);
}
