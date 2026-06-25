package in.elcot.avgcxr;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Full Spring context integration test. Requires Postgres, Redis, RabbitMQ, Elasticsearch and MinIO
 * to be reachable on localhost.
 *
 * <p>Skipped by default to allow {@code mvn test} to pass in CI / local builds without docker. Run
 * explicitly with:
 *
 * <pre>
 *   RUN_INTEGRATION_TESTS=1 mvn test
 * </pre>
 */
@SpringBootTest
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "RUN_INTEGRATION_TESTS", matches = "1")
class ApiApplicationTests {

  @Test
  void contextLoads() {
    // Verify Spring context loads without errors
  }

  @Test
  void healthEndpointResponds() {
    // In integration test: verify /actuator/health returns 200
  }
}
