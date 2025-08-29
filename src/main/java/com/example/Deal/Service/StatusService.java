package com.example.Deal.Service;

import com.example.Deal.DTO.Status;
import com.example.Deal.Service.cached.StatusCachedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Represents service-layer to process DealStatus instances.
 */
@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusCachedService cachedService;

    /**
     * Returns list of all DealStatus entities.
     *
     * @return list of DealStatus
     */
    public List<Status> get() {
        return cachedService.get();
    }

    /**
     * Returns DealStatus entity by passed id value.
     *
     * @param id value of id field of searched DealStatus entity
     * @return searched entity or empty
     */
    public Optional<Status> get(String id) {
        return cachedService.get()
                .stream()
                .filter(status -> status.getId().equals(id))
                .findAny();
    }

}
