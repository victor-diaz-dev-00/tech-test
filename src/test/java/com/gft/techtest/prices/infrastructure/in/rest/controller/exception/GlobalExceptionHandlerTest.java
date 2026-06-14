package com.gft.techtest.prices.infrastructure.in.rest.controller.exception;

import com.gft.techtest.prices.domain.exception.ApplicablePriceNotFoundException;
import com.gft.techtest.prices.domain.exception.BrandNotFoundException;
import com.gft.techtest.prices.domain.exception.DuplicateConfigurationException;
import com.gft.techtest.prices.domain.exception.ProductNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleBrandNotFound() {
        var response = handler.handleBrandNotFound(new BrandNotFoundException(1L), webRequest());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Brand not found: 1", response.getBody().message());
    }

    @Test
    void shouldHandleProductNotFound() {
        var response = handler.handleBrandNotFound(new ProductNotFoundException(35455L), webRequest());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Product not found: 35455", response.getBody().message());
    }

    @Test
    void shouldHandleApplicablePriceNotFound() {
        OffsetDateTime now = OffsetDateTime.parse("2026-06-14T10:00:00Z");
        var exception = new ApplicablePriceNotFoundException(1L, 35455L, now);

        var response = handler.handleBrandNotFound(exception, webRequest());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldHandleDuplicateConfiguration() {
        var response = handler.handleDuplicateConfiguration(new DuplicateConfigurationException("Multiple prices"), webRequest());

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Multiple prices", response.getBody().message());
    }

    @Test
    void shouldHandleMissingParameter() {
        var response = handler.handleMissingParameter(new MissingServletRequestParameterException("date", "String"), webRequest());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Required parameter 'date' is missing", response.getBody().message());
    }

    @Test
    void shouldHandleConstraintViolation() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Invalid date format");

        ConstraintViolationException ex = new ConstraintViolationException("Validation failed", Set.of(violation));

        var response = handler.handleConstraintViolation(ex, webRequest());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid date format", response.getBody().message());
    }

    @Test
    void shouldHandleTypeMismatch() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("brandId");
        when(ex.getValue()).thenReturn("invalid-id");

        var response = handler.handleTypeMismatch(ex, webRequest());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid parameter 'brandId': invalid-id", response.getBody().message());
    }

    @Test
    void shouldHandleGenericException() {
        var response = handler.handleGenericException(new RuntimeException("Fatal database crash"), webRequest());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal server error", response.getBody().message());
    }

    private ServletWebRequest webRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/prices");
        return new ServletWebRequest(request);
    }
}