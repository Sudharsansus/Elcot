package in.elcot.avgcxr.ecosystem.businessconnect.application.port.input;

import in.elcot.avgcxr.ecosystem.businessconnect.api.rest.dto.response.CompanyResponse;
import in.elcot.avgcxr.ecosystem.businessconnect.application.command.CreateCompanyCommand;

public interface CreateCompanyUseCase {
    CompanyResponse create(CreateCompanyCommand command);
}
