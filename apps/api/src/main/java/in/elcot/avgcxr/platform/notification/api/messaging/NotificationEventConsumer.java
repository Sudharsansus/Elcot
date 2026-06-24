package in.elcot.avgcxr.platform.notification.api.messaging;

import in.elcot.avgcxr.platform.notification.domain.event.NotificationUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(NotificationEventConsumer.class);
    @RabbitListener(queues = "avgc.notification.updated")
    public void onNotificationUpdated(NotificationUpdatedEvent e) { log.info("Notification updated: id={}, status={}", e.notificationId(), e.status()); }
}

