package com.gft.techtest.prices.infrastructure.in.rest.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponse() {
        ErrorResponse response = ErrorResponse.of("boom", 409, "/prices");

        assertEquals("boom", response.message());
        assertEquals(409, response.status());
        assertEquals("/prices", response.path());
    }
}
