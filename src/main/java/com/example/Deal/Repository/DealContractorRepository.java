package com.example.Deal.Repository;

import com.example.Deal.DTO.DealContractor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface DealContractorRepository extends CrudRepository<DealContractor, UUID> {

    List<DealContractor> findByDealIdAndIsActive(UUID dealId, boolean isActive);

    long countByDealIdAndMainIsTrue(UUID dealId);

}
