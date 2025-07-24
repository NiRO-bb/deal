package com.example.Deal.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DealSearch {

    @JsonProperty("deal_id")
    private UUID dealId;

    private String description;

    @Schema(name = "agreement_number")
    @JsonProperty("agreement_number")
    private String agreementNumber;

    @Schema(name = "agreement_date")
    @JsonProperty("agreement_date")
    private AgreementDate agreementDate;

    @Schema(name = "availability_date")
    @JsonProperty("availability_date")
    private AvailabilityDate availabilityDate;

    private List<String> type;

    private List<String> status;

    @Schema(name = "close_dt")
    @JsonProperty("close_dt")
    private CloseDt closeDt;

    @Schema(name = "borrower_search")
    @JsonProperty("borrower_search")
    private String borrowerSearch;

    @Schema(name = "warranty_search")
    @JsonProperty("warranty_search")
    private String warrantySearch;

    private Sum sum;

    public AgreementDate getAgreementDate() {
        return agreementDate == null ? new AgreementDate() : agreementDate;
    }

    public AvailabilityDate getAvailabilityDate() {
        return availabilityDate == null ? new AvailabilityDate() : availabilityDate;
    }

    public CloseDt getCloseDt() {
        return closeDt == null ? new CloseDt() : closeDt;
    }

    public Sum getSum() {
        return sum == null ? new Sum() : sum;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgreementDate {

        @JsonProperty("agreement_date_start")
        private LocalDate agreementDateStart;

        @JsonProperty("agreement_date_end")
        private LocalDate agreementDateEnd;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AvailabilityDate {

        @JsonProperty("availability_date_start")
        private LocalDate availabilityDateStart;

        @JsonProperty("availability_date_end")
        private LocalDate availabilityDateEnd;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CloseDt {

        @JsonProperty("close_dt_start")
        private LocalDate closeDtStart;

        @JsonProperty("close_dt_end")
        private LocalDate closeDtEnd;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sum {

        private BigDecimal value;

        private String currency;

        @JsonIgnore
        private boolean main = false;

    }

}
