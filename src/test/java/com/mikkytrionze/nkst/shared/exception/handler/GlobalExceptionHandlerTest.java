package com.mikkytrionze.nkst.shared.exception.handler;

import static org.junit.jupiter.api.Assertions.*;

import com.mikkytrionze.nkst.shared.dto.response.ErrorResponse;
import com.mikkytrionze.nkst.shared.exception.BadRequestException;
import com.mikkytrionze.nkst.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleDataIntegrityViolation() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Duplicate entry");

        ResponseEntity<ErrorResponse> response = handler.handleConflict(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CONFLICT: A record with these details already exist!.", response.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void shouldHandleResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Church not found");

        ResponseEntity<ErrorResponse> response = handler.handleNotFoundException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Church not found", response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void shouldHandleBadRequestException() {
        BadRequestException exception = new BadRequestException("Invalid input");

        ResponseEntity<ErrorResponse> response = handler.handleBadException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid input", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }
}
