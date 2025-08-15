package com.example.Deal.Service;

import com.example.Deal.ContextSetup;
import com.example.Deal.DTO.RabbitMessage;
import com.example.Deal.Repository.RabbitMessageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Date;

@SpringBootTest
@ExtendWith(ContextSetup.class)
public class InboxServiceTest {

    @Autowired
    private InboxService service;

    @Autowired
    private RabbitMessageRepository repository;

    @Test
    public void testActual() {
        RabbitMessage message = new RabbitMessage("actual",
                new Timestamp(System.currentTimeMillis() - 100000));
        repository.save(message);

        message.setTime(new Timestamp(System.currentTimeMillis()));
        Assertions.assertTrue(service.isActual(message));
    }

    @Test
    public void testNotActual() {
        RabbitMessage message = new RabbitMessage("not_actual",
                new Timestamp(System.currentTimeMillis()));
        repository.save(message);

        message.setTime(new Timestamp(System.currentTimeMillis() - 100000));
        Assertions.assertFalse(service.isActual(message));
    }

    private RabbitMessage getTestMessage(String id, Date date) {
        return new RabbitMessage("test", new Timestamp(date.getTime()));
    }

}
