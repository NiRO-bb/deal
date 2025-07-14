package com.example.Deal.Service;

import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.DealGet;
import com.example.Deal.DTO.DealSearch;
import com.example.Deal.ExcelWriter;
import com.example.Deal.Repository.DealRepository;
import com.example.Deal.Repository.SpecificationManager;
import lombok.RequiredArgsConstructor;
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

    private final DealRepository repository;
    private final SpecificationManager specificationManager;

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
    public Optional<Deal> change(Deal.DealStatusUpdate dealUpdate) {
        Optional<Deal> optDeal = repository.findById(dealUpdate.getId());
        if (optDeal.isPresent()) {
            Deal deal = optDeal.get();
            deal.setStatusId(dealUpdate.getStatus());
            return Optional.of(repository.save(deal));
        }
        return Optional.empty();
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
        Specification<Deal> specification = specificationManager.getSpecificationForDealSearch(search);
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

}
