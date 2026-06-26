package in.elcot.avgcxr.common.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.elcot.avgcxr.common.api.rest.dto.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * In-memory fixed-window rate limiter for the unauthenticated /auth write endpoints (login,
 * register, password reset). Caps credential brute-force and registration spam without an external
 * dependency — adequate for the single-instance API. If the API is scaled horizontally, move this
 * to a Redis-backed counter so the limit is shared across instances.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthRateLimitFilter extends OncePerRequestFilter {

  private static final Set<String> LIMITED_PATHS =
      Set.of(
          "/api/v1/auth/login",
          "/api/v1/auth/register",
          "/api/v1/auth/forgot-password",
          "/api/v1/auth/reset-password");

  private static final long WINDOW_MILLIS = 60_000L; // 1 minute
  private static final int MAX_REQUESTS = 10; // per client IP, per endpoint, per window
  private static final int MAX_TRACKED_KEYS = 50_000; // safety cap against memory growth

  private final ObjectMapper objectMapper;
  private final Map<String, Window> windows = new ConcurrentHashMap<>();

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String path = request.getRequestURI();
    if (!LIMITED_PATHS.contains(path)) {
      filterChain.doFilter(request, response);
      return;
    }

    long now = System.currentTimeMillis();
    String key = clientIp(request) + "|" + path;
    Window window =
        windows.compute(
            key,
            (k, existing) -> {
              if (existing == null || now - existing.start >= WINDOW_MILLIS) {
                return new Window(now);
              }
              existing.count.incrementAndGet();
              return existing;
            });

    if (window.count.get() > MAX_REQUESTS) {
      long retryAfter = Math.max(1, (WINDOW_MILLIS - (now - window.start)) / 1000);
      reject(request, response, retryAfter);
      return;
    }

    if (windows.size() > MAX_TRACKED_KEYS) {
      windows.entrySet().removeIf(e -> now - e.getValue().start >= WINDOW_MILLIS);
    }

    filterChain.doFilter(request, response);
  }

  private void reject(HttpServletRequest request, HttpServletResponse response, long retryAfter)
      throws IOException {
    log.warn("Rate limit exceeded for {} from {}", request.getRequestURI(), clientIp(request));
    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setHeader(HttpHeaders.RETRY_AFTER, String.valueOf(retryAfter));
    ApiResponse<Void> body =
        ApiResponse.error(
            "RATE_LIMITED", "Too many requests. Please wait " + retryAfter + "s and try again.");
    response.getWriter().write(objectMapper.writeValueAsString(body));
  }

  /** Behind Cloudflare/Render the real client IP is the first X-Forwarded-For hop. */
  private String clientIp(HttpServletRequest request) {
    String xff = request.getHeader("X-Forwarded-For");
    if (xff != null && !xff.isBlank()) {
      int comma = xff.indexOf(',');
      return (comma > 0 ? xff.substring(0, comma) : xff).trim();
    }
    return request.getRemoteAddr();
  }

  private static final class Window {
    final long start;
    final AtomicInteger count = new AtomicInteger(1);

    Window(long start) {
      this.start = start;
    }
  }
}
