package in.elcot.avgcxr.common.infrastructure.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

/**
 * Encoder configuration, separated from SecurityConfig to avoid circular dependencies with
 * Flowable's IDM engine wiring.
 */
@Configuration
public class PasswordEncoderConfig {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    SecretKeySpec key = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    return new NimbusJwtEncoder(new ImmutableSecret<>(key));
  }
}
