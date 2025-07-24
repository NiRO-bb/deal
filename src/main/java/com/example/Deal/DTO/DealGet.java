package com.example.Deal.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Schema(name = "DealGet")
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

    @Schema(name = "agreement_number")
    @JsonProperty("agreement_number")
    private String agreementNumber;

    @Schema(name = "agreement_date", example = "2025-01-12")
    @JsonProperty("agreement_date")
    private LocalDate agreementDate;

    @Schema(name = "agreement_start_dt", example = "2025-01-10 12:23:05")
    @JsonProperty("agreement_start_dt")
    private LocalDate agreementStartDt;

    @Schema(name = "availability_date", example = "2025-03-17")
    @JsonProperty("availability_date")
    private LocalDate availabilityDate;

    private Type type;

    private Status status;

    private List<DealSum> sum;

    @Schema(name = "close_dt", example = "2025-05-11 05:13:01")
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
        setType(deal.getType());
        setStatus(deal.getStatus());
        setSum(deal.getSum());
        setContractors(deal.getContractors());
    }

    public void setContractors(List<DealContractor> dealContractors) {
        List<Contractor> contractors = new ArrayList<>();
        for (DealContractor dc : dealContractors) {
            contractors.add(new Contractor(
                    dc.getId(),
                    dc.getContractorId(),
                    dc.getName(),
                    dc.isMain(),
                    dc.getRolesOnly(),
                    dc.getInn()
            ));
        }
        this.contractors = contractors;
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

        private boolean main;

        private List<ContractorRole> roles;

        @JsonIgnore
        private String inn;
    }

}
