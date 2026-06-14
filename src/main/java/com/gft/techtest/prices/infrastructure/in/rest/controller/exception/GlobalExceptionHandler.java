package com.gft.techtest.prices.infrastructure.in.rest.controller.exception;

import com.gft.techtest.prices.domain.exception.ApplicablePriceNotFoundException;
import com.gft.techtest.prices.domain.exception.BrandNotFoundException;
import com.gft.techtest.prices.domain.exception.DuplicateConfigurationException;
import com.gft.techtest.prices.domain.exception.ProductNotFoundException;
import com.gft.techtest.prices.infrastructure.in.rest.dto.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBrandNotFound(BrandNotFoundException ex, WebRequest request) {
        log.info("Resource not found execution at Brand [{}]: {}", sanitize(request), ex.getMessage());

        ErrorResponse error = ErrorResponse.of(ex.getMessage(), HttpStatus.NOT_FOUND.value(), sanitize(request));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBrandNotFound(ProductNotFoundException ex, WebRequest request) {
        log.info("Resource not found execution at Product [{}]: {}", sanitize(request), ex.getMessage());

        ErrorResponse error = ErrorResponse.of(ex.getMessage(), HttpStatus.NOT_FOUND.value(), sanitize(request));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ApplicablePriceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBrandNotFound(ApplicablePriceNotFoundException ex, WebRequest request) {
        log.info("Resource not found execution at Price [{}]: {}", sanitize(request), ex.getMessage());

        ErrorResponse error = ErrorResponse.of(ex.getMessage(), HttpStatus.NOT_FOUND.value(), sanitize(request));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DuplicateConfigurationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateConfiguration(DuplicateConfigurationException ex, WebRequest request) {
        log.warn("Business conflict detected at [{}]: {}", sanitize(request), ex.getMessage());

        ErrorResponse error = ErrorResponse.of(ex.getMessage(), HttpStatus.CONFLICT.value(), sanitize(request));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex, WebRequest request) {
        String message = String.format("Required parameter '%s' is missing", ex.getParameterName());
        ErrorResponse error = ErrorResponse.of(message, HttpStatus.BAD_REQUEST.value(), sanitize(request));
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        String message = ex.getConstraintViolations().stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("Validation error");
        ErrorResponse error = ErrorResponse.of(message, HttpStatus.BAD_REQUEST.value(), sanitize(request));
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = String.format("Invalid parameter '%s': %s", ex.getName(), ex.getValue());
        ErrorResponse error = ErrorResponse.of(message, HttpStatus.BAD_REQUEST.value(), sanitize(request));
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unhandled internal server error detected at URI [{}]: ", sanitize(request), ex);

        ErrorResponse error = ErrorResponse.of("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(), sanitize(request));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private String sanitize(WebRequest request) {
        String desc = request.getDescription(false);
        return desc.replace("uri=", "");
    }
}
