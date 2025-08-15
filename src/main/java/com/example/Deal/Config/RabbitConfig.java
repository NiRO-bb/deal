package com.example.Deal.Config;

import com.example.Deal.RabbitMQ.RabbitConsumer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class RabbitConfig {

    @Autowired
    private RabbitConsumer consumer;

    @PostConstruct
    public void postConstruct() throws IOException, TimeoutException {
        consumer.consume();
    }

}
