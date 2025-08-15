package com.example.Deal.Repository;

import com.example.Deal.DTO.DealContractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DealContractorRepository extends JpaRepository<DealContractor, UUID> {

    long countByDealIdAndMainIsTrue(UUID dealId);

    @Modifying
    @Query("""
            UPDATE DealContractor dc
            SET name = :name, inn = :inn
            WHERE dc.contractorId = :contractorId
            """)
    int updateByCustomerId(@Param("contractorId") String contractorId,
                           @Param("name") String name,
                           @Param("inn") String inn);

}
