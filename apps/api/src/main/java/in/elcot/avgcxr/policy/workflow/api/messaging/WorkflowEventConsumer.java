package in.elcot.avgcxr.policy.workflow.api.messaging;

import in.elcot.avgcxr.policy.workflow.domain.event.WorkflowCreatedEvent;
import in.elcot.avgcxr.policy.workflow.domain.event.WorkflowUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkflowEventConsumer {
  private static final Logger log = LoggerFactory.getLogger(WorkflowEventConsumer.class);

  @RabbitListener(queues = "avgc.workflow.created")
  public void onCreated(WorkflowCreatedEvent event) {
    log.info("Workflow created event: id={}", event.workflowId());
  }

  @RabbitListener(queues = "avgc.workflow.updated")
  public void onUpdated(WorkflowUpdatedEvent event) {
    log.info("Workflow updated event: id={}, field={}", event.workflowId(), event.field());
  }
}
