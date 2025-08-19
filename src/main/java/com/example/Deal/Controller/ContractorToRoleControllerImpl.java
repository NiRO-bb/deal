package com.example.Deal.Controller;

import com.example.Deal.DTO.ContractorToRole;
import com.example.Deal.Service.ContractorToRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
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
@RequestMapping("/deal/contractor-to-role")
@RequiredArgsConstructor
public class ContractorToRoleControllerImpl implements ContractorToRoleController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ContractorToRoleControllerImpl.class);

    private final ContractorToRoleService service;

    /**
     * Adds new role to existing deal contractor.
     * Throw RuntimeException if something goes wrong -
     * it is assumed that it will be catched by global exception handler.
     *
     * @param contractorToRole contains contractor id and new role for him
     * @return added ContractorToRole instance and OK status,
     * BAD_REQUEST status if could not find passed deal contractor or contractor role entities
     * or INTERNAL_SERVER_ERROR status if error occurred
     */
    @Operation(summary = "Add role for Deal Contractor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role added",
                    content = @Content(schema = @Schema(implementation = ContractorToRole.class))),
            @ApiResponse(responseCode = "400", description = "Role not added",
                    content = @Content(schema = @Schema(type = "string", example = "error message"))),
            @ApiResponse(responseCode = "500", description = "Role adding was failed",
                    content = @Content(schema = @Schema(type = "string", example = "error message")))
    })
    @PostMapping("/add")
    @Override
    public ResponseEntity<?> add(@RequestBody ContractorToRole.Key contractorToRole) {
        try {
            Optional<ContractorToRole> optContractorToRole = service.add(contractorToRole);
            if (optContractorToRole.isPresent()) {
                LOGGER.info("ContractorToRole added {}", contractorToRole.desc());
                return new ResponseEntity<>(optContractorToRole.get(), HttpStatus.OK);
            } else {
                LOGGER.warn("ContractorToRole not added {}; " +
                        "There is no such DealContractor or/and ContractorRole entity(-ies)", contractorToRole.desc());
                return new ResponseEntity<>("There is no such DealContractor or/and ContractorRole entity(-ies)", HttpStatus.BAD_REQUEST);
            }
        } catch (DataAccessException exception) {
            LOGGER.error("ContractorToRole not added {} - {}",
                    contractorToRole.desc(), exception.getMessage());
            throw new DataAccessException(exception.getMessage()) {};
        }
    }

    /**
     * Logically deletes role of DealContractor.
     * <p>
     * Updates value of 'is_active' field to 'false'.
     * Throw RuntimeException if something goes wrong -
     * it is assumed that it will be catched by global exception handler.
     *
     * @param contractorToRole contains contractor id and role that must be deleted
     * @return NO_CONTENT - if successful, BAD_REQUEST - if could not find ContractorToRole instance with passed id,
     * INTERNAL_SERVER_ERROR status if error occurred
     */
    @Operation(summary = "Delete Deal Contractor role", description = "Logically deletes role of Deal Contractor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role deleted"),
            @ApiResponse(responseCode = "400", description = "Role not deleted - invalid data",
                    content = @Content(schema = @Schema(type = "string", example = "error message"))),
            @ApiResponse(responseCode = "500", description = "Role deleting was failed",
                    content = @Content(schema = @Schema(type = "string", example = "error message")))
    })
    @DeleteMapping("/delete")
    @Override
    public ResponseEntity<?> delete(@RequestBody ContractorToRole.Key contractorToRole) {
        try {
            Optional<ContractorToRole> optContractorToRole = service.delete(contractorToRole);
            if (optContractorToRole.isPresent()) {
                LOGGER.info("ContractorToRole deleted {}", contractorToRole.desc());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                LOGGER.warn("ContractorToRole not deleted {}; There is no contractor_to_role entity with such key", contractorToRole.desc());
                return new ResponseEntity<>("There is no contractor_to_role entity with such key.", HttpStatus.BAD_REQUEST);
            }
        } catch (DataAccessException exception) {
            LOGGER.error("ContractorToRole not deleted {} - {}",
                    contractorToRole.desc(), exception.getMessage());
            throw new DataAccessException(exception.getMessage()) {};
        }
    }

}
