package com.example.Deal.DTO;

import com.example.Deal.Repository.CurrencyRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deal_sum")
public class DealSum {

    @Id
    private long id;
    @Column(name = "deal_id")
    private UUID dealId;
    private BigDecimal sum;
    @Column(name = "currency_id")
    private String currencyId;
    @Column(name = "is_main")
    private boolean isMain = false;
    @Column(name = "is_active")
    private boolean isActive = true;
    @Transient
    private Currency currency;

    public void setCurrency(CurrencyRepository repository) {
        Optional<Currency> currency = repository.findById(currencyId);
        currency.ifPresent(value -> this.currency = currency.get());
    }

}

