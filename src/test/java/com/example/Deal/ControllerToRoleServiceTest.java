package com.example.Deal;

import com.example.Deal.DTO.ContractorRole;
import com.example.Deal.DTO.ContractorToRole;
import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.DealContractor;
import com.example.Deal.Repository.ContractorRoleRepository;
import com.example.Deal.Repository.ContractorToRoleRepository;
import com.example.Deal.Repository.DealContractorRepository;
import com.example.Deal.Repository.DealRepository;
import com.example.Deal.Service.ContractorToRoleService;
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
public class ControllerToRoleServiceTest {

    private static UUID dealId;
    private static UUID contractorId;
    private static ContractorToRole.Key key;

    @Autowired
    private ContractorToRoleService service;

    @BeforeAll
    public static void setup(@Autowired DealRepository dealRepo,
                             @Autowired DealContractorRepository dealContractorRepo,
                             @Autowired ContractorToRoleRepository contractorToRoleRepo,
                             @Autowired ContractorRoleRepository contractorRoleRepo) {
        Deal deal = new Deal();
        dealId = deal.getId();
        dealRepo.save(deal);

        DealContractor contractor = new DealContractor();
        contractorId = contractor.getId();
        contractor.setDealId(dealId);
        contractor.setName("name");
        contractor.setContractorId("1");
        contractor.setMain(true);
        dealContractorRepo.save(contractor);

        key = contractorToRoleRepo.save(new ContractorToRole(
                new ContractorToRole.Key(contractorId, "BORROWER"), contractor, contractorRoleRepo.findById("BORROWER").get(), true)).getKey();
    }

    @Test
    public void testAdd() {
        Optional<ContractorToRole> contractorToRole = service.add(new ContractorToRole.Key(contractorId, "BORROWER"));
        Assertions.assertEquals(contractorId, contractorToRole.get().getKey().getContractorId());
    }

    @Test
    public void testDeleteSuccess() {
        Optional<ContractorToRole> result = service.delete(key);
        Assertions.assertFalse(result.get().isActive());
    }

    @Test
    public void testDeleteFailure() {
        Assertions.assertEquals(Optional.empty(), service.delete(new ContractorToRole.Key(UUID.randomUUID(), "BORROWER")));
    }

}
