package com.example.Deal.Controller;

import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.DealSearch;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface DealController {

    /**
     * Adds and updates Deal entity.
     *
     * @param deal instance that must be saved.
     * @return added/updated Deal entity
     */
    ResponseEntity<?> save(Deal deal);

    /**
     * Updates Deal status field.
     *
     * @param deal contains Deal id and new status values
     * @return updated Deal entity
     */
    ResponseEntity<?> change(Deal.DealStatusUpdate deal);

    /**
     * Retrieves Deal entity by passed id value.
     *
     * @param id value of id field of Deal entity that must be retrieved
     * @return matched Deal entity
     */
    ResponseEntity<?> get(UUID id);

    /**
     * Retrieves Deal entities based on passed parameters.
     *
     * @param search contains filtering fields
     * @param page number of returning page
     * @param size size (amount of Deal entities) of returning page
     * @return matched Deal entities
     */
    ResponseEntity<?> search(DealSearch search, int page, int size);

    /**
     * Retrieves Deal entities based on passed parameters.
     *
     * @param search contains filtering fields
     * @return matched Deal entities as file
     */
    ResponseEntity<?> export(DealSearch search);

}
