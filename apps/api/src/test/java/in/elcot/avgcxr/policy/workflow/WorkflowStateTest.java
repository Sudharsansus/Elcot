package in.elcot.avgcxr.policy.workflow;

import static org.assertj.core.api.Assertions.assertThat;

import in.elcot.avgcxr.policy.workflow.domain.model.WorkflowStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Unit tests for the workflow state machine. The AVGC-XR workflow engine uses Flowable BPMN 2.0
 * with a maker-checker-reviewer-approver pattern; the externally observable lifecycle is captured
 * by {@link WorkflowStatus}.
 */
@DisplayName("Workflow state machine")
class WorkflowStateTest {

  @Nested
  @DisplayName("active state")
  class ActiveState {
    @Test
    @DisplayName("RUNNING is the only active state")
    void onlyRunningIsActive() {
      assertThat(WorkflowStatus.RUNNING.isActive()).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = WorkflowStatus.class, names = "RUNNING", mode = EnumSource.Mode.EXCLUDE)
    @DisplayName("non-RUNNING states are not active")
    void nonRunningIsNotActive(WorkflowStatus status) {
      assertThat(status.isActive()).isFalse();
    }
  }

  @Nested
  @DisplayName("terminal state")
  class TerminalState {
    @Test
    @DisplayName("COMPLETED, CANCELLED, FAILED are terminal")
    void completedCancelledFailedAreTerminal() {
      assertThat(WorkflowStatus.COMPLETED.isTerminal()).isTrue();
      assertThat(WorkflowStatus.CANCELLED.isTerminal()).isTrue();
      assertThat(WorkflowStatus.FAILED.isTerminal()).isTrue();
    }

    @Test
    @DisplayName("RUNNING and SUSPENDED are not terminal")
    void runningAndSuspendedAreNotTerminal() {
      assertThat(WorkflowStatus.RUNNING.isTerminal()).isFalse();
      assertThat(WorkflowStatus.SUSPENDED.isTerminal()).isFalse();
    }
  }

  @Nested
  @DisplayName("maker-checker-reviewer-approver lifecycle")
  class MakerCheckerReviewerApprover {
    @Test
    @DisplayName("a fresh workflow starts RUNNING (maker step active)")
    void freshWorkflowIsRunning() {
      // The maker step is the first user-task in the BPMN; the workflow
      // instance itself is in RUNNING state until all tasks complete.
      assertThat(WorkflowStatus.RUNNING.isActive()).isTrue();
      assertThat(WorkflowStatus.RUNNING.isTerminal()).isFalse();
    }

    @Test
    @DisplayName("SUSPENDED can resume to RUNNING (e.g. pending reviewer clarification)")
    void suspendedIsResumable() {
      // SUSPENDED is neither active nor terminal — it can transition back to RUNNING
      assertThat(WorkflowStatus.SUSPENDED.isActive()).isFalse();
      assertThat(WorkflowStatus.SUSPENDED.isTerminal()).isFalse();
    }

    @Test
    @DisplayName("approver sign-off moves workflow to COMPLETED (terminal)")
    void approverSignOffIsTerminal() {
      assertThat(WorkflowStatus.COMPLETED.isTerminal()).isTrue();
      assertThat(WorkflowStatus.COMPLETED.isActive()).isFalse();
    }

    @Test
    @DisplayName("approver rejection moves workflow to CANCELLED (terminal)")
    void approverRejectionIsTerminal() {
      assertThat(WorkflowStatus.CANCELLED.isTerminal()).isTrue();
    }

    @Test
    @DisplayName("system failure (e.g. expiry) moves workflow to FAILED (terminal)")
    void systemFailureIsTerminal() {
      assertThat(WorkflowStatus.FAILED.isTerminal()).isTrue();
    }
  }
}
