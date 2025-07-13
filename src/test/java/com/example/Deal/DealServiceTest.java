package com.example.Deal;

import com.example.Deal.DTO.Deal;
import com.example.Deal.Repository.DealRepository;
import com.example.Deal.Service.DealService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ExtendWith(DatabaseSetup.class)
public class DealServiceTest {

    private static UUID dealId;

    @Autowired
    private DealService service;

    @BeforeAll
    public static void setup(@Autowired DealRepository repository) {
        Deal deal = new Deal();
        dealId = deal.getId();
        deal.setStatusId("DRAFT");
        repository.save(deal);
    }

    @Test
    public void testSave() {
        Deal deal = new Deal();
        deal.setDescription("test");

        Deal result = service.save(deal);
        Assertions.assertEquals("test", result.getDescription());
    }

    @Test
    public void testChangeSuccess() {
        Optional<Deal> deal = service.change(new Deal.DealStatusUpdate(dealId, "CLOSED"));
        Assertions.assertEquals("CLOSED", deal.get().getStatusId());
        service.change(new Deal.DealStatusUpdate(dealId, "DRAFT"));
    }

    @Test
    public void testChangeNull() {
        Assertions.assertEquals(Optional.empty(),
                service.change(new Deal.DealStatusUpdate(UUID.randomUUID(), "test")));
    }

}
