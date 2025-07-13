package com.example.Deal.Repository;

import com.example.Deal.DTO.ContractorToRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorToRoleRepository extends JpaRepository<ContractorToRole, ContractorToRole.Key> {
}
