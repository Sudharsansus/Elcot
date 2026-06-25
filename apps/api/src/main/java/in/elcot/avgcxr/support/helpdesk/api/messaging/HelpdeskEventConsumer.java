package in.elcot.avgcxr.support.helpdesk.api.messaging;

import in.elcot.avgcxr.support.helpdesk.domain.event.HelpdeskCreatedEvent;
import in.elcot.avgcxr.support.helpdesk.domain.event.HelpdeskUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HelpdeskEventConsumer {
  private static final Logger log = LoggerFactory.getLogger(HelpdeskEventConsumer.class);

  @RabbitListener(queues = "avgc.helpdesk.created")
  public void onCreated(HelpdeskCreatedEvent event) {
    log.info("Helpdesk created event: id={}", event.helpdeskId());
  }

  @RabbitListener(queues = "avgc.helpdesk.updated")
  public void onUpdated(HelpdeskUpdatedEvent event) {
    log.info("Helpdesk updated event: id={}, field={}", event.helpdeskId(), event.field());
  }
}
