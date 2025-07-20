package com.example.Deal.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handles RuntimeExceptions from controller classes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Catches runtime exceptions and returns ResponseEntity with passed error message.
     *
     * @param exception catched exception
     * @return ResponseEntity with error message
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
