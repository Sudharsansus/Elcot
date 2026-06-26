package in.elcot.avgcxr.common.infrastructure.security.sso;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ELCOT SSO federation (tender §IV.3.7). A dedicated, higher-precedence filter chain that handles
 * only the OIDC authorization/callback endpoints and, on success, mints the portal's own JWT (see
 * {@link OidcLoginSuccessHandler}).
 *
 * <p>Activated ONLY when an OIDC client is configured (an ELCOT IdP or Keycloak) via {@code
 * spring.security.oauth2.client.registration.keycloak.client-id}. When absent — the default state,
 * including the current live deploy — this configuration is not created at all, so the existing
 * email/password JWT login is completely untouched. Adding the OIDC client env vars is all that is
 * required to turn federated SSO on.
 */
@Configuration
@ConditionalOnProperty(name = "spring.security.oauth2.client.registration.keycloak.client-id")
public class SsoSecurityConfig {

  @Bean
  @Order(1)
  public SecurityFilterChain ssoFilterChain(
      HttpSecurity http, OidcLoginSuccessHandler successHandler) throws Exception {
    return http.securityMatcher("/oauth2/authorization/**", "/login/oauth2/code/**", "/sso/**")
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .oauth2Login(oauth -> oauth.successHandler(successHandler))
        .build();
  }
}
