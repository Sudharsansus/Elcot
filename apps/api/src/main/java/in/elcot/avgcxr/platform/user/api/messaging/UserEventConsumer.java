package in.elcot.avgcxr.platform.user.api.messaging;

import in.elcot.avgcxr.platform.user.domain.event.UserCreatedEvent;
import in.elcot.avgcxr.platform.user.domain.event.UserUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(UserEventConsumer.class);
    @RabbitListener(queues = "avgc.user.created")
    public void onUserCreated(UserCreatedEvent event) { log.info("User created: userId={}, username={}", event.userId(), event.username()); }
    @RabbitListener(queues = "avgc.user.updated")
    public void onUserUpdated(UserUpdatedEvent event) { log.info("User updated: userId={}, field={}", event.userId(), event.field()); }
}

