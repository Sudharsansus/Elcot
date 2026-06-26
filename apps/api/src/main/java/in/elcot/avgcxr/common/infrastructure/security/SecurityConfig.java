package in.elcot.avgcxr.common.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthRateLimitFilter authRateLimitFilter;

  @Bean
  @org.springframework.context.annotation.Lazy
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth
                    // Public endpoints (no auth)
                    .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/login"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/register"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/refresh-token"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/forgot-password"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/reset-password"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/v1/auth/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/v1/public/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/public/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/auth/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/actuator/health"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/actuator/info"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/v1/mira/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/v1/chat/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/v1/reports/export/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/v1/schemes/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/v1/public/schemes/**"))
                    .permitAll()
                    // AVGC-XR Bridge directory: public to browse (GET), auth to register (POST)
                    .requestMatchers(new AntPathRequestMatcher("/api/v1/bridge/**", "GET"))
                    .permitAll()
                    // Internal notification delivery (worker callback) — self-validates
                    // X-Internal-Token; closed (503) until the token is configured
                    .requestMatchers(
                        new AntPathRequestMatcher("/api/v1/notifications/email", "POST"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/v1/notifications/sms", "POST"))
                    .permitAll()
                    .requestMatchers(
                        new AntPathRequestMatcher("/api/v1/notifications/push", "POST"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/swagger-ui.html"))
                    .permitAll()
                    // Everything else requires authentication
                    .requestMatchers(new AntPathRequestMatcher("/**"))
                    .authenticated())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(authRateLimitFilter, JwtAuthenticationFilter.class)
        .headers(
            headers ->
                headers
                    .contentSecurityPolicy(
                        csp ->
                            csp.policyDirectives(
                                "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'; "
                                    + "img-src 'self' data: blob:; font-src 'self' data:; "
                                    + "connect-src 'self'; frame-ancestors 'none'; base-uri 'self'; "
                                    + "form-action 'self'"))
                    .frameOptions(fo -> fo.deny()))
        .build();
  }
}
