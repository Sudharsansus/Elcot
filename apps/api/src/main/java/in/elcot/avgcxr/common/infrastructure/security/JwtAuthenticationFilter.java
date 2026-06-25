package in.elcot.avgcxr.common.infrastructure.security;

import in.elcot.avgcxr.platform.user.application.port.output.UserRepositoryPort;
import in.elcot.avgcxr.platform.user.domain.model.User;
import in.elcot.avgcxr.platform.user.domain.model.UserId;
import in.elcot.avgcxr.platformsecurity.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserDetailsService userDetailsService;
  private final UserRepositoryPort userRepo;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String token = extractToken(request);
      if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
        String userIdStr = jwtTokenProvider.getUserIdFromToken(token);
        UserDetails userDetails = resolveUser(userIdStr, token);
        if (userDetails != null) {
          var authentication =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    } catch (Exception ex) {
      log.error("Cannot set user authentication: {}", ex.getMessage());
      SecurityContextHolder.clearContext();
    }
    filterChain.doFilter(request, response);
  }

  /**
   * Try Flowable's UserDetailsService first, then fall back to our users table. The JWT subject is
   * the user's UUID.
   */
  private UserDetails resolveUser(String userIdStr, String token) {
    // 1) Try Flowable (might not know about this user)
    try {
      UserDetails ud = userDetailsService.loadUserByUsername(userIdStr);
      if (ud != null) return ud;
    } catch (Exception e) {
      log.debug("Flowable UserDetailsService rejected {}: {}", userIdStr, e.getMessage());
    }

    // 2) Try our users table
    try {
      UUID id = UUID.fromString(userIdStr);
      User user = userRepo.findById(UserId.of(id.toString())).orElse(null);
      if (user == null) {
        log.debug("JWT subject {} not found in our users table", userIdStr);
        return null;
      }
      // Build UserDetails from domain User
      String role =
          user.getRoles() != null && !user.getRoles().isEmpty()
              ? user.getRoles().iterator().next()
              : "APPLICANT";
      return org.springframework.security.core.userdetails.User.builder()
          .username(userIdStr)
          .password(user.getId().value().toString())
          .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + role)))
          .build();
    } catch (Exception e) {
      log.debug("Could not resolve user from JWT subject {}: {}", userIdStr, e.getMessage());
      return null;
    }
  }

  private String extractToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return path.startsWith("/api/public/")
        || path.startsWith("/api/auth/")
        || path.startsWith("/actuator/health")
        || path.startsWith("/actuator/info");
  }
}
