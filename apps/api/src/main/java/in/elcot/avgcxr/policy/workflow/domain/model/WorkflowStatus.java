package in.elcot.avgcxr.policy.workflow.domain.model;



public enum WorkflowStatus {
    RUNNING, COMPLETED, SUSPENDED, CANCELLED, FAILED;
    public boolean isActive() { return this == RUNNING; }
    public boolean isTerminal() { return this == COMPLETED || this == CANCELLED || this == FAILED; }
}
