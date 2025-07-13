package com.example.Deal.Repository;

import com.example.Deal.DTO.ContractorRole;
import com.example.Deal.DTO.ContractorToRole;
import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.DealContractor;
import com.example.Deal.DTO.DealSearch;
import com.example.Deal.DTO.DealSum;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains methods for Specification creating.
 */
public class SpecificationManager {

    /**
     * Creates Specification for Deal.
     * <p>
     * Executed query depends on passed parameters.
     * If some field has null value, it is not used in query.
     *
     * @param search contains filter fields
     * @return completed specification for Deal
     */
    public Specification<Deal> getSpecificationForDealSearch(DealSearch search) {
        return (deal, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (search.getDealId() != null) {
                predicates.add(builder.equal(deal.get("id"), search.getDealId()));
            }
            if (search.getDescription() != null) {
                predicates.add(builder.equal(deal.get("description"), search.getDescription()));
            }
            if (search.getAgreementNumber() != null) {
                predicates.add(builder.like(builder.lower(deal.get("agreementNumber")), format(search.getAgreementNumber())));
            }
            if (search.getAgreementDate().getAgreementDateStart() != null) {
                predicates.add(builder.greaterThanOrEqualTo(deal.get("agreementDate"),
                        search.getAgreementDate().getAgreementDateStart()));
            }
            if (search.getAgreementDate().getAgreementDateEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(deal.get("agreementDate"),
                        search.getAgreementDate().getAgreementDateEnd()));
            }
            if (search.getAvailabilityDate().getAvailabilityDateStart() != null) {
                predicates.add(builder.greaterThanOrEqualTo(deal.get("availabilityDate"),
                        search.getAvailabilityDate().getAvailabilityDateStart()));
            }
            if (search.getAvailabilityDate().getAvailabilityDateEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(deal.get("availabilityDate"),
                        search.getAvailabilityDate().getAvailabilityDateEnd()));
            }
            if (search.getType() != null) {
                predicates.add(deal.get("typeId").in(search.getType()));
            }
            if (search.getStatus() != null) {
                predicates.add(deal.get("statusId").in(search.getStatus()));
            }
            if (search.getCloseDt().getCloseDtStart() != null) {
                predicates.add(builder.greaterThanOrEqualTo(deal.get("closeDt"),
                        search.getCloseDt().getCloseDtStart()));
            }
            if (search.getCloseDt().getCloseDtEnd() != null) {
                predicates.add(builder.greaterThanOrEqualTo(deal.get("closeDt"),
                        search.getCloseDt().getCloseDtEnd()));
            }
            if (search.getBorrowerSearch() != null) {
                Join<Deal, DealContractor> joinContractor = deal.join("contractors", JoinType.INNER);
                Join<DealContractor, ContractorToRole> joinToRole = joinContractor.join("roles", JoinType.INNER);
                Join<ContractorToRole, ContractorRole> joinRole = joinToRole.join("role", JoinType.INNER);
                predicates.add(builder.and(
                        builder.equal(joinRole.get("category"), "BORROWER"),
                        builder.or(builder.like(builder.lower(joinContractor.get("contractorId")), format(search.getBorrowerSearch())),
                                builder.like(builder.lower(joinContractor.get("name")), format(search.getBorrowerSearch())),
                                builder.like(builder.lower(joinContractor.get("inn")), format(search.getBorrowerSearch())))));
            }
            if (search.getWarrantySearch() != null) {
                Join<Deal, DealContractor> joinContractor = deal.join("contractors", JoinType.INNER);
                Join<DealContractor, ContractorToRole> joinToRole = joinContractor.join("roles", JoinType.INNER);
                Join<ContractorToRole, ContractorRole> joinRole = joinToRole.join("role", JoinType.INNER);
                predicates.add(builder.and(
                        builder.equal(joinRole.get("category"), "WARRANTY"),
                        builder.or(builder.like(builder.lower(joinContractor.get("contractorId")), format(search.getWarrantySearch())),
                                builder.like(builder.lower(joinContractor.get("name")), format(search.getWarrantySearch())),
                                builder.like(builder.lower(joinContractor.get("inn")), format(search.getWarrantySearch())))));
            }
            if (search.getSum().getValue() != null) {
                Join<Deal, DealSum> join = deal.join("sum", JoinType.LEFT);
                predicates.add(builder.equal(join.get("sum"), search.getSum().getValue()));
            }
            if (search.getSum().getCurrency() != null) {
                Join<Deal, DealSum> join = deal.join("sum", JoinType.LEFT);
                predicates.add(builder.equal(join.get("currencyId"), search.getSum().getCurrency()));
            }
            return predicates.isEmpty() ? null : builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private String format(String param) {
        return param == null ? null : "%" + param.toLowerCase() + "%";
    }

}
