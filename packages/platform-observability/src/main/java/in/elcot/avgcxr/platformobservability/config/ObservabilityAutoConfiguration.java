package in.elcot.avgcxr.platformobservability.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Auto-configuration for observability infrastructure. Registers correlation ID filter and
 * configures structured logging.
 */
@AutoConfiguration
@ConditionalOnWebApplication
public class ObservabilityAutoConfiguration {

  private static final String CORRELATION_ID_HEADER = "X-Request-ID";
  private static final String CORRELATION_ID_MDC_KEY = "correlationId";
  private static final String USER_ID_MDC_KEY = "userId";

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public FilterRegistrationBean<CorrelationIdFilter> correlationIdFilter() {
    FilterRegistrationBean<CorrelationIdFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(new CorrelationIdFilter());
    registration.addUrlPatterns("/api/*");
    registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return registration;
  }

  /**
   * Servlet filter that extracts or generates a correlation ID for each request and places it in
   * the MDC for structured logging.
   */
  static class CorrelationIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      String correlationId = httpRequest.getHeader(CORRELATION_ID_HEADER);

      if (correlationId == null || correlationId.isBlank()) {
        correlationId = UUID.randomUUID().toString();
      }

      try {
        MDC.put(CORRELATION_ID_MDC_KEY, correlationId);

        String userId = httpRequest.getHeader("X-User-ID");
        if (userId != null && !userId.isBlank()) {
          MDC.put(USER_ID_MDC_KEY, userId);
        }

        chain.doFilter(request, response);
      } finally {
        MDC.remove(CORRELATION_ID_MDC_KEY);
        MDC.remove(USER_ID_MDC_KEY);
      }
    }

    @Override
    public void init(FilterConfig filterConfig) {
      // No initialization needed
    }

    @Override
    public void destroy() {
      // No cleanup needed
    }
  }
}
