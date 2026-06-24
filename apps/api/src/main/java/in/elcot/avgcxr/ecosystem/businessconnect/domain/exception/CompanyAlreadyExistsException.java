package in.elcot.avgcxr.ecosystem.businessconnect.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class CompanyAlreadyExistsException extends ConflictException {
    public CompanyAlreadyExistsException(String field, String value) {
        super("COMPANY_DUPLICATE", "Company already exists with " + field + ": " + value);
    }
}
