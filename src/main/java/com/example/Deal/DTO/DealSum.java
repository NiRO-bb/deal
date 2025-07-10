package com.example.Deal.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deal_sum")
public class DealSum {

    @Id
    @JsonIgnore
    private long id;

    @Column(name = "deal_id")
    @JsonIgnore
    private UUID dealId;

    @JsonProperty("value")
    private BigDecimal sum;

    @Column(name = "currency_id")
    @JsonProperty("currency")
    private String currencyId;

    @Column(name = "is_main")
    @JsonIgnore
    private boolean isMain = false;

    @Column(name = "is_active")
    @JsonIgnore
    private boolean isActive = true;

//    @Transient
//    private Currency currency;
//
//    public void setCurrency(CurrencyRepository repository) {
//        Optional<Currency> currency = repository.findById(currencyId);
//        currency.ifPresent(value -> this.currency = currency.get());
//    }

}

