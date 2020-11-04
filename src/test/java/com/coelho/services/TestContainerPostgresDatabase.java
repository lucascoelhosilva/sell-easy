package com.coelho.services;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.HashMap;
import java.util.Map;

@QuarkusTestResource(TestContainerPostgresDatabase.Initializer.class)
public class TestContainerPostgresDatabase {

    public static class Initializer implements QuarkusTestResourceLifecycleManager {

        private PostgreSQLContainer db;

        @Override
        public Map<String, String> start() {
            this.db = new PostgreSQLContainer<>("postgres");
            db.start();

            return configurationParameters();
        }

        @Override
        public void stop() {
            if (db != null) {
                db.stop();
            }
        }

        private Map<String, String> configurationParameters() {
            final Map<String, String> config = new HashMap<>();
            config.put("quarkus.datasource.jdbc.url", db.getJdbcUrl());
            config.put("quarkus.datasource.username", db.getUsername());
            config.put("quarkus.datasource.password", db.getPassword());

            return config;
        }
    }
}
