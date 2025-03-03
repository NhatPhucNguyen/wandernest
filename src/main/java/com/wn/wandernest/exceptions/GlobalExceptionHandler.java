package com.wn.wandernest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.converter.HttpMessageNotReadableException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            org.springframework.http.HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        if (mostSpecificCause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) mostSpecificCause;
            if (ife.getTargetType().isEnum()) {
                String message = String.format("Invalid value '%s' for enum type '%s'. Accepted values are: %s",
                        ife.getValue(), ife.getTargetType().getSimpleName(), ife.getTargetType().getEnumConstants());
                return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}