package com.example.Deal.Service.cached;

import com.example.Deal.DTO.Status;
import com.example.Deal.Repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Caches results of calling all methods.
 */
@Service
@RequiredArgsConstructor
public class StatusCachedService {

    private final StatusRepository repository;

    /**
     * Provides list of all DealStatus entities.
     * Tries to find statuses in cache with key - deal_statuses::all.
     * Gets statuses from repository if cache don't store value with required key.
     *
     * @return list of deal statuses
     */
    @Cacheable(value = "deal_statuses", key = "'all'")
    public List<Status> get() {
        return repository.findAll();
    }

}
