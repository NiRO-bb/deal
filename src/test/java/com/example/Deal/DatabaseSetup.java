package com.example.Deal;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseSetup implements BeforeAllCallback {

    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testDatabase")
            .withUsername("testUser")
            .withPassword("testPass");

    @Override
    public void beforeAll(ExtensionContext context) {
        container.start();
        updateProperties(container);
    }

    private void updateProperties(PostgreSQLContainer<?> container) {
        System.setProperty("spring.datasource.url", container.getJdbcUrl());
        System.setProperty("spring.datasource.username", container.getUsername());
        System.setProperty("spring.datasource.password", container.getPassword());
    }
}
