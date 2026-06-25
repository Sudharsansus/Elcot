package in.elcot.avgcxr.platformevents.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import in.elcot.avgcxr.platformevents.domain.DomainEvent;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Publishes domain events to the configured RabbitMQ exchange. The RabbitTemplate dependency is
 * lazy-loaded so this bean can be instantiated in environments where RabbitMQ is not available.
 */
@Slf4j
@Component
public class RabbitMQEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final ObjectMapper objectMapper;

  public RabbitMQEventPublisher(@Lazy RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
    this.rabbitTemplate = rabbitTemplate;
    this.objectMapper =
        objectMapper != null
            ? objectMapper
            : new ObjectMapper().registerModule(new JavaTimeModule());
  }

  public void publish(String routingKey, DomainEvent event) {
    try {
      Map<String, Object> body = new HashMap<>();
      body.put("eventId", event.getEventId().toString());
      body.put("eventType", event.getEventType());
      body.put("timestamp", event.getTimestamp().toString());
      body.put("metadata", event.getMetadata());
      String json = objectMapper.writeValueAsString(body);
      Message message = new Message(json.getBytes(), new MessageProperties());
      rabbitTemplate.send(routingKey, message);
      log.info("Published event [{}] to routing key [{}]", event.getEventType(), routingKey);
    } catch (JsonProcessingException ex) {
      log.error("Failed to serialize event [{}]: {}", event.getEventType(), ex.getMessage());
    } catch (Exception ex) {
      log.error(
          "Failed to publish event [{}] to [{}]: {}",
          event.getEventType(),
          routingKey,
          ex.getMessage());
    }
  }
}
