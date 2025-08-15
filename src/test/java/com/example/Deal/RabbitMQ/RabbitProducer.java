package com.example.Deal.RabbitMQ;

import com.example.Deal.DTO.RabbitContractor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitProducer {

    private final ConnectionFactory factory = new ConnectionFactory();

    private final long timeout = 1000;

    @Value("${app.rabbit.exchange}")
    private String exchange;

    @Value("${app.rabbit.queue}")
    private String queue;

    public RabbitProducer(@Value("${app.rabbit.host}") String host,
                          @Value("${app.rabbit.port}") int port) {
        factory.setHost(host);
        factory.setPort(port);
    }

    public void send(RabbitContractor message) throws IOException, TimeoutException, InterruptedException {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            configProducer(channel);
            AMQP.BasicProperties props = new AMQP.BasicProperties().builder().timestamp(new Date()).build();
            channel.basicPublish(exchange, queue, props, serialize(message));
        }
    }

    private byte[] serialize(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(object);
    }

    private void configProducer(Channel channel) throws IOException {
        exchangeDeclare(channel);
        queueBind(channel);
    }

    private void exchangeDeclare(Channel channel) throws IOException {
        channel.exchangeDeclare(exchange, "direct", true);
    }

    private void queueBind(Channel channel) throws IOException {
        channel.queueBind(queue, exchange, queue);
    }

}
