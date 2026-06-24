package in.elcot.avgcxr.policy.scheme.api.messaging;

import in.elcot.avgcxr.policy.scheme.domain.event.SchemeSubmittedEvent;
import in.elcot.avgcxr.policy.scheme.domain.event.SchemeApprovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SchemeEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(SchemeEventConsumer.class);
    @RabbitListener(queues = "avgc.scheme.submitted")
    public void onSubmitted(SchemeSubmittedEvent e) { log.info("Scheme submitted: {}", e.schemeId()); }
    @RabbitListener(queues = "avgc.scheme.approved")
    public void onApproved(SchemeApprovedEvent e) { log.info("Scheme approved: {}", e.schemeId()); }
}

