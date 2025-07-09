package com.example.Deal.Controller;

import com.example.Deal.DTO.DealContractor;
import com.example.Deal.Service.DealContractorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Handles incoming http-requests at URL '/deal-contractor'
 * <p>
 * Contains some methods to work with 'DealContractor' entity.
 */
@RestController
@RequestMapping("/deal-contractor")
public class DealContractorController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DealContractorController.class);

    private final DealContractorService service;

    @Autowired
    public DealContractorController(DealContractorService service) {
        this.service = service;
    }

    /**
     * Creates and updates 'DealContractor' entities.
     * <p>
     * Can be called at URL '/deal-contractor/save'
     *
     * @param contractor instance that must be updated
     * @return OK status and added/updated instance - if successful,
     * BAD_REQUEST status - if could not find DealContractor with passed id,
     * INTERNAL_SERVER_ERROR status - if error occurred
     */
    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody DealContractor contractor) {
        try {
            DealContractor result = service.save(contractor);
            if (result != null) {
                LOGGER.info("DealContractor added {}", String.format("{ \"id\":\"%s\" }", contractor.getId()));
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                LOGGER.warn("DealContractor not added {}; Main contractor already exists", String.format("{ \"id\":\"%s\" }", contractor.getId()));
                return new ResponseEntity<>("Main contractor of this deal already exists. " +
                        "Change \"main\" value to \"false\".", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("DealContractor not added {}; Error occurred {}",
                    String.format("{ \"id\":\"%s\" }", contractor.getId()), exception.getMessage());
            return new ResponseEntity<>("DealContractor saving was failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Logically deletes DealContractor entities.
     * <p>
     * Updates 'is_active' field value to 'false' value.
     *
     * @param id value of 'id' field of 'DealContractor' that must be deleted
     * @return NO_CONTENT status - if successful,
     * NOT_FOUND status - if there is no DealContractor entity with passed key,
     * INTERNAL_SERVER_ERROR - if error occurred
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        try {
            DealContractor result = service.delete(id);
            if (result != null) {
                LOGGER.info("DealContractor deleted {}", String.format("{ \"id\":\"%s\" }", id));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                LOGGER.warn("DealContractor not deleted {}; DealContractor not found", String.format("{ \"id\":\"%s\" }", id));
                return new ResponseEntity<>("There is no DealContractor entity with such key.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            LOGGER.error("DealContractor not deleted {}; Error occurred {}",
                    String.format("{ \"id\":\"%s\" }", id), exception.getMessage());
            return new ResponseEntity<>("DealContractor deleting was failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
