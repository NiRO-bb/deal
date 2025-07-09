package com.example.Deal.Service;

import com.example.Deal.DTO.ContractorToRole;
import com.example.Deal.Repository.ContractorToRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Provides access to repository-layer
 */
@Service
public class ContractorToRoleService {

    private final ContractorToRoleRepository repository;

    @Autowired
    public ContractorToRoleService(ContractorToRoleRepository repository) {
        this.repository = repository;
    }

    /**
     * Adds new role to existing contractor.
     *
     * @param contractorToRole contains contractor id and new role for him
     * @return added ContractorToRole instance
     */
    public ContractorToRole add(ContractorToRole.Key contractorToRole) {
        return repository.save(new ContractorToRole(contractorToRole, true));
    }

    /**
     * Logically deletes ContractorToRole entity.
     * <p>
     * Updates 'is_active' field value to 'false'.
     *
     * @param contractorToRole contains contractor id and role that must be deleted
     * @return deleted instance or null - if could not find entity with passed id
     */
    public ContractorToRole delete(ContractorToRole.Key contractorToRole) {
        Optional<ContractorToRole> optContractorToRole = repository.findById(contractorToRole);
        if (optContractorToRole.isPresent()) {
            ContractorToRole result = optContractorToRole.get();
            result.setActive(false);
            return repository.save(result);
        } else {
            return null;
        }
    }

}
