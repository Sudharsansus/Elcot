package in.elcot.avgcxr.platformobservability.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Structured JSON logger for AVGC-XR Portal. Outputs log entries as single-line JSON objects for
 * easy parsing by Elasticsearch and centralized log management systems.
 *
 * <p>Log format complies with Tamil Nadu government IT audit requirements.
 *
 * <p>Example output:
 *
 * <pre>
 * {"@timestamp":"2026-06-23T10:30:00.000+05:30","level":"INFO","logger":"in.elcot.avgcxr.scheme.application.CreateSchemeHandler","message":"Scheme created","correlationId":"abc-123","userId":"user-456","traceId":"","spanId":""}
 * </pre>
 */
public final class StructuredJsonLogger {

  private static final Logger LOG = LoggerFactory.getLogger(StructuredJsonLogger.class);
  private static final ObjectMapper MAPPER;
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Asia/Kolkata"));

  static {
    MAPPER = new ObjectMapper();
    MAPPER.registerModule(new JavaTimeModule());
  }

  private final Logger delegate;
  private final String loggerName;

  private StructuredJsonLogger(Logger delegate) {
    this.delegate = delegate;
    this.loggerName = delegate.getName();
  }

  public static StructuredJsonLogger getLogger(Class<?> clazz) {
    return new StructuredJsonLogger(LoggerFactory.getLogger(clazz));
  }

  public void info(String message, Map<String, Object> context) {
    log("INFO", message, context, null);
  }

  public void warn(String message, Map<String, Object> context) {
    log("WARN", message, context, null);
  }

  public void error(String message, Map<String, Object> context, Throwable throwable) {
    log("ERROR", message, context, throwable);
  }

  public void debug(String message, Map<String, Object> context) {
    log("DEBUG", message, context, null);
  }

  private void log(String level, String message, Map<String, Object> context, Throwable throwable) {
    try {
      ObjectNode node = MAPPER.createObjectNode();
      node.put("@timestamp", FORMATTER.format(Instant.now()));
      node.put("level", level);
      node.put("logger", loggerName);
      node.put("message", message);

      String correlationId = MDC.get("correlationId");
      if (correlationId != null) {
        node.put("correlationId", correlationId);
      }

      String userId = MDC.get("userId");
      if (userId != null) {
        node.put("userId", userId);
      }

      String traceId = MDC.get("traceId");
      if (traceId != null) {
        node.put("traceId", traceId);
      }

      String spanId = MDC.get("spanId");
      if (spanId != null) {
        node.put("spanId", spanId);
      }

      if (context != null) {
        context.forEach(
            (key, value) -> {
              if (value instanceof String s) {
                node.put(key, s);
              } else if (value instanceof Number n) {
                node.put(key, n.toString());
              } else if (value instanceof Boolean b) {
                node.put(key, b);
              } else {
                node.put(key, value != null ? value.toString() : "null");
              }
            });
      }

      String json = MAPPER.writeValueAsString(node);

      switch (level) {
        case "ERROR" -> {
          if (throwable != null) {
            delegate.error(json, throwable);
          } else {
            delegate.error(json);
          }
        }
        case "WARN" -> delegate.warn(json);
        case "DEBUG" -> delegate.debug(json);
        default -> delegate.info(json);
      }
    } catch (Exception e) {
      LOG.error("Failed to write structured log: {}", e.getMessage());
      delegate.info("[{}]: {}", level, message);
    }
  }
}
