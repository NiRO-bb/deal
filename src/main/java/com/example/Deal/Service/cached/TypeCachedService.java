package com.example.Deal.Service.cached;

import com.example.Deal.DTO.Type;
import com.example.Deal.Repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caches results of calling all methods.
 */
@Service
@RequiredArgsConstructor
public class TypeCachedService {

    private final TypeRepository repository;

    /**
     * Provides list of all DealType entities.
     * Tries to find types in cache with key - deal_types::all.
     * Gets types from repository if cache don't store value with required key.
     *
     * @return list of deal types
     */
    @Cacheable(value = "deal_types", key = "'all'")
    public List<Type> get() {
        return repository.findAll();
    }

}
