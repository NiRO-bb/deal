package com.example.Deal;

import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.DealContractor;
import com.example.Deal.Repository.DealContractorRepository;
import com.example.Deal.Repository.DealRepository;
import com.example.Deal.Service.DealContractorService;
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
public class DealContractorServiceTest {

    private static UUID dealId;
    private static UUID contractorId;

    @Autowired
    private DealContractorService service;

    @BeforeAll
    public static void setup(@Autowired DealRepository dealRepo,
                             @Autowired DealContractorRepository dealContractorRepo) {
        Deal deal = new Deal();
        dealId = deal.getId();
        dealRepo.save(deal);

        DealContractor mainContractor = new DealContractor();
        mainContractor.setDealId(dealId);
        mainContractor.setName("name");
        mainContractor.setContractorId("1");
        mainContractor.setMain(true);
        dealContractorRepo.save(mainContractor);

        DealContractor deleteContractor = new DealContractor();
        contractorId = deleteContractor.getId();
        deleteContractor.setDealId(dealId);
        deleteContractor.setName("name");
        deleteContractor.setContractorId("1");
        dealContractorRepo.save(deleteContractor);
    }

    @Test
    public void testSaveWithMainIsFalse() {
        DealContractor dealContractor = new DealContractor();
        dealContractor.setDealId(dealId);
        dealContractor.setName("name");
        dealContractor.setContractorId("1");
        Optional<DealContractor> result = service.save(dealContractor);
        Assertions.assertEquals(dealId, result.get().getDealId());
    }

    @Test
    public void testSaveWithMainIsTrue() {
        DealContractor dealContractor = new DealContractor();
        dealContractor.setDealId(dealId);
        dealContractor.setMain(true);
        Assertions.assertEquals(Optional.empty(), service.save(dealContractor));
    }

    @Test
    public void testDeleteSuccess() {
        Optional<DealContractor> result = service.delete(contractorId);
        Assertions.assertEquals(contractorId, result.get().getId());
    }

    @Test
    public void testDeleteFailure() {
        Assertions.assertEquals(Optional.empty(), service.delete(UUID.randomUUID()));
    }

}
