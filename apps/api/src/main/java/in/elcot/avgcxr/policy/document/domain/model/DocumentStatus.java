package in.elcot.avgcxr.policy.document.domain.model;



public enum DocumentStatus {
    PENDING, UPLOADED, VERIFIED, REJECTED;
    public boolean isFinal() { return this == VERIFIED || this == REJECTED; }
}
