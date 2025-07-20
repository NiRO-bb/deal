package com.example.Deal.Controller;

import com.example.Deal.DTO.DealContractor;
import com.example.Deal.Service.DealContractorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

/**
 * Handles incoming http-requests at URL '/deal-contractor'
 * <p>
 * Contains some methods to work with 'DealContractor' entity.
 */
@RestController
@RequestMapping("/deal/deal-contractor")
@RequiredArgsConstructor
public class DealContractorController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DealContractorController.class);

    private final DealContractorService service;

    /**
     * Creates and updates 'DealContractor' entities.
     * <p>
     * Throw RuntimeException if something goes wrong -
     * it is assumed that it will be catched by global exception handler.
     *
     * @param contractor instance that must be updated
     * @return OK status and added/updated instance - if successful,
     * BAD_REQUEST status - if could not find DealContractor with passed id,
     * INTERNAL_SERVER_ERROR status - if error occurred
     */
    @Operation(summary = "Add/update Deal Contractor", description = "Adds and updates Deal Contractor entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deal Contractor saved",
                    content = @Content(schema = @Schema(implementation = DealContractor.class))),
            @ApiResponse(responseCode = "400", description = "Deal Contractor not saved - invalid data",
                    content = @Content(schema = @Schema(type = "string", example = "error message"))),
            @ApiResponse(responseCode = "500", description = "Deal Contractor saving was failed",
                    content = @Content(schema = @Schema(type = "string", example = "error message")))
    })
    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody DealContractor contractor) {
        try {
            Optional<DealContractor> optContractor = service.save(contractor);
            if (optContractor.isPresent()) {
                LOGGER.info("DealContractor added {}", String.format("{ \"id\":\"%s\" }", contractor.getId()));
                return new ResponseEntity<>(optContractor, HttpStatus.OK);
            } else {
                LOGGER.warn("DealContractor not added {}; Main contractor already exists", String.format("{ \"id\":\"%s\" }", contractor.getId()));
                return new ResponseEntity<>("Main contractor of this deal already exists. " +
                        "Change \"main\" value to \"false\".", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("DealContractor not added {}; Error occurred {}",
                    String.format("{ \"id\":\"%s\" }", contractor.getId()), exception.getMessage());
            throw new RuntimeException("DealContractor saving was failed.");
        }
    }

    /**
     * Logically deletes DealContractor entities.
     * <p>
     * Updates 'is_active' field value to 'false' value.
     * Throw RuntimeException if something goes wrong -
     * it is assumed that it will be catched by global exception handler.
     *
     * @param id value of 'id' field of 'DealContractor' that must be deleted
     * @return NO_CONTENT status - if successful,
     * NOT_FOUND status - if there is no DealContractor entity with passed key,
     * INTERNAL_SERVER_ERROR - if error occurred
     */
    @Operation(summary = "Delete Deal Contractor", description = "Logically deletes Deal Contractor entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deal Contractor deleted"),
            @ApiResponse(responseCode = "404", description = "Deal Contractor not deleted - invalid data",
                    content = @Content(schema = @Schema(type = "string", example = "error message"))),
            @ApiResponse(responseCode = "500", description = "Deal Contractor deleting was failed",
                    content = @Content(schema = @Schema(type = "string", example = "error message")))
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        try {
            Optional<DealContractor> optContractor = service.delete(id);
            if (optContractor.isPresent()) {
                LOGGER.info("DealContractor deleted {}", String.format("{ \"id\":\"%s\" }", id));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                LOGGER.warn("DealContractor not deleted {}; DealContractor not found", String.format("{ \"id\":\"%s\" }", id));
                return new ResponseEntity<>("There is no DealContractor entity with such key.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            LOGGER.error("DealContractor not deleted {}; Error occurred {}",
                    String.format("{ \"id\":\"%s\" }", id), exception.getMessage());
            throw new RuntimeException("DealContractor deleting was failed.");
        }
    }

}
