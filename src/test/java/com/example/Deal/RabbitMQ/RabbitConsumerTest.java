package com.example.Deal.RabbitMQ;

import com.example.Deal.AbstractContainer;
import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.DealContractor;
import com.example.Deal.DTO.rabbit.RabbitContractor;
import com.example.Deal.Repository.DealContractorRepository;
import com.example.Deal.Service.DealService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.Deal.util.TestUtil.getDeal;
import static com.example.Deal.util.TestUtil.getDealContractor;
import static com.example.Deal.util.TestUtil.getRabbitContractor;

@SpringBootTest
public class RabbitConsumerTest extends AbstractContainer {

    private final int sleepTime = 2000;

    @Autowired
    private RabbitProducer producer;

    @Autowired
    private DealContractorRepository repository;

    @Autowired
    private DealService service;

    @Test
    public void testConsuming() throws Exception {
        Deal deal = service.save(getDeal());
        DealContractor contractor = repository.save(getDealContractor(deal.getId()));
        RabbitContractor rContractor = getRabbitContractor();

        producer.send(rContractor);
        Thread.sleep(sleepTime);
        String name = repository.findById(contractor.getId()).get().getName();
        Assertions.assertEquals("new test", name);
    }

}
