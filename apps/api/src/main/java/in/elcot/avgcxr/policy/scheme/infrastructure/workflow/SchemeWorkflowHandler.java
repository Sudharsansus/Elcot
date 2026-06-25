package in.elcot.avgcxr.policy.scheme.infrastructure.workflow;

import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SchemeWorkflowHandler {
  private static final Logger log = LoggerFactory.getLogger(SchemeWorkflowHandler.class);
  private final RuntimeService runtimeService;

  public SchemeWorkflowHandler(RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }

  public void startSchemeApprovalProcess(String schemeId) {
    log.info("Starting scheme approval workflow for: {}", schemeId);
    var variables =
        java.util.Map.of(
            "schemeId", (Object) schemeId, "initiatedAt", java.time.Instant.now().toString());
    runtimeService.startProcessInstanceByKey("schemeApproval", schemeId, variables);
  }
}
