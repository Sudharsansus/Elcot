package in.elcot.avgcxr.ecosystem.talentconnect.domain.exception;

import java.util.UUID;

public class TalentconnectNotFoundException extends RuntimeException {
    public TalentconnectNotFoundException(UUID id) { super("Talentconnect not found with id: " + id); }
    public TalentconnectNotFoundException(String identifier) { super("Talentconnect not found: " + identifier); }
}
