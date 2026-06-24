package in.elcot.avgcxr.ecosystem.businessconnect.api.messaging;

import in.elcot.avgcxr.ecosystem.businessconnect.domain.event.BusinessconnectCreatedEvent;
import in.elcot.avgcxr.ecosystem.businessconnect.domain.event.BusinessconnectUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BusinessconnectEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(BusinessconnectEventConsumer.class);

    @RabbitListener(queues = "avgc.businessconnect.created")
    public void onCreated(BusinessconnectCreatedEvent event) {
        log.info("Businessconnect created event: id={}", event.businessconnectId());
    }

    @RabbitListener(queues = "avgc.businessconnect.updated")
    public void onUpdated(BusinessconnectUpdatedEvent event) {
        log.info("Businessconnect updated event: id={}, field={}", event.businessconnectId(), event.field());
    }
}
