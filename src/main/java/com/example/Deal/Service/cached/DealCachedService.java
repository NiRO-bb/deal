package com.example.Deal.Service.cached;

import com.example.Deal.DTO.Deal;
import com.example.Deal.Repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Caches results of calling all methods.
 */
@Service
@RequiredArgsConstructor
public class DealCachedService {

    private final DealRepository repository;

    /**
     * Provides Deal entity by passed id value.
     * Tries to find deal in cache with key - deals::#id (#id - passed id value).
     * Gets deal from repository if cache don't store value with required key.
     *
     * @param id value of id field of searched deal entity
     * @return found deal
     */
    @Cacheable(value = "deals", key = "#id")
    public Optional<Deal> get(UUID id) {
        return repository.findById(id);
    }

}
