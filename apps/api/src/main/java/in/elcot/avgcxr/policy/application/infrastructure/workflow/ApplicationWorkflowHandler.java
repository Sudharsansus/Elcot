package in.elcot.avgcxr.policy.application.infrastructure.workflow;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ApplicationWorkflowHandler {
  private static final Logger log = LoggerFactory.getLogger(ApplicationWorkflowHandler.class);
  private final RuntimeService runtimeService;
  private final TaskService taskService;

  public ApplicationWorkflowHandler(RuntimeService runtimeService, TaskService taskService) {
    this.runtimeService = runtimeService;
    this.taskService = taskService;
  }

  public void startReviewProcess(String applicationId, String schemeType) {
    log.info("Starting review workflow for application: {}", applicationId);
    var vars = java.util.Map.of("applicationId", (Object) applicationId, "schemeType", schemeType);
    runtimeService.startProcessInstanceByKey("applicationReview", applicationId, vars);
  }

  public void completeTask(String taskId, String action, String comment) {
    log.info("Completing task {}: action={}", taskId, action);
    taskService.setVariable(taskId, "action", action);
    taskService.setVariable(taskId, "comment", comment);
    taskService.complete(taskId);
  }
}
