package in.elcot.avgcxr.policy.application.api.messaging;

import in.elcot.avgcxr.policy.application.domain.event.ApplicationApprovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(ApplicationEventConsumer.class);
    @RabbitListener(queues = "avgc.application.approved")
    public void onApproved(ApplicationApprovedEvent e) { log.info("Application approved: {}, amount: {}", e.applicationId(), e.approvedAmount()); }
}

