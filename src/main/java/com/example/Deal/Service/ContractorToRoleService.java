package com.example.Deal.Service;

import com.example.Deal.DTO.ContractorRole;
import com.example.Deal.DTO.ContractorToRole;
import com.example.Deal.DTO.DealContractor;
import com.example.Deal.Repository.ContractorRoleRepository;
import com.example.Deal.Repository.ContractorToRoleRepository;
import com.example.Deal.Repository.DealContractorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Provides access to repository-layer
 */
@Service
public class ContractorToRoleService {

    private final ContractorToRoleRepository repository;
    private final DealContractorRepository dealRepo;
    private final ContractorRoleRepository roleRepo;

    @Autowired
    public ContractorToRoleService(ContractorToRoleRepository repository,
                                   DealContractorRepository dealRepo, ContractorRoleRepository roleRepo) {
        this.repository = repository;
        this.dealRepo = dealRepo;
        this.roleRepo = roleRepo;
    }

    /**
     * Adds new role to existing contractor.
     *
     * @param key contains contractor id and new role for him
     * @return added ContractorToRole instance
     */
    public Optional<ContractorToRole> add(ContractorToRole.Key key) {
        Optional<DealContractor> contractor = dealRepo.findById(key.getContractorId());
        Optional<ContractorRole> role = roleRepo.findById(key.getRoleId());
        if (contractor.isPresent() && role.isPresent()) {
            return Optional.of(repository.save(new ContractorToRole(key, contractor.get(), role.get(), true)));
        } else {
            return Optional.empty();
        }
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
