package in.elcot.avgcxr.platformevents.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMQEventConsumer {

  private final ObjectMapper objectMapper;

  public RabbitMQEventConsumer() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
  }

  @SuppressWarnings("unchecked")
  protected Map<String, Object> deserializeMessage(String message) {
    try {
      return objectMapper.readValue(message, Map.class);
    } catch (Exception e) {
      log.error("Failed to deserialize message: {}", e.getMessage(), e);
      throw new RuntimeException("Failed to deserialize event message", e);
    }
  }

  protected String extractEventType(Map<String, Object> message) {
    Object eventType = message.get("eventType");
    return eventType != null ? eventType.toString() : "UNKNOWN";
  }

  protected String extractCorrelationId(Map<String, Object> message) {
    Object corrId = message.get("correlationId");
    return corrId != null ? corrId.toString() : null;
  }
}
