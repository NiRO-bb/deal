package com.example.Deal.Controller;

import com.example.Deal.DTO.ContractorToRole;
import com.example.Deal.Service.ContractorToRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Handles incoming http-requests at URL '/contractor-to-role'
 * <p>
 * Contains some methods to work with 'ContractorToRole' entity.
 */
@RestController
@RequestMapping("/contractor-to-role")
public class ContractorToRoleController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ContractorToRoleController.class);

    private final ContractorToRoleService service;

    @Autowired
    public ContractorToRoleController(ContractorToRoleService service) {
        this.service = service;
    }

    /**
     * Adds new role to existing deal contractor.
     *
     * @param contractorToRole contains contractor id and new role for him
     * @return added ContractorToRole instance and OK status,
     * BAD_REQUEST status if could not find passed deal contractor or contractor role entities
     * or INTERNAL_SERVER_ERROR status if error occurred
     */
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody ContractorToRole.Key contractorToRole) {
        try {
            Optional<ContractorToRole> result = service.add(contractorToRole);
            if (result.isPresent()) {
                LOGGER.info("ContractorToRole added {}", contractorToRole.desc());
                return new ResponseEntity<>(result.get(), HttpStatus.OK);
            } else {
                LOGGER.warn("ContractorToRole not added {};m" +
                        "There is no such DealContractor or/and ContractorRole entity(-ies)", contractorToRole.desc());
                return new ResponseEntity<>("There is no such DealContractor or/and ContractorRole entity(-ies)", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("ContractorToRole not added {}; Error occurred {}",
                    contractorToRole.desc(), exception.getMessage());
            return new ResponseEntity<>("ContractorToRole adding was failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Logically deletes role of DealContractor.
     * <p>
     * Updates value of 'is_active' field to 'false'.
     *
     * @param contractorToRole contains contractor id and role that must be deleted
     * @return NO_CONTENT - if successful, BAD_REQUEST - if could not find ContractorToRole instance with passed id,
     * INTERNAL_SERVER_ERROR status if error occurred
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody ContractorToRole.Key contractorToRole) {
        try {
            ContractorToRole result = service.delete(contractorToRole);
            if (result != null) {
                LOGGER.info("ContractorToRole deleted {}", contractorToRole.desc());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                LOGGER.warn("ContractorToRole not deleted {}; There is no contractor_to_role entity with such key", contractorToRole.desc());
                return new ResponseEntity<>("There is no contractor_to_role entity with such key.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("ContractorToRole not deleted {}; Error occurred {}",
                    contractorToRole.desc(), exception.getMessage());
            return new ResponseEntity<>("ContractorToRole deleting was failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
