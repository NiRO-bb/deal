package com.example.Deal.Service;

import com.example.Deal.DTO.DealSum;
import com.example.Deal.Repository.DealSumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Represents service-layer to work with DealSum instances.
 */
@Service
@RequiredArgsConstructor
public class DealSumService {

    private final DealSumRepository repository;

    /**
     * Returns list of all DealSum entities.
     *
     * @return list of DealSum
     */
    public List<DealSum> get() {
        return repository.findAll();
    }

}
