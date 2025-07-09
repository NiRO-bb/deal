package com.example.Deal.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "description", "agreement_number", "agreement_date",
        "agreement_start_dt", "availability_date", "type", "status", "sum", "close_dt",
        "contractors"})
public class DealGet {

    private UUID id;
    private String description;
    @JsonProperty("agreement_number")
    private String agreementNumber;
    @JsonProperty("agreement_date")
    private LocalDate agreementDate;
    @JsonProperty("agreement_start_dt")
    private LocalDate agreementStartDt;
    @JsonProperty("availability_date")
    private LocalDate availabilityDate;
    private Type type;
    private Status status;
    private List<Sum> sum;
    @JsonProperty("close_dt")
    private LocalDate closeDt;
    private List<Contractor> contractors;

    public void getDataFromDeal(Deal deal) {
        setId(deal.getId());
        setDescription(deal.getDescription());
        setAgreementNumber(deal.getAgreementNumber());
        setAgreementDate(deal.getAgreementDate());
        setAgreementStartDt(LocalDate.from(deal.getAgreementStartDt()));
        setAvailabilityDate(deal.getAvailabilityDate());
        setCloseDt(LocalDate.from(deal.getCloseDt()));
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"id", "contractor_id", "name", "main", "roles"})
    public static class Contractor {

        private UUID id;
        @JsonProperty("contractor_id")
        private String contractorId;
        private String name;
        @JsonIgnore
        private String inn;
        private boolean main;
        private List<ContractorRole> roles;

        public void getDataFromDealContractor(DealContractor dc) {
            setId(dc.getId());
            setContractorId(dc.getContractorId());
            setName(dc.getName());
            setInn(dc.getInn());
            setMain(dc.isMain());
        }
    }

}
