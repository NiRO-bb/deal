package com.example.Deal.Controller;

import com.example.Deal.Service.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides endpoints to get deal statuses.
 */
@RestController
@RequestMapping("deal/deal-status")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService service;

    /**
     * Returns all deal_status entities.
     *
     * @return
     */
    @Operation(summary = "Provides list of all possible deal statuses.")
    @ApiResponse(responseCode = "200", description = "Status list received.",
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

}
