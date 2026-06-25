package in.elcot.avgcxr.platformevents.domain;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class DomainEvent {

  private final UUID eventId;
  private final String eventType;
  private final LocalDateTime timestamp;
  private final Map<String, Object> metadata;
  private String correlationId;

  public DomainEvent(String eventType) {
    this.eventId = UUID.randomUUID();
    this.eventType = eventType;
    this.timestamp = LocalDateTime.now();
    this.metadata = Map.of();
  }

  public DomainEvent(String eventType, Map<String, Object> metadata) {
    this.eventId = UUID.randomUUID();
    this.eventType = eventType;
    this.timestamp = LocalDateTime.now();
    this.metadata = metadata != null ? metadata : Map.of();
  }

  /**
   * Compatibility constructor for subclasses that pass aggregate id, event type, timestamp, entity
   * name, and metadata.
   */
  public DomainEvent(
      UUID aggregateId,
      String eventType,
      LocalDateTime timestamp,
      String entityName,
      Map<String, String> metadata) {
    this.eventId = aggregateId != null ? aggregateId : UUID.randomUUID();
    this.eventType = eventType;
    this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    this.metadata = metadata != null ? new java.util.HashMap<>(metadata) : Map.of();
  }

  /**
   * Compatibility constructor for subclasses that pass aggregate id, event type, timestamp, and
   * metadata (no entity name).
   */
  public DomainEvent(
      UUID aggregateId, String eventType, LocalDateTime timestamp, Map<String, Object> metadata) {
    this.eventId = aggregateId != null ? aggregateId : UUID.randomUUID();
    this.eventType = eventType;
    this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    this.metadata = metadata != null ? metadata : Map.of();
  }

  public UUID getEventId() {
    return eventId;
  }

  public String getEventType() {
    return eventType;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }
}
