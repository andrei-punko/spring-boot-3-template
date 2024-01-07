package by.andd3dfx.templateapp;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

/**
 * According to https://reflectoring.io/spring-boot-flyway-testcontainers/
 */
public class IntegrationTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        startContainers();
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        MapPropertySource propertySource = new MapPropertySource(
                "test-containers", (Map) createConnectionConfiguration()
        );
        environment.getPropertySources().addFirst(propertySource);
    }

    private static void startContainers() {
        Startables.deepStart(Stream.of(postgres)).join();
        // we can add further containers
        // here like rabbitmq or other databases
    }

    private static Map<String, String> createConnectionConfiguration() {
        return Map.of(
                "DB_URL", postgres.getJdbcUrl(),
                "DB_USERNAME", postgres.getUsername(),
                "DB_PASSWORD", postgres.getPassword(),
                "DRIVER_CLASS_NAME", postgres.getDriverClassName()
        );
    }
}
