package com.example.Deal.Repository;

import com.example.Deal.DTO.DealContractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DealContractorRepository extends JpaRepository<DealContractor, UUID> {

    long countByDealIdAndMainIsTrue(UUID dealId);

}
