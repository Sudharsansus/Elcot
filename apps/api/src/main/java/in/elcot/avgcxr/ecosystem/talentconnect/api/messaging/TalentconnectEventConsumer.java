package in.elcot.avgcxr.ecosystem.talentconnect.api.messaging;

import in.elcot.avgcxr.ecosystem.talentconnect.domain.event.TalentconnectCreatedEvent;
import in.elcot.avgcxr.ecosystem.talentconnect.domain.event.TalentconnectUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TalentconnectEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(TalentconnectEventConsumer.class);

    @RabbitListener(queues = "avgc.talentconnect.created")
    public void onCreated(TalentconnectCreatedEvent event) {
        log.info("Talentconnect created event: id={}", event.talentconnectId());
    }

    @RabbitListener(queues = "avgc.talentconnect.updated")
    public void onUpdated(TalentconnectUpdatedEvent event) {
        log.info("Talentconnect updated event: id={}, field={}", event.talentconnectId(), event.field());
    }
}
