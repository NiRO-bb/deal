package com.example.Deal.Service;

import com.example.Deal.DTO.Type;
import com.example.Deal.Repository.TypeRepository;
import com.example.Deal.Service.cached.TypeCachedService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Represents service-layer to process DealType instances.
 */
@Service
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository repository;

    private final TypeCachedService cachedService;
    /**
     * Returns list of all DealType entities.
     *
     * @return list of DealType
     */
    public List<Type> get() {
        return cachedService.get();
    }

    /**
     * Returns DealType entity by passed id value.
     *
     * @param id value of id field of searched DealType entity
     * @return searched entity or empty
     */
    public Optional<Type> get(String id) {
        return cachedService.get()
                .stream()
                .filter(type -> type.getId().equals(id))
                .findAny();
    }

    /**
     * Saves passed DealType instance in repository.
     * Also makes cache (with key 'deal_types::all') outdated.
     *
     * @param type instance must be saved
     * @return saved entity
     */
    @CacheEvict(value = "deal_types", key = "'all'")
    public Type save(Type type) {
        return repository.save(type);
    }

}
