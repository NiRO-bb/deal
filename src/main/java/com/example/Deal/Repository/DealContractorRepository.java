package com.example.Deal.Repository;

import com.example.Deal.DTO.DealContractor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DealContractorRepository extends CrudRepository<DealContractor, UUID> {

    List<DealContractor> findByDealIdAndIsActive(UUID dealId, boolean isActive);

    long countByDealIdAndMainIsTrue(UUID dealId);

}
