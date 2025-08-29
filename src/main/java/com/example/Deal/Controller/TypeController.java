package com.example.Deal.Controller;

import com.example.Deal.DTO.Type;
import com.example.Deal.Service.TypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides endpoints to get and save deal types.
 */
@RestController
@RequestMapping("/deal/deal-type")
@RequiredArgsConstructor
public class TypeController {

    private final TypeService service;

    /**
     * Returns all deal_type entities.
     *
     * @return
     */
    @Operation(summary = "Provides list of all possible deal types.")
    @ApiResponse(responseCode = "200", description = "Type list received.",
            content = @Content(schema = @Schema(example = """
                    [
                        {
                            "id": "example_id",
                            "name": "example_name"
                        }
                    ]
                    """)))
    @GetMapping("/all")
    public ResponseEntity<?> get() {
        return new ResponseEntity<>(service.get(), HttpStatus.OK);
    }

    /**
     * Saves passed Type instance in repository.
     *
     * @param type instance must be saved
     * @return saved entity
     */
    @Operation(summary = "Saves Type.", description = "Saves passed type in repository.")
    @ApiResponse(responseCode = "200", description = "Type saved.",
            content = @Content(schema = @Schema(implementation = Type.class)))
    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody Type type) {
        return new ResponseEntity<>(service.save(type), HttpStatus.OK);
    }

}
