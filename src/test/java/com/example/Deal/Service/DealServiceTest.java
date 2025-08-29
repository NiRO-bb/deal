package com.example.Deal.Service;

import com.example.Deal.AbstractContainer;
import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.request.ChangeStatus;
import com.example.Deal.Repository.DealRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class DealServiceTest extends AbstractContainer {

    @Autowired
    private DealService service;

    @Autowired
    private DealRepository repository;

    @Test
    public void testSave() {
        Deal deal = new Deal();
        deal.setDescription("test");

        Deal result = service.save(deal);
        Assertions.assertEquals("test", result.getDescription());
    }

    @Test
    public void testChangeSuccess() {
        Deal deal = new Deal();
        UUID dealId = deal.getId();
        deal.setStatusId("DRAFT");
        repository.save(deal);
        Optional<Deal> optDeal = service.change(new ChangeStatus(dealId, "CLOSED"));
        Assertions.assertEquals("CLOSED", optDeal.get().getStatusId());
    }

    @Test
    public void testChangeNull() {
        Assertions.assertEquals(Optional.empty(),
                service.change(new ChangeStatus(UUID.randomUUID(), "test")));
    }

}
