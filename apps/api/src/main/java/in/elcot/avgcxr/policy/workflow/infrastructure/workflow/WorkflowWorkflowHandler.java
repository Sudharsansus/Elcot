package in.elcot.avgcxr.policy.workflow.infrastructure.workflow;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WorkflowWorkflowHandler {
  private static final Logger log = LoggerFactory.getLogger(WorkflowWorkflowHandler.class);
  private final RuntimeService runtimeService;
  private final TaskService taskService;

  public WorkflowWorkflowHandler(RuntimeService runtimeService, TaskService taskService) {
    this.runtimeService = runtimeService;
    this.taskService = taskService;
  }

  public void startProcess(
      String processKey, String businessKey, java.util.Map<String, Object> variables) {
    log.info("Starting process {} for business key {}", processKey, businessKey);
    runtimeService.startProcessInstanceByKey(processKey, businessKey, variables);
  }

  public void completeTask(String taskId, java.util.Map<String, Object> variables) {
    log.info("Completing task {}", taskId);
    if (variables != null) variables.forEach((k, v) -> taskService.setVariable(taskId, k, v));
    taskService.complete(taskId);
  }

  public void claimTask(String taskId, String userId) {
    taskService.claim(taskId, userId);
  }

  public void delegateTask(String taskId, String userId) {
    taskService.delegateTask(taskId, userId);
  }
}
