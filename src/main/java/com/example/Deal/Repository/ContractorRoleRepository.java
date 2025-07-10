package com.example.Deal.Repository;

import com.example.Deal.DTO.ContractorRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractorRoleRepository extends CrudRepository<ContractorRole, String> {

    List<ContractorRole> findAllById(String roleId);

}
