package com.muslimtrivia.Trivia;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class}) // handle all types of exceptions
    public ResponseEntity<Object> handleAll(Exception ex) {
        com.muslimtrivia.Trivia.auth.ApiError apiError = new com.muslimtrivia.Trivia.auth.ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR, // or whatever HTTP status you want to return
                ex.getMessage()
        );
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
