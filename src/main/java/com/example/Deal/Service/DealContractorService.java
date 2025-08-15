package com.example.Deal.Service;

import com.example.Deal.DTO.DealContractor;
import com.example.Deal.Repository.DealContractorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Provides access to repository-layer
 */
@Service
@RequiredArgsConstructor
public class DealContractorService {

    private final DealContractorRepository dealContractorRepo;

    /**
     * Adds and updates DealContractor entities.
     * <p>
     * Also check count of 'main contractors' (value of 'main' field = true) for each Deal entity.
     * Only one 'main contractor' can exist for every Deal.
     *
     * @param contractor instance that must be saved or updated
     * @return added/updated instance or null - if 'main' condition was violate
     */
    public Optional<DealContractor> save(DealContractor contractor) {
        if (contractor.isMain()) {
            if (dealContractorRepo.countByDealIdAndMainIsTrue(contractor.getDealId()) == 0) {
                return Optional.of(dealContractorRepo.save(contractor));
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.of(dealContractorRepo.save(contractor));
        }
    }

    /**
     * Logically deletes DealContractor entities.
     * <p>
     * Updates 'is_active' field value to 'false' value.
     *
     * @param id value of 'id' field of 'DealContractor' that must be deleted
     * @return deleted instance - if successful, null - if could not find entity with passed id
     */
    public Optional<DealContractor> delete(UUID id) {
        Optional<DealContractor> optContractor = dealContractorRepo.findById(id);
        if (optContractor.isPresent()) {
            DealContractor contractor = optContractor.get();
            contractor.setActive(false);
            return Optional.of(dealContractorRepo.save(contractor));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Updates DealContractor instances by passed contractorId value.
     * Updates fields 'name' and 'inn' of entities with matched contractorId.
     *
     * @return updated entities amount
     * @param contractorId value of contractorId field
     * @param name new value of name field
     * @param inn new value of inn field
     */
    @Transactional
    public int update(String contractorId, String name, String inn) throws DataAccessException {
        return dealContractorRepo.updateByCustomerId(contractorId, name, inn);
    }

}
