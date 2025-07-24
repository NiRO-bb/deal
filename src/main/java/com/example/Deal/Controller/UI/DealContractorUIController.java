package com.example.Deal.Controller.UI;

import com.example.Deal.Controller.DealContractorController;
import com.example.Deal.DTO.DealContractor;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Responsible for request that require authentication.
 * Just checks user role, then calls DealContractorController methods.
 */
@Hidden
@RestController
@RequestMapping("/ui/deal/deal-contractor")
@RequiredArgsConstructor
public class DealContractorUIController {

    private final DealContractorController controller;

    /**
     * Adds or updates DealContractor entity.
     * Requires DEAL_SUPERUSER role or higher for access.
     *
     * @param contractor DealContractor instance that must be added or updated
     * @return added/updated DealContractor entity
     */
    @Secured("DEAL_SUPERUSER")
    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody DealContractor contractor) {
        return controller.save(contractor);
    }

    /**
     * Logically deletes DealContractor entity.
     * Requires DEAL_SUPERUSER role or higher for access.
     *
     * @param id value of id field of DealContractor entity that must be deleted
     * @return NO_CONTENT status if successful, NOT_FOUND if there is no matched DealContractor entity
     */
    @Secured("DEAL_SUPERUSER")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        return controller.delete(id);
    }

}
