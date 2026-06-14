package com.gft.techtest.prices.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PriceTest {

    private final OffsetDateTime startDate = OffsetDateTime.parse("2026-06-14T10:00:00Z");
    private final OffsetDateTime endDate = OffsetDateTime.parse("2026-12-31T23:59:59Z");

    @Test
    void shouldBeReflexive() {
        Price price = new Price(1L, 35455L, 1, startDate, endDate, 0, new BigDecimal("35.50"), "EUR");
        assertEquals(price, price);
    }

    @Test
    void shouldBeSymmetricAndEqualWithSameValues() {
        Price price1 = new Price(1L, 35455L, 1, startDate, endDate, 0, new BigDecimal("35.50"), "EUR");
        Price price2 = new Price(1L, 35455L, 1, startDate, endDate, 0, new BigDecimal("35.50"), "EUR");

        assertEquals(price1, price2);
        assertEquals(price2, price1);
    }

    @Test
    void shouldBeTransitive() {
        Price price1 = new Price(1L, 35455L, 1, startDate, endDate, 0, new BigDecimal("35.50"), "EUR");
        Price price2 = new Price(1L, 35455L, 1, startDate, endDate, 0, new BigDecimal("35.50"), "EUR");
        Price price3 = new Price(1L, 35455L, 1, startDate, endDate, 0, new BigDecimal("35.50"), "EUR");

        assertEquals(price1, price2);
        assertEquals(price2, price3);
        assertEquals(price1, price3);
    }

    @Test
    void shouldNotBeEqualWithNullOrDifferentType() {
        Price price = new Price(1L, 35455L, 1, startDate, endDate, 0, new BigDecimal("35.50"), "EUR");

        assertNotEquals(null, price);

        assertNotEquals("un string cualquiera", price);
    }

    @Test
    void shouldNotBeEqualIfAnyPrimitiveFieldDiffers() {
        Price base = new Price(1L, 35455L, 1, startDate, endDate, 0, new BigDecimal("35.50"), "EUR");

        assertNotEquals(base, new Price(2L, 35455L, 1, startDate, endDate, 0, new BigDecimal("35.50"), "EUR"));
        assertNotEquals(base, new Price(1L, 99999L, 1, startDate, endDate, 0, new BigDecimal("35.50"), "EUR"));
        assertNotEquals(base, new Price(1L, 35455L, 2, startDate, endDate, 0, new BigDecimal("35.50"), "EUR"));
        assertNotEquals(base, new Price(1L, 35455L, 1, startDate, endDate, 1, new BigDecimal("35.50"), "EUR"));
    }

    @Test
    void shouldNotBeEqualIfAnyObjectFieldDiffers() {
        Price base = new Price(1L, 35455L, 1, startDate, endDate, 0, new BigDecimal("35.50"), "EUR");
        assertNotEquals(base, new Price(1L, 35455L, 1, startDate.plusDays(1), endDate, 0, new BigDecimal("35.50"), "EUR"));
        assertNotEquals(base, new Price(1L, 35455L, 1, startDate, endDate.minusDays(1), 0, new BigDecimal("35.50"), "EUR"));
        assertNotEquals(base, new Price(1L, 35455L, 1, startDate, endDate, 0, new BigDecimal("35.51"), "EUR"));
        assertNotEquals(base, new Price(1L, 35455L, 1, startDate, endDate, 0, new BigDecimal("35.50"), "USD"));
    }
}