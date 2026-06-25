package in.elcot.avgcxr.ecosystem.talentconnect.domain.exception;

public class DuplicateTalentconnectException extends RuntimeException {
  public DuplicateTalentconnectException(String field, String value) {
    super("Talentconnect already exists with " + field + ": " + value);
  }
}
