package com.example.Deal;

import com.redis.testcontainers.RedisContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;

public abstract class AbstractContainer  {

    private static RedisContainer redisContainer;

    private static RabbitMQContainer rabbitContainer;

    private static PostgreSQLContainer<?> psqlContainer;

    static {
        redisContainer = new RedisContainer("redis:latest");
        rabbitContainer = new RabbitMQContainer("rabbitmq:3-management");
        psqlContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("testDatabase")
                .withUsername("testUser")
                .withPassword("testPass");

        redisContainer.start();
        rabbitContainer.start();
        psqlContainer.start();
        updateProperties();
    }

    private static void updateProperties() {
        System.setProperty("spring.datasource.url", psqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", psqlContainer.getUsername());
        System.setProperty("spring.datasource.password", psqlContainer.getPassword());
        System.setProperty("token.secret.key", "secret");
        System.setProperty("app.rabbit.host", rabbitContainer.getHost());
        System.setProperty("app.rabbit.port", rabbitContainer.getAmqpPort().toString());
        System.setProperty("app.rabbit.deadQueue", "test_dead_queue");
        System.setProperty("app.rabbit.dlx", "test_dead_letter_exchange");
        System.setProperty("app.rabbit.contractorDlx", "test_contractor_dead_letter_excahnge");
        System.setProperty("app.rabbit.queue", "test_queue");
        System.setProperty("app.rabbit.exchange", "test_exchange");
        System.setProperty("app.schedule.fixedDelay", "1000");
        System.setProperty("app.schedule.initialDelay", "1000");
        System.setProperty("spring.data.redis.host", redisContainer.getHost());
        System.setProperty("spring.data.redis.port", redisContainer.getMappedPort(6379).toString());
    }

}
