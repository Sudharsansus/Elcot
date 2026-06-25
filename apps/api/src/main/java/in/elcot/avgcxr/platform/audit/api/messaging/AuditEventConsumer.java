package in.elcot.avgcxr.platform.audit.api.messaging;

import in.elcot.avgcxr.platform.audit.domain.event.AuditCreatedEvent;
import in.elcot.avgcxr.platform.audit.domain.event.AuditUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AuditEventConsumer {
  private static final Logger log = LoggerFactory.getLogger(AuditEventConsumer.class);

  @RabbitListener(queues = "avgc.audit.created")
  public void onCreated(AuditCreatedEvent event) {
    log.info("Audit created event: id={}", event.auditId());
  }

  @RabbitListener(queues = "avgc.audit.updated")
  public void onUpdated(AuditUpdatedEvent event) {
    log.info("Audit updated event: id={}, field={}", event.auditId(), event.field());
  }
}
