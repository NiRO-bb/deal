package com.example.Deal.Controller;

import com.example.Deal.DTO.DealContractor;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface DealContractorController {

    /**
     * Adds and updates DealContractor entity.
     *
     * @param contractor instance that must be saved
     * @return added/updated DealContractor entity
     */
    ResponseEntity<?> save(DealContractor contractor);

    /**
     * Logically deletes DealContractor entity.
     *
     * @param id value of id field of DealContractor entity that must be deleted
     * @return NO_CONTENT response code if successful, NOT_FOUND else
     */
    ResponseEntity<?> delete(UUID id);

}
