package com.gft.techtest.prices.infrastructure.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Error response structure")
public record ErrorResponse(
        @Schema(description = "Error message", example = "No applicable price found")
        String message,

        @Schema(description = "Timestamp when error occurred")
        LocalDateTime timestamp,

        @Schema(description = "HTTP status code", example = "404")
        int status,

        @Schema(description = "Request path", example = "/prices")
        String path
) {
    public static ErrorResponse of(String message, int status, String path) {
        return new ErrorResponse(message, LocalDateTime.now(), status, path);
    }
}