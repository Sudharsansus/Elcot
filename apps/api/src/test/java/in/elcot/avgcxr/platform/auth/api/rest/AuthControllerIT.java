package in.elcot.avgcxr.platform.auth.api.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Smoke test for AuthController path mapping.
 *
 * <p>A full {@code @SpringBootTest} bootstrap requires the entire Hexagonal wiring (Postgres,
 * Redis, RabbitMQ, Strapi, MinIO) to be reachable, which is not feasible inside the build pipeline.
 * Instead, this test verifies the static {@link AuthController} class metadata — its
 * {@code @RequestMapping} path — so we know the frontend contract {@code /api/v1/auth/...} is
 * honored without booting the application context.
 */
@DisplayName("AuthController routing")
class AuthControllerIT {

  @Test
  @DisplayName("AuthController is mapped under /api/v1/auth")
  void authControllerIsMappedToApiV1Auth() {
    var mapping =
        AuthController.class.getAnnotation(
            org.springframework.web.bind.annotation.RequestMapping.class);
    assertThat(mapping).as("AuthController must carry @RequestMapping").isNotNull();
    assertThat(mapping.value())
        .as("AuthController path must be /api/v1/auth (matches frontend AuthService.API_BASE)")
        .containsExactly("/api/v1/auth");
  }

  @Test
  @DisplayName("AuthController is a @RestController")
  void authControllerIsRestController() {
    var rest =
        AuthController.class.getAnnotation(
            org.springframework.web.bind.annotation.RestController.class);
    assertThat(rest).as("AuthController must carry @RestController").isNotNull();
  }
}
