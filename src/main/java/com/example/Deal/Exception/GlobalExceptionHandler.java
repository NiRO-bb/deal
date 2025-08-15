package com.example.Deal.Exception;

import com.example.Deal.Controller.ContractorToRoleControllerImpl;
import com.example.Deal.Controller.DealContractorControllerImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handles RuntimeExceptions from controller classes.
 */
@RestControllerAdvice(assignableTypes = {ContractorToRoleControllerImpl.class,
        DealContractorControllerImpl.class, DealContractorControllerImpl.class})
public class GlobalExceptionHandler {

    /**
     * Catches DataAccessException thrown cases and returns ResponseEntity with passed error message.
     *
     * @param exception intercepted exception
     * @return ResponseEntity with error message
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
