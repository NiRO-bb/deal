package com.example.Deal.Repository;

import com.example.Deal.DTO.Currency;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRepository extends CrudRepository<Currency, String> {
}
