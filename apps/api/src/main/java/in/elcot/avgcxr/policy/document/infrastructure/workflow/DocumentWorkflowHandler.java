package in.elcot.avgcxr.policy.document.infrastructure.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DocumentWorkflowHandler {
    private static final Logger log = LoggerFactory.getLogger(DocumentWorkflowHandler.class);

    public void initiateDocumentVerification(String documentId, String applicationId) {
        log.info("Initiating document verification: doc={}, app={}", documentId, applicationId);
        // In production: assign to reviewer queue via RabbitMQ
    }
}

