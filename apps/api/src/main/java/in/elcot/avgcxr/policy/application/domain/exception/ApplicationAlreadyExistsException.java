package in.elcot.avgcxr.policy.application.domain.exception;



public class ApplicationAlreadyExistsException extends RuntimeException {
    public ApplicationAlreadyExistsException(String schemeId, String applicantId) { super("Application already exists for scheme " + schemeId + " by applicant " + applicantId); }
}
