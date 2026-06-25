package in.elcot.avgcxr.ecosystem.freelancerregistry.api.messaging;

import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.event.FreelancerregistryCreatedEvent;
import in.elcot.avgcxr.ecosystem.freelancerregistry.domain.event.FreelancerregistryUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FreelancerregistryEventConsumer {
  private static final Logger log = LoggerFactory.getLogger(FreelancerregistryEventConsumer.class);

  @RabbitListener(queues = "avgc.freelancerregistry.created")
  public void onCreated(FreelancerregistryCreatedEvent event) {
    log.info("Freelancerregistry created event: id={}", event.freelancerregistryId());
  }

  @RabbitListener(queues = "avgc.freelancerregistry.updated")
  public void onUpdated(FreelancerregistryUpdatedEvent event) {
    log.info(
        "Freelancerregistry updated event: id={}, field={}",
        event.freelancerregistryId(),
        event.field());
  }
}
