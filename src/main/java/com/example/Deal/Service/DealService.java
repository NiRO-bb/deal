package com.example.Deal.Service;

import com.example.Deal.DTO.ContractorRole;
import com.example.Deal.DTO.ContractorToRole;
import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.DealContractor;
import com.example.Deal.DTO.DealSearch;
import com.example.Deal.DTO.DealSum;
import com.example.Deal.DTO.Status;
import com.example.Deal.DTO.Sum;
import com.example.Deal.DTO.Type;
import com.example.Deal.DTO.DealGet;
import com.example.Deal.ExcelWriter;
import com.example.Deal.Repository.ContractorRoleRepository;
import com.example.Deal.Repository.ContractorToRoleRepository;
import com.example.Deal.Repository.CurrencyRepository;
import com.example.Deal.Repository.DealContractorRepository;
import com.example.Deal.Repository.DealRepository;
import com.example.Deal.Repository.DealSumRepository;
import com.example.Deal.Repository.StatusRepository;
import com.example.Deal.Repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private final DealRepository dealRepo;
    private final TypeRepository typeRepo;
    private final StatusRepository statusRepo;
    private final DealSumRepository dealSumRepo;
    private final CurrencyRepository currencyRepo;
    private final DealContractorRepository dealContractorRepo;
    private final ContractorToRoleRepository contractorToRoleRepo;
    private final ContractorRoleRepository contractorRoleRepo;

    @Autowired
    public DealService(DealRepository dealRepo, TypeRepository typeRepo, StatusRepository statusRepo,
                       DealSumRepository dealSumRepo, CurrencyRepository currencyRepo,
                       DealContractorRepository dealContractorRepo,
                       ContractorToRoleRepository contractorToRoleRepo,
                       ContractorRoleRepository contractorRoleRepo) {
        this.dealRepo = dealRepo;
        this.typeRepo = typeRepo;
        this.statusRepo = statusRepo;
        this.dealSumRepo = dealSumRepo;
        this.currencyRepo = currencyRepo;
        this.dealContractorRepo = dealContractorRepo;
        this.contractorToRoleRepo = contractorToRoleRepo;
        this.contractorRoleRepo = contractorRoleRepo;
    }

    /**
     * Save/update passed Deal instance.
     *
     * @param deal instance that must be saved
     * @return saved/updated instance of Deal
     */
    public Deal save(Deal deal) {
        return dealRepo.save(deal);
    }

    /**
     * Update Deal status to passed in args value.
     *
     * @param dealUpdate Deal id and Status that must be updated
     * @return updated Deal entity or null if could not find Deal entity with passed id
     */
    public Deal change(Deal.DealStatusUpdate dealUpdate) {
        Optional<Deal> optDeal = dealRepo.findById(dealUpdate.getId());
        if (optDeal.isPresent()) {
            Deal deal = optDeal.get();
            deal.setStatusId(dealUpdate.getStatus());
            return dealRepo.save(deal);
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
        Optional<Deal> optDeal = dealRepo.findById(id);
        DealGet result = new DealGet();
        if (optDeal.isPresent()) {
            Deal deal = optDeal.get();
            result.getDataFromDeal(deal);

            Optional<Type> type = typeRepo.findById(deal.getTypeId());
            type.ifPresent(value -> result.setType(value));

            Optional<Status> status = statusRepo.findById(deal.getStatusId());
            status.ifPresent(value -> result.setStatus(value));

            List<DealSum> dealSumList = dealSumRepo.findByDealId(deal.getId());
            List<Sum> sumList = new ArrayList<>();
            for (DealSum dealSum : dealSumList) {
                dealSum.setCurrency(currencyRepo);
                sumList.add(new Sum(dealSum.getSum(), dealSum.getCurrency().getId(), dealSum.isMain()));
            }
            result.setSum(sumList);

            List<DealContractor> dealContractorList = dealContractorRepo.findByDealIdAndIsActive(deal.getId(), true);
            List<DealGet.Contractor> resultContractors = new ArrayList<>();
            for (DealContractor dealContractor : dealContractorList) {
                DealGet.Contractor resultContactor = new DealGet.Contractor();
                resultContactor.getDataFromDealContractor(dealContractor);
                resultContactor.setRoles(new ArrayList<>());

                List<ContractorToRole> contractorToRoleList = contractorToRoleRepo.findByKeyContractorId(dealContractor.getId());
                for (ContractorToRole contractorToRole : contractorToRoleList) {
                    List<ContractorRole> roleList = contractorRoleRepo.findAllById(contractorToRole.getKey().getRoleId());
                    for (ContractorRole role : roleList) {
                        resultContactor.getRoles().add(role);
                    }
                }
                resultContractors.add(resultContactor);
            }
            result.setContractors(resultContractors);

            return Optional.of(result);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Searches Deal entities according to passed filter.
     *
     * @param d contains filtering fields
     * @param page number of returned page
     * @param size amount of returned DealGet instances
     * @return list of found DealGet instances, can be empty
     */
    public List<DealGet> search(DealSearch d, int page, int size) {
        List<Deal> deals = dealRepo.findFilteredDeal(
                true,
                d.getDealId(),
                d.getDescription(),
                format(d.getAgreementNumber()),
                d.getAgreementDate().getAgreementDateStart() == null ? null : d.getAgreementDate().getAgreementDateStart(),
                d.getAgreementDate().getAgreementDateEnd() == null ? null : d.getAgreementDate().getAgreementDateEnd(),
                d.getAvailabilityDate().getAvailabilityDateStart() == null ? null : d.getAvailabilityDate().getAvailabilityDateStart(),
                d.getAvailabilityDate().getAvailabilityDateEnd() == null ? null : d.getAvailabilityDate().getAvailabilityDateEnd(),
                d.getType(),
                d.getStatus(),
                d.getCloseDt().getCloseDtStart() == null ? null : d.getCloseDt().getCloseDtStart(),
                d.getCloseDt().getCloseDtEnd() == null ? null : d.getCloseDt().getCloseDtEnd(),
                format(d.getBorrowerSearch()),
                format(d.getWarrantySearch()),
                d.getSum().getValue(),
                d.getSum().getCurrency(),
                PageRequest.of(page, size, Sort.by("id")));
        List<DealGet> dealGets = new ArrayList<>();
        for (Deal deal : deals) {
            dealGets.add(get(deal.getId()).get());
        }
        return dealGets;
    }

    /**
     * Returns .xlsx file with found DealGet instances.
     *
     * @param dealSearch contains filtering fields
     * @return File instance with created .xlsx file (based on search results) or null - if error occurred while file writing
     */
    public Optional<File> export(DealSearch dealSearch) {
        List<DealGet> deals = search(dealSearch, 0, Integer.MAX_VALUE);
        return ExcelWriter.writer(deals);
    }

    private String format(String param) {
        return param == null ? null : "%" + param + "%";
    }

}
