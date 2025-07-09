package com.example.Deal.Repository;

import com.example.Deal.DTO.Deal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DealRepository extends CrudRepository<Deal, UUID> {

    @Query("""
            select d from Deal d
            where isActive = :is_active and
            (:id is null or id = :id) and
            (:description is null or description = :description) and
            (agreementNumber ilike coalesce(:agreement_number, '%')) and
            (cast(:agreement_date_start as date) is null or agreementDate >= :agreement_date_start) and
            (cast(:agreement_date_end as date) is null or agreementDate <= :agreement_date_end) and
            (cast(:availability_date_start as date) is null or availabilityDate >= :availability_date_start) and
            (cast(:availability_date_end as date) is null or availabilityDate <= :availability_date_end) and
            (cast(:close_dt_start as date) is null or cast(closeDt as date) >= :close_dt_start) and
            (:type is null or typeId in :type) and
            (:status is null or statusId in :status) and
            (cast(:close_dt_end as date) is null or cast(closeDt as date) <= :close_dt_end) and
            id = any (
                select distinct dc.dealId from DealContractor dc
                where dc.id = any (
                    select ctr.key.contractorId from ContractorToRole ctr
                    where ctr.key.roleId = any (
                        select cr.id from ContractorRole cr where cr.category = 'BORROWER'
                    )
                ) and
                (:borrower_search is null or
                dc.contractorId ilike :borrower_search or
                dc.name ilike :borrower_search or
                dc.inn ilike :borrower_search)
            ) and
            id = any (
                select distinct dc.dealId from DealContractor dc
                where dc.id = any (
                    select ctr.key.contractorId from ContractorToRole ctr
                    where ctr.key.roleId = any (
                        select cr.id from ContractorRole cr where cr.category = 'WARRANTY'
                    )
                ) and
                (:warranty_search is null or
                dc.contractorId ilike :warranty_search or
                dc.name ilike :warranty_search or
                dc.inn ilike :warranty_search)
            ) and
            id = any (
                select distinct ds.dealId from DealSum ds
                where ds.currencyId = any (
                    select c.id from Currency c
                    where (:currency is null or c.id = :currency)
                ) and
                (:value is null or ds.sum = :value)
            )
            """)
    List<Deal> findFilteredDeal(@Param("is_active") boolean isActive,
                                @Param("id") UUID id,
                                @Param("description") String description,
                                @Param("agreement_number") String agreementNumber,
                                @Param("agreement_date_start") LocalDate agreementDateStart,
                                @Param("agreement_date_end") LocalDate agreementDateEnd,
                                @Param("availability_date_start") LocalDate availabilityDateStart,
                                @Param("availability_date_end") LocalDate availabilityDateEnd,
                                @Param("type") List<String> type,
                                @Param("status") List<String> status,
                                @Param("close_dt_start") LocalDate closeDtStart,
                                @Param("close_dt_end") LocalDate closeDtEnd,
                                @Param("borrower_search") String borrowerSearch,
                                @Param("warranty_search") String warrantySearch,
                                @Param("value") BigDecimal value,
                                @Param("currency") String currency,
                                Pageable pageable);

}
