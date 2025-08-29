package com.example.Deal.Service;

import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.request.ChangeStatus;
import com.example.Deal.DTO.response.DealGet;
import com.example.Deal.DTO.request.DealSearch;
import com.example.Deal.Repository.DealRepository;
import com.example.Deal.Repository.SpecificationManager;
import com.example.Deal.Service.cached.DealCachedService;
import com.example.Deal.Utils.ExcelWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
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
@RequiredArgsConstructor
public class DealService {

    private final DealCachedService cachedService;

    private final DealRepository repository;

    private final StatusService statusService;

    private final TypeService typeService;

    private final DealSumService sumService;

    private final DealContractorService contractorService;

    private final SpecificationManager specificationManager;

    /**
     * Save/update passed Deal instance.
     * Also makes cache outdated.
     *
     * @param deal instance that must be saved
     * @return saved/updated instance of Deal
     */
    @CacheEvict(value = "deals", key = "#deal.getId")
    public Deal save(Deal deal) {
        return repository.save(deal);
    }

    /**
     * Update Deal status to passed in args value.
     * Also makes cache outdated.
     *
     * @param status Deal id and Status that must be updated
     * @return updated Deal entity or null if could not find Deal entity with passed id
     */
    @CacheEvict(value = "deals", key = "#status.getId")
    public Optional<Deal> change(ChangeStatus status) {
        Optional<Deal> optDeal = cachedService.get(status.getId());
        if (optDeal.isPresent()) {
            Deal deal = optDeal.get();
            deal.setStatusId(status.getStatus());
            return Optional.of(repository.save(deal));
        }
        return Optional.empty();
    }

    /**
     * Returns deal entity with appropriate data by passed id value.
     *
     * @param id value of id field of searched deal entity
     * @return data about deal entity and appropriates (type, status, sum and contractors)
     */
    public Optional<DealGet> get(UUID id) {
        Optional<Deal> optDeal = cachedService.get(id);
        if (optDeal.isPresent()) {
            return Optional.of(getDealGet(optDeal.get()));
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
        Specification<Deal> specification = specificationManager.getSpecificationForDealSearch(search);
        List<Deal> deals = repository.findAll(specification, PageRequest.of(page, size)).getContent();
        List<DealGet> result = new ArrayList<>();
        for (Deal d : deals) {
            result.add(getDealGet(d));
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

    /**
     * Convert Deal instance to DealGet.
     *
     * @param deal instance must be converted
     * @return
     */
    private DealGet getDealGet(Deal deal) {
        DealGet dealGet = new DealGet();
        dealGet.getDataFromDeal(deal);
        dealGet.setStatus(statusService.get(deal.getStatusId()).get());
        dealGet.setType(typeService.get(deal.getTypeId()).get());
        dealGet.setSum(sumService.get()
                .stream()
                .filter(sum -> sum.getDealId().equals(deal.getId()))
                .toList());
        dealGet.setContractors(contractorService.get()
                .stream()
                .filter(contractor -> contractor.getDealId().equals(deal.getId()))
                .toList());
        return dealGet;
    }

}
