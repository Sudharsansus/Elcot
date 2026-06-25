package in.elcot.avgcxr.support.grievance.application.port.input;

import in.elcot.avgcxr.support.grievance.api.rest.dto.response.GrievanceResponse;
import in.elcot.avgcxr.support.grievance.application.command.CreateGrievanceCommand;

public interface CreateGrievanceUseCase {
  GrievanceResponse create(CreateGrievanceCommand command);
}
