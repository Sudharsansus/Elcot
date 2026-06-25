package in.elcot.avgcxr.ecosystem.talentconnect.domain.exception;

import in.elcot.avgcxr.platformcore.error.NotFoundException;
import java.util.UUID;

public class TalentProfileNotFoundException extends NotFoundException {
  public TalentProfileNotFoundException(UUID id) {
    super("TALENTPROFILE_NOT_FOUND", "TalentProfile not found with id: " + id);
  }
}
