package com.example.Deal.Controller;

import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.DealGet;
import com.example.Deal.DTO.DealSearch;
import com.example.Deal.Service.DealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Handles incoming http-requests at URL '/deal'
 * <p>
 * Contains some methods to work with 'Deal' entity.
 */
@RestController
@RequestMapping("/deal")
public class DealController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DealController.class);

    private final DealService service;

    @Autowired
    public DealController(DealService service) {
        this.service = service;
    }

    /**
     * Creates and updates 'Deal' entities.
     * <p>
     * Can be called at url '/deal/save'
     *
     * @param deal instance that must be saved or updated
     * @return ResponseEntity with result and OK status - if successful, ResponseEntity with INTERNAL_SERVER_ERROR - else
     */
    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody Deal deal) {
        try {
            Deal result = service.save(deal);
            LOGGER.info("Deal added {}", String.format("{ \"id\":\"%s\" }", deal.getId()));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("Deal not added {}; Error occurred {}",
                    String.format("{ \"id\":\"%s\" }", deal.getId()), exception.getMessage());
            return new ResponseEntity<>("Deal adding/updating was failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates 'Deal' status.
     * <p>
     * Can be called at url '/deal/change/status'.
     *
     * @param deal contains Deal id (that must be updated) and Status (that must be assigned)
     * @return ResponseEntity with updated result and OK status - if successful,
     * NOT_FOUND status - if could not find Deal with passed id,
     * INTERNAL_SERVER_ERROR status - else
     */
    @PatchMapping("/change/status")
    public ResponseEntity<?> change(@RequestBody Deal.DealStatusUpdate deal) {
        try {
            Deal result = service.change(deal);
            if (result != null) {
                LOGGER.info("Deal status updated {}", deal.desc());
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                LOGGER.warn("Deal status not updated {}; There is no deal entity with such id", deal.desc());
                return new ResponseEntity<>("There is no deal with such id.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            LOGGER.error("Deal status not updated {}; Error occurred {}",
                    deal.desc(), exception.getMessage());
            return new ResponseEntity<>("Deal status updating was failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Provides 'Deal' instance with related data.
     * <p>
     * Can be called at url '/deal/{id}'.
     * Returns data of Deal entity + Type, Status, Sum, DealContractor data.
     *
     * @param id value of 'id' field of 'Deal' entity that is in the search
     * @return ResponseEntity with founded result and OK status - if successful,
     * NOT_FOUND status - if could not find Deal with passed id
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") UUID id) {
        Optional<DealGet> result = service.get(id);
        if (result.isPresent()) {
            LOGGER.info("Deal obtained {}", String.format("{ \"id\":\"%s\" }", id));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            LOGGER.info("Deal not obtained {}; There is no deal entity with such id", String.format("{ \"id\":\"%s\" }", id));
            return new ResponseEntity<>("There is no deal entity with such id", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Provides list of 'Deal' instances with related data.
     * <p>
     * Can be called at url '/deal/search'.
     * Filter deals with passed dealSearch.
     * Returns data of Deal entity + Type, Status, Sum, DealContractor data.
     *
     * @param dealSearch contains filtering fields
     * @param page number of page that will be returned
     * @param size amount of returned DealGet instances
     * @return ResponseEntity with founded result and OK status - if successful,
     * NO_CONTENT status - if could not find any Deal entities
     */
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody DealSearch dealSearch,
                                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<DealGet> result = service.search(dealSearch, page, size);
        if (!result.isEmpty()) {
            LOGGER.info("Deal list obtained {}", String.format("{ \"count\":%d }", result.size()));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            LOGGER.info("Deal list not obtained { \"count\":0 }; Could not find any suitable deal");
            return new ResponseEntity<>("Could not find any suitable deal", HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Provides .zip archive file with filtering DealGet instances.
     * <p>
     * Can be called at url '/deal/search/export'.
     * Filter deals with passed dealSearch.
     * Returns data of Deal entity + Type, Status, Sum, DealContractor data.
     * Results presented in Deal.xlsx file inside .zip archive.
     *
     * @param dealSearch contains filtering fields
     * @return ResponseEntity with created .zip archive and OK status - if successful,
     * INTERNAL_SERVER_ERROR status - if export failed
     * (could not write .xlsx file, could not create .zip archive)
     */
    @PostMapping("/search/export")
    public ResponseEntity<?> export(@RequestBody DealSearch dealSearch) throws IOException {
        Optional<File> optFile = service.export(dealSearch);
        if (optFile.isPresent()) {
            File file = optFile.get();
            File tmpFile = new File("tmp_" + file.getName());

            try (FileInputStream fileInputStream = new FileInputStream(file);
                 FileOutputStream fileOutputStream = new FileOutputStream(tmpFile);
                 ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {

                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                byte[] buffer = new byte[1024];
                int data;
                while((data = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, data);
                }
                zipOutputStream.close();
                InputStreamResource result = new InputStreamResource(new FileInputStream(tmpFile));
                file.delete();
                tmpFile.delete();
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/zip");

                LOGGER.info("Deal exported");
                return new ResponseEntity<>(result, headers, HttpStatus.OK);
            } catch (IOException exception) {
                LOGGER.info("Deal not exported; Error occurred during .zip archive creating");
                return new ResponseEntity<>("Deal exporting was failed", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            LOGGER.error("Deal not exported; Error occurred during .xlsx file writing");
            return new ResponseEntity<>("Deal exporting was failed.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
