package com.example.Deal.Repository;

import com.example.Deal.DTO.ContractorRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContractorRoleRepository extends CrudRepository<ContractorRole, String> {

    List<ContractorRole> findAllById(String roleId);

}
