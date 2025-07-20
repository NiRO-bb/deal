package com.example.Deal.Controller.UI;

import com.example.Deal.Controller.DealController;
import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.DealSearch;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Responsible for request that require authentication.
 * Just checks user role, then calls DealController methods.
 */
@Hidden
@RestController
@RequestMapping("/ui/deal")
@RequiredArgsConstructor
public class DealUIController {

    private final DealController controller;

    /**
     * Adds or updates Deal entity.
     * Requires DEAL_SUPERUSER role or higher for access.
     *
     * @param deal Deal instance that must be added or updated
     * @return added/updated Deal entity
     */
    @Secured("DEAL_SUPERUSER")
    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody Deal deal) {
        return controller.save(deal);
    }

    /**
     * Updates Deal entity status.
     * Requires DEAL_SUPERUSER role or higher for access.
     *
     * @param deal contains status and id of Deal entity that must be updated
     * @return updated Deal and OK status; NOT_FOUND if there is no matched Deal entity
     */
    @Secured("DEAL_SUPERUSER")
    @PatchMapping("/change/status")
    public ResponseEntity<?> change(@RequestBody Deal.DealStatusUpdate deal) {
        return controller.change(deal);
    }

    /**
     * Retrieves Deal entity by id value.
     * Requires USER role or higher for access.
     *
     * @param id value of id field of Deal entity that must be received
     * @return matched Deal entity and OK status; NOT_FOUND if there is no matched Deal entity
     */
    @Secured("USER")
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") UUID id) {
        return controller.get(id);
    }

    /**
     * Retrieves Deal entity list by passed parameters.
     * Requires CREDIT_USER role, OVERDRAFT_USER role or higher for access.
     *
     * @param search contains filtering fields
     * @param page number of page that will be returned
     * @param size amount of returned DealGet instances
     * @return matched Deal entities as list
     */
    @Secured({"CREDIT_USER", "OVERDRAFT_USER"})
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody DealSearch search,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        if (hasAccess(search)) {
            return controller.search(search, page, size);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Retrieves Deal entity list by passed parameters as .zip archive with .xlsx file inside.
     * Requires DEAL_SUPERUSER role or higher for access.
     *
     * @param search contains filtering fields
     * @return created .zip archive
     */
    @Secured("DEAL_SUPERUSER")
    @PostMapping("/search/export")
    public ResponseEntity<?> export(@RequestBody DealSearch search) {
        return controller.export(search);
    }

    /**
     * Checks if user has access to some method.
     * Uses data stored in SecurityContextHolder - extracts user authorities.
     *
     * @param search contains filtering fields
     * @return result of check (true - access permitted, false - access denied)
     */
    private boolean hasAccess(DealSearch search) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = auth.getAuthorities().stream().map(Object::toString).toList();
        if (roles.contains("DEAL_SUPERUSER") || roles.contains("SUPERUSER")) {
            return true;
        } else if (roles.contains("CREDIT_USER") && search.getType().equals(Collections.singletonList("CREDIT"))) {
            return true;
        } else if (roles.contains("OVERDRAFT_USER") && search.getType().equals(Collections.singletonList("OVERDRAFT"))) {
            return true;
        }
        return false;
    }

}
