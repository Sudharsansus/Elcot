package in.elcot.avgcxr.platform.auth.api.messaging;

import in.elcot.avgcxr.platform.auth.domain.event.AuthCreatedEvent;
import in.elcot.avgcxr.platform.auth.domain.event.AuthUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AuthEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(AuthEventConsumer.class);
    @RabbitListener(queues = "avgc.auth.created")
    public void onAuthCreated(AuthCreatedEvent e) { log.info("Auth: userId={}, action={}", e.userId(), e.action()); }
    @RabbitListener(queues = "avgc.auth.updated")
    public void onAuthUpdated(AuthUpdatedEvent e) { log.info("Auth updated: userId={}, action={}", e.userId(), e.action()); }
}

