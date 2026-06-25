package in.elcot.avgcxr.ecosystem.talentconnect.domain.exception;

import in.elcot.avgcxr.platformcore.error.ConflictException;

public class TalentProfileAlreadyExistsException extends ConflictException {
  public TalentProfileAlreadyExistsException(String field, String value) {
    super("TALENTPROFILE_DUPLICATE", "TalentProfile already exists with " + field + ": " + value);
  }
}
