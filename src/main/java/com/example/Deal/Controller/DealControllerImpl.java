package com.example.Deal.Controller;

import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.DealGet;
import com.example.Deal.DTO.DealSearch;
import com.example.Deal.Service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Handles incoming http-requests at URL '/deal'
 * <p>
 * Contains some methods to work with 'Deal' entity.
 */
@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealControllerImpl implements DealController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DealControllerImpl.class);

    private final DealService service;

    /**
     * Creates and updates 'Deal' entities.
     * <p>
     * Throw RuntimeException if something goes wrong -
     * it is assumed that it will be catched by global exception handler.
     *
     * @param deal instance that must be saved or updated
     * @return ResponseEntity with result and OK status - if successful, ResponseEntity with INTERNAL_SERVER_ERROR - else
     */
    @Operation(summary = "Add/update Deal", description = "Adds and updates Deal entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deal saved",
                    content = @Content(schema = @Schema(implementation = Deal.class))),
            @ApiResponse(responseCode = "500", description = "Deal saving was failed",
                    content = @Content(schema = @Schema(type = "string", example = "error message")))
    })
    @PutMapping("/save")
    @Override
    public ResponseEntity<?> save(@RequestBody Deal deal) {
        try {
            Deal result = service.save(deal);
            LOGGER.info("Deal added {}", String.format("{ \"id\":\"%s\" }", deal.getId()));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (DataAccessException exception) {
            LOGGER.error("Deal not added {} - {}",
                    String.format("{ \"id\":\"%s\" }", deal.getId()), exception.getMessage());
            throw new DataAccessException(exception.getMessage()) {};
        }
    }

    /**
     * Updates 'Deal' status.
     * <p>
     * Throw RuntimeException if something goes wrong -
     * it is assumed that it will be catched by global exception handler.
     *
     * @param deal contains Deal id (that must be updated) and Status (that must be assigned)
     * @return ResponseEntity with updated result and OK status - if successful,
     * NOT_FOUND status - if could not find Deal with passed id,
     * INTERNAL_SERVER_ERROR status - else
     */
    @Operation(summary = "Change Deal status", description = "Replaces Deal entity status with passed value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deal status saved",
                    content = @Content(schema = @Schema(implementation = Deal.class))),
            @ApiResponse(responseCode = "404", description = "Deal status not saved - invalid data",
                    content = @Content(schema = @Schema(type = "string", example = "error message"))),
            @ApiResponse(responseCode = "500", description = "Deal status saving was failing",
                    content = @Content(schema = @Schema(type = "string", example = "error message")))
    })
    @PatchMapping("/change/status")
    @Override
    public ResponseEntity<?> change(@RequestBody Deal.DealStatusUpdate deal) {
        try {
            Optional<Deal> optDeal = service.change(deal);
            if (optDeal.isPresent()) {
                LOGGER.info("Deal status updated {}", deal.desc());
                return new ResponseEntity<>(optDeal.get(), HttpStatus.OK);
            } else {
                LOGGER.warn("Deal status not updated {}; There is no deal entity with such id", deal.desc());
                return new ResponseEntity<>("There is no deal with such id.", HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException exception) {
            LOGGER.error("Deal status not updated {} - {}",
                    deal.desc(), exception.getMessage());
            throw new DataAccessException(exception.getMessage()) {};
        }
    }

    /**
     * Provides 'Deal' instance with related data.
     * <p>
     * Throw RuntimeException if something goes wrong -
     * it is assumed that it will be catched by global exception handler.
     * Returns data of Deal entity + Type, Status, Sum, DealContractor data.
     *
     * @param id value of 'id' field of 'Deal' entity that is in the search
     * @return ResponseEntity with founded result and OK status - if successful,
     * NOT_FOUND status - if could not find Deal with passed id
     */
    @Operation(summary = "Receive Deal", description = "Receives Deal entity by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deal received",
                    content = @Content(schema = @Schema(implementation = DealGet.class))),
            @ApiResponse(responseCode = "404", description = "Deal not received - invalid data",
                    content = @Content(schema = @Schema(type = "string", example = "error message"))),
            @ApiResponse(responseCode = "500", description = "Deal receiving was failed",
                    content = @Content(schema = @Schema(type = "string", example = "error message")))
    })
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<?> get(@PathVariable("id") UUID id) {
        try {
            Optional<DealGet> result = service.get(id);
            if (result.isPresent()) {
                LOGGER.info("Deal obtained {}", String.format("{ \"id\":\"%s\" }", id));
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                LOGGER.warn("Deal not obtained {}; There is no deal entity with such id",
                        String.format("{ \"id\":\"%s\" }", id));
                return new ResponseEntity<>("There is no deal entity with such id.", HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException exception) {
            LOGGER.error("Deal obtaining was failed - {}", exception.getMessage());
            throw new DataAccessException(exception.getMessage()) {};
        }
    }

    /**
     * Provides list of 'Deal' instances with related data.
     * <p>
     * Throw RuntimeException if something goes wrong -
     * it is assumed that it will be catched by global exception handler.
     * Filter deals with passed dealSearch.
     * Returns data of Deal entity + Type, Status, Sum, DealContractor data.
     *
     * @param dealSearch contains filtering fields
     * @param page number of page that will be returned
     * @param size amount of returned DealGet instances
     * @return ResponseEntity with founded result and OK status - if successful,
     * NO_CONTENT status - if could not find any Deal entities
     */
    @Operation(summary = "Receive Deals with filtering parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deal list received",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DealGet.class)))),
            @ApiResponse(responseCode = "204", description = "Deal list not received"),
            @ApiResponse(responseCode = "500", description = "Deal list receiving was failed",
                    content = @Content(schema = @Schema(type = "string", example = "error message")))
    })
    @PostMapping("/search")
    @Override
    public ResponseEntity<?> search(@RequestBody DealSearch dealSearch,
                                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            List<DealGet> result = service.search(dealSearch, page, size);
            if (!result.isEmpty()) {
                LOGGER.info("Deal list obtained {}", String.format("{ \"count\":%d }", result.size()));
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                LOGGER.warn("Deal list not obtained { \"count\":0 }; Could not find any suitable deal");
                return new ResponseEntity<>("There is no matched Deal entity.", HttpStatus.NO_CONTENT);
            }
        } catch (DataAccessException exception) {
            LOGGER.error("Deal list obtaining was failed - {}", exception.getMessage());
            throw new DataAccessException(exception.getMessage()) {};
        }
    }

    /**
     * Provides .zip archive file with filtering DealGet instances.
     * <p>
     * Throw RuntimeException if something goes wrong -
     * it is assumed that it will be catched by global exception handler.
     * Filter deals with passed dealSearch.
     * Returns data of Deal entity + Type, Status, Sum, DealContractor data.
     * Results presented in Deal.xlsx file inside .zip archive.
     *
     * @param dealSearch contains filtering fields
     * @return ResponseEntity with created .zip archive and OK status - if successful,
     * INTERNAL_SERVER_ERROR status - if export failed
     * (could not write .xlsx file, could not create .zip archive)
     */
    @Operation(summary = "Receive Deals as file with filtering parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deal list exported",
                    content = @Content(schema = @Schema(implementation = InputStreamResource.class))),
            @ApiResponse(responseCode = "500", description = "Deal list exporting was failed",
                    content = @Content(schema = @Schema(type = "string", example = "error message")))
    })
    @PostMapping("/search/export")
    @Override
    public ResponseEntity<?> export(@RequestBody DealSearch dealSearch) {
        try {
            Optional<InputStreamResource> resource = service.export(dealSearch);
            if (resource.isPresent()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/zip");
                LOGGER.info("Deal exported");
                return new ResponseEntity<>(resource.get(), headers, HttpStatus.OK);
            } else {
                LOGGER.error("Deal not exported; Error occurred during .xlsx file writing or .zip archive creating");
                return new ResponseEntity<>("Error occurred during .xlsx file writing or .zip archive creating.",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (DataAccessException exception) {
            LOGGER.error("Deal exporting was failed - {}", exception.getMessage());
            throw new DataAccessException(exception.getMessage()) {};
        }

    }

}
