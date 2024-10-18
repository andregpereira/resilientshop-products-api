package com.github.andregpereira.resilientshop.productsapi.infra.repositories.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

public abstract class PostgreSQLContainerConfig {

    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:alpine"));

    static {
        Startables
            .deepStart(POSTGRESQL_CONTAINER)
            .join();
    }

    public static class PostgreSQLContainerInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues
                .of(
                    "spring.datasource.url=".concat(POSTGRESQL_CONTAINER.getJdbcUrl()),
                    "spring.datasource.username=".concat(POSTGRESQL_CONTAINER.getUsername()),
                    "spring.datasource.password=".concat(POSTGRESQL_CONTAINER.getPassword()),
                    "spring.test.database.replace=none"
                )
                .applyTo(applicationContext.getEnvironment());
        }

    }

}
