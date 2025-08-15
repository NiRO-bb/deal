package com.example.Deal.RabbitMQ;

import com.example.Deal.ContextSetup;
import com.example.Deal.DTO.RabbitContractor;
import com.example.Deal.Service.DealContractorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@ExtendWith(ContextSetup.class)
public class RabbitConsumerTest {

    private final int sleepTime = 2;

    @Autowired
    private RabbitProducer producer;

    @MockitoBean
    private DealContractorService mockService;

    @Test
    public void testConsuming() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Mockito.when(mockService.update(anyString(), anyString(), anyString()))
                .thenAnswer(answer -> {
                    latch.countDown();
                    return null;
                });
        RabbitContractor contractor = getTestContractor();
        producer.send(contractor);
        boolean isConsumed = latch.await(sleepTime, TimeUnit.SECONDS);
        Assertions.assertTrue(isConsumed);
    }

    private RabbitContractor getTestContractor() {
        return new RabbitContractor(
                "test",
                null,
                "test",
                "test",
                "test",
                "test",
                "test",
                0,
                0,
                new Date(),
                new Date(),
                "test",
                "test",
                true
        );
    }

}
