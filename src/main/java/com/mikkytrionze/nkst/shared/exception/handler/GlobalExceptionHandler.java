package com.mikkytrionze.nkst.shared.exception.handler;

import com.mikkytrionze.nkst.shared.dto.response.ErrorResponse;
import com.mikkytrionze.nkst.shared.exception.BadRequestException;
import com.mikkytrionze.nkst.shared.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(DataIntegrityViolationException exception) {
        log.error("Database conflict: {}", exception.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("CONFLICT: A record with these details already exist!.")
                .status(HttpStatus.CONFLICT.value())
                .timestamp(Instant.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(ResourceNotFoundException resourceNotFoundException) {
        log.warn("Resource not found: {}", resourceNotFoundException.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(resourceNotFoundException.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadException(BadRequestException badRequestException) {
        log.warn("Bad request encountered: {}", badRequestException.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(badRequestException.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
