package in.elcot.avgcxr.ecosystem.freelancerregistry.domain.exception;

public class DuplicateFreelancerregistryException extends RuntimeException {
  public DuplicateFreelancerregistryException(String field, String value) {
    super("Freelancerregistry already exists with " + field + ": " + value);
  }
}
