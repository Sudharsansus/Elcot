package in.elcot.avgcxr.common.infrastructure.security.sso;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * On successful OIDC/SSO login, mints the portal's own JWT (same shape as password login — subject
 * = email) so the federated identity flows through the existing {@code JwtAuthenticationFilter} and
 * RBAC unchanged, then redirects the browser to the configured frontend callback with the token.
 *
 * <p>This is what bridges an external ELCOT IdP (or Keycloak) into the portal's stateless JWT
 * world.
 */
@Component
public class OidcLoginSuccessHandler implements AuthenticationSuccessHandler {

  private static final Logger log = LoggerFactory.getLogger(OidcLoginSuccessHandler.class);

  private final JwtEncoder jwtEncoder;
  private final String successRedirect;

  public OidcLoginSuccessHandler(
      JwtEncoder jwtEncoder, @Value("${avgcxr.sso.success-redirect:/}") String successRedirect) {
    this.jwtEncoder = jwtEncoder;
    this.successRedirect = successRedirect;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    String subject = authentication.getName();
    if (authentication.getPrincipal() instanceof OAuth2User user) {
      Object email = user.getAttributes().get("email");
      if (email != null) {
        subject = email.toString();
      }
    }
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .subject(subject)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(1800))
            .claim("sso", true)
            .build();
    String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    log.info("SSO login success for {} — issued portal JWT", subject);

    String target =
        UriComponentsBuilder.fromUriString(successRedirect)
            .queryParam("token", URLEncoder.encode(token, StandardCharsets.UTF_8))
            .build()
            .toUriString();
    response.sendRedirect(target);
  }
}
