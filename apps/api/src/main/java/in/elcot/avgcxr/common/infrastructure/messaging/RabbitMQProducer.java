package in.elcot.avgcxr.common.infrastructure.messaging;

import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Slf4j
@org.springframework.context.annotation.Profile("!no-rabbit")
@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class RabbitMQProducer {

  private final RabbitTemplate rabbitTemplate;

  public void publish(DomainEvent event, String routingKey) {
    try {
      rabbitTemplate.convertAndSend(
          in.elcot.avgcxr.common.infrastructure.config.RabbitMQConfig.EXCHANGE, routingKey, event);
      log.info("Published event [{}] with routing key [{}]", event.getEventType(), routingKey);
    } catch (Exception ex) {
      log.error(
          "Failed to publish event [{}] to [{}]: {}",
          event.getEventType(),
          routingKey,
          ex.getMessage());
      throw new RuntimeException("Failed to publish domain event", ex);
    }
  }
}
