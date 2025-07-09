package com.example.Deal.Repository;

import com.example.Deal.DTO.ContractorToRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ContractorToRoleRepository extends CrudRepository<ContractorToRole, ContractorToRole.Key> {

    List<ContractorToRole> findByKeyContractorId(UUID contractorId);

}
