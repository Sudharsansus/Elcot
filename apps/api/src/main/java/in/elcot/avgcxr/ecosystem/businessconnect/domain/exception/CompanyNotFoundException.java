package in.elcot.avgcxr.ecosystem.businessconnect.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class CompanyNotFoundException extends NotFoundException {
    public CompanyNotFoundException(UUID id) {
        super("COMPANY_NOT_FOUND", "Company not found with id: " + id);
    }
}
