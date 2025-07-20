package com.example.Deal.Controller.UI;

import com.example.Deal.Controller.ContractorToRoleController;
import com.example.Deal.DTO.ContractorToRole;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Responsible for request that require authentication.
 * Just checks user role, then calls ContractorToRoleController methods.
 */
@Hidden
@RestController
@RequestMapping("/ui/deal/contractor-to-role")
@RequiredArgsConstructor
public class ContractorToRoleUIController {

    private final ContractorToRoleController controller;

    /**
     * Adds new role to contractor.
     * Requires DEAL_SUPERUSER role or higher for access.
     *
     * @param contractorToRole new role for contractor
     * @return added role
     */
    @Secured("DEAL_SUPERUSER")
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody ContractorToRole.Key contractorToRole) {
        return controller.add(contractorToRole);
    }

    /**
     * Logically deletes contractor role.
     * Requires DEAL_SUPERUSER role or higher for access.
     *
     * @param contractorToRole role that must be deleted
     * @return NO_CONTENT if successful, BAD_REQUEST if there is no matched ContractorToRole entities
     */
    @Secured("DEAL_SUPERUSER")
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody ContractorToRole.Key contractorToRole) {
        return controller.delete(contractorToRole);
    }

}
