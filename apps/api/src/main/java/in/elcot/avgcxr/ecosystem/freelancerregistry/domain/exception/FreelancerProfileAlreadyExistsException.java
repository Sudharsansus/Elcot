package in.elcot.avgcxr.ecosystem.freelancerregistry.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class FreelancerProfileAlreadyExistsException extends ConflictException {
  public FreelancerProfileAlreadyExistsException(String field, String value) {
    super(
        "FREELANCERPROFILE_DUPLICATE",
        "FreelancerProfile already exists with " + field + ": " + value);
  }
}
