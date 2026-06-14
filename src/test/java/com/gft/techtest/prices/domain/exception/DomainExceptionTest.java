package com.gft.techtest.prices.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DomainExceptionTest {

    @Test
    void shouldBuildBrandNotFoundMessage() {
        assertEquals("Brand not found: 2", new BrandNotFoundException(2L).getMessage());
    }

    @Test
    void shouldBuildDuplicateConfigurationMessage() {
        assertEquals("dup", new DuplicateConfigurationException("dup").getMessage());
    }
}
