package com.example.Deal.Service;

import com.example.Deal.DTO.ContractorRole;
import com.example.Deal.DTO.ContractorToRole;
import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.DealContractor;
import com.example.Deal.DTO.DealGet;
import com.example.Deal.DTO.DealSearch;
import com.example.Deal.DTO.DealSum;
import com.example.Deal.ExcelWriter;
import com.example.Deal.Repository.DealRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Provides access to repository-layer
 */
@Service
public class DealService {

    private final DealRepository repository;

    @Autowired
    public DealService(DealRepository repository) {
        this.repository = repository;
    }

    /**
     * Save/update passed Deal instance.
     *
     * @param deal instance that must be saved
     * @return saved/updated instance of Deal
     */
    public Deal save(Deal deal) {
        return repository.save(deal);
    }

    /**
     * Update Deal status to passed in args value.
     *
     * @param dealUpdate Deal id and Status that must be updated
     * @return updated Deal entity or null if could not find Deal entity with passed id
     */
    public Deal change(Deal.DealStatusUpdate dealUpdate) {
        Optional<Deal> optDeal = repository.findById(dealUpdate.getId());
        if (optDeal.isPresent()) {
            Deal deal = optDeal.get();
            deal.setStatusId(dealUpdate.getStatus());
            return repository.save(deal);
        }
        return null;
    }

    /**
     * Returns instance of Deal with related data.
     * <p>
     * Returns data of Deal entity + Type, Status, Sum, DealContractor data.
     *
     * @param id value of 'id' filed of searching 'Deal' entity
     * @return DealGet instance - if successful, empty Optional - if could not find Deal with passed id
     */
    public Optional<DealGet> get(UUID id) {
        Optional<Deal> optDeal = repository.findById(id);
        if (optDeal.isPresent()) {
            DealGet deal = new DealGet();
            deal.getDataFromDeal(optDeal.get());
            return Optional.of(deal);
        }
        return Optional.empty();
    }

    /**
     * Searches Deal entities according to passed filter.
     *
     * @param search contains filtering fields
     * @param page number of returned page
     * @param size amount of returned DealGet instances
     * @return list of found DealGet instances, can be empty
     */
    public List<DealGet> search(DealSearch search, int page, int size) {
        Specification<Deal> specification = (deal, query, builder) -> {
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
        List<Deal> deals = repository.findAll(specification, PageRequest.of(page, size)).getContent();
        List<DealGet> result = new ArrayList<>();
        for (Deal d : deals) {
            DealGet dealGet = new DealGet();
            dealGet.getDataFromDeal(d);
            result.add(dealGet);
        }
        return result;
    }

    /**
     * Returns .zip file with found DealGet instances.
     *
     * @param dealSearch contains filtering fields
     * @return Resource instance with created .zip archive (based on search results)
     * or null - if error occurred while archive creating
     */
    public Optional<InputStreamResource> export(DealSearch dealSearch) {
        List<DealGet> deals = search(dealSearch, 0, Integer.MAX_VALUE);
        Optional<File> file =  ExcelWriter.writer(deals);
        if (file.isPresent()) {
            return ExcelWriter.archive(file.get());
        } else {
            return Optional.empty();
        }
    }

    private String format(String param) {
        return param == null ? null : "%" + param.toLowerCase() + "%";
    }

}
