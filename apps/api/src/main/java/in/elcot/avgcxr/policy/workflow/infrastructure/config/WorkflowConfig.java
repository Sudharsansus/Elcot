package in.elcot.avgcxr.policy.workflow.infrastructure.config;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Map;

/**
 * Workflow (Flowable) configuration.
 *
 * <p>Flowable's Spring Boot starter auto-configures the ProcessEngine and
 * all the service beans (RepositoryService, TaskService, etc.). This
 * config adds an explicit readiness check: on application context
 * refresh we enumerate the deployed process definitions and log them
 * so a missing BPMN is immediately visible at boot.</p>
 */
@Configuration
public class WorkflowConfig {

    private static final Logger log = LoggerFactory.getLogger(WorkflowConfig.class);

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;
    private final IdentityService identityService;
    private final ManagementService managementService;

    public WorkflowConfig(RepositoryService repositoryService,
                          RuntimeService runtimeService,
                          TaskService taskService,
                          HistoryService historyService,
                          IdentityService identityService,
                          ManagementService managementService) {
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.historyService = historyService;
        this.identityService = identityService;
        this.managementService = managementService;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void verifyDeployedProcesses() {
        try {
            List<org.flowable.engine.repository.ProcessDefinition> defs =
                    repositoryService.createProcessDefinitionQuery().latestVersion().list();
            log.info("Flowable ready: {} process definition(s) deployed", defs.size());
            for (var d : defs) {
                log.info("  • process: id={} key={} name={} version={}",
                        d.getId(), d.getKey(), d.getName(), d.getVersion());
            }
        } catch (Exception e) {
            log.error("Flowable engine check failed", e);
        }
    }

    // The Flowable service beans are auto-configured by flowable-spring-boot-starter.
    // We expose them as @Bean here so other modules can inject them by name if needed.
    // (Method names differ from field names to avoid self-injection / circular refs.)
}
