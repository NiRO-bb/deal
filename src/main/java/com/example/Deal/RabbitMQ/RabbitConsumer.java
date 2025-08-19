package com.example.Deal.RabbitMQ;

import com.example.Deal.DTO.RabbitContractor;
import com.example.Deal.DTO.RabbitMessage;
import com.example.Deal.Service.DealContractorService;
import com.example.Deal.Service.InboxService;
import com.example.Deal.Utils.JsonUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Releases message consuming from RabbitMQ.
 */
@Component
public class RabbitConsumer {

    private final Logger logger = LogManager.getLogger();

    private final ConnectionFactory factory = new ConnectionFactory();

    private final int timeout = 300000;

    @Autowired
    private InboxService inboxService;

    @Autowired
    private DealContractorService service;

    @Value("${app.rabbit.queue}")
    private String queue;

    @Value("${app.rabbit.deadQueue}")
    private String deadQueue;

    @Value("${app.rabbit.dlx}")
    private String deadLetterExchange;

    @Value("${app.rabbit.contractorDlx}")
    private String contractorDeadLetterExchange;

    public RabbitConsumer(@Value("${app.rabbit.host}") String host,
                          @Value("${app.rabbit.port}") int port) {
        factory.setHost(host);
        factory.setPort(port);
    }

    /**
     * Consumes messages from RabbitMQ queue.
     */
    public void consume() throws IOException, TimeoutException {
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        configConsumer(channel);
        channel.basicConsume(queue, false, deliverCallback(channel), consumerTag -> {});
    }

    /**
     * Sets some consumer configs.
     * Creates queues and exchangers.
     *
     * @param channel
     */
    private void configConsumer(Channel channel) throws IOException {
        Map<String, Object> queueArgs = new HashMap<>();
        queueArgs.put("x-dead-letter-exchange", deadLetterExchange);
        queueArgs.put("x-dead-letter-routing-key", deadQueue);

        Map<String, Object> deadQueueArgs = new HashMap<>();
        deadQueueArgs.put("x-dead-letter-exchange", contractorDeadLetterExchange);
        deadQueueArgs.put("x-dead-letter-routing-key", queue);
        deadQueueArgs.put("x-message-ttl", timeout);

        queueDeclare(channel, queue, queueArgs);
        queueDeclare(channel, deadQueue, deadQueueArgs);

        exchangeDeclare(channel, deadLetterExchange);
        exchangeDeclare(channel, contractorDeadLetterExchange);

        queueBind(channel, deadQueue, deadLetterExchange);
        queueBind(channel, queue, contractorDeadLetterExchange);
    }

    /**
     * Declares queue in RabbitMQ (creates if not exists, otherwise does nothing).
     *
     * @param channel
     * @param queue name of queue must be declared
     * @param args config of declaring queue
     */
    private void queueDeclare(Channel channel, String queue, Map<String, Object> args) throws IOException {
        channel.queueDeclare(queue, true, false, false, args);
    }

    /**
     * Declares exchange in RabbitMQ (creates if not exists, otherwise does nothing).
     *
     * @param channel
     * @param exchange name of declaring exchange
     */
    private void exchangeDeclare(Channel channel, String exchange) throws IOException {
        channel.exchangeDeclare(exchange, "direct", true);
    }

    /**
     * Binds existing queue with exchange.
     *
     * @param channel
     * @param queue name of queue must be bind
     * @param exchange name of exchange must be bind
     */
    private void queueBind(Channel channel, String queue, String exchange) throws IOException {
        channel.queueBind(queue, exchange, queue);
    }

    /**
     * Creates DeliverCallback instance.
     * Defines how to acknowledge or reject received message.
     *
     * @param channel
     * @return
     */
    private DeliverCallback deliverCallback(Channel channel) {
        return (consumerTag, message) -> {
            try {
                RabbitContractor contr = getContractor(message);
                logger.trace("Message was received - {}",
                        String.format("%s %s %s", contr.getId(), contr.getName(), contr.getInn()));

                if (isActual(message)) {
                    int updatedEntitiesNumber =
                            service.update(contr.getId(), contr.getName(), contr.getInn());
                    logger.info("DealContractor entities was updated - {} entity(-ies)", updatedEntitiesNumber);
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                } else {
                    logger.trace("Received message is no longer actual");
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                }
            } catch (IOException exception) {
                logger.error("Error occurred when deserializing received message - {}", exception.getMessage());
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } catch (DataAccessException exception) {
                logger.error("Error occurred when updating DealContractor entities - {}", exception.getMessage());
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            }
        };
    }

    /**
     * Checks if passed message is actual (should use to update or not).
     *
     * @param message message must be checked
     * @return
     */
    private boolean isActual(Delivery message) throws IOException {
        RabbitContractor contractor = getContractor(message);
        return inboxService.isActual(new RabbitMessage(
                contractor.getId(),
                new Timestamp(message.getProperties().getTimestamp().getTime())));
    }

    /**
     * Deserializes passed message to RabbitContractor.
     *
     * @param message received message must be deserialized
     * @return RabbitContractor instance
     */
    private RabbitContractor getContractor(Delivery message) throws IOException {
        return (RabbitContractor) JsonUtil.deserialize(message.getBody(), RabbitContractor.class);
    }

}
