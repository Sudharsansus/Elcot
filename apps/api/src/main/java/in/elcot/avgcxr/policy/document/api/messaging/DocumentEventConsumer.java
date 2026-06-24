package in.elcot.avgcxr.policy.document.api.messaging;

import in.elcot.avgcxr.policy.document.domain.event.DocumentSubmittedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DocumentEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(DocumentEventConsumer.class);
    @RabbitListener(queues = "avgc.document.submitted")
    public void onSubmitted(DocumentSubmittedEvent e) { log.info("Document submitted: {} for app: {}", e.documentId(), e.applicationId()); }
}

