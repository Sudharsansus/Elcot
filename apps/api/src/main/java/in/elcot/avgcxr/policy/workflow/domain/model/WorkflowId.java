package in.elcot.avgcxr.policy.workflow.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record WorkflowId(UUID value) implements Serializable {
  public static WorkflowId generate() {
    return new WorkflowId(UUID.randomUUID());
  }

  public static WorkflowId of(String value) {
    return new WorkflowId(UUID.fromString(value));
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
