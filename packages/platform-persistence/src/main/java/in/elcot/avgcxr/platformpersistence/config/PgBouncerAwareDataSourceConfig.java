package in.elcot.avgcxr.platformpersistence.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * DataSource configuration optimized for PgBouncer connection pooling.
 *
 * <p>When PgBouncer is used in transaction pooling mode, prepared statements must be disabled
 * (prepareThreshold=0) because PgBouncer does not support prepared statement proxying in
 * transaction mode.
 *
 * <p>Activated by the "pgbouncer" Spring profile (default for staging/prod).
 */
@Configuration
@Profile("pgbouncer")
public class PgBouncerAwareDataSourceConfig {

  @Bean
  @ConfigurationProperties("spring.datasource.hikari")
  public DataSource pgbouncerDataSource(DataSourceProperties properties) {
    HikariDataSource dataSource =
        properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    dataSource.addDataSourceProperty("prepareThreshold", "0");
    return dataSource;
  }
}
