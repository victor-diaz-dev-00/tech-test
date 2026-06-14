package com.gft.techtest.prices.application.usecases;

import com.gft.techtest.prices.application.port.out.BrandQueryPort;
import com.gft.techtest.prices.application.port.out.PriceQueryPort;
import com.gft.techtest.prices.application.port.out.ProductQueryPort;
import com.gft.techtest.prices.domain.exception.ApplicablePriceNotFoundException;
import com.gft.techtest.prices.domain.exception.BrandNotFoundException;
import com.gft.techtest.prices.domain.exception.DuplicateConfigurationException;
import com.gft.techtest.prices.domain.model.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApplicablePricePortUseCasesTest {

    @Test
    void shouldReturnPriceWithBrandNameWhenBrandExists() {
        ProductQueryPort productQueryPort = mock(ProductQueryPort.class);
        BrandQueryPort brandQueryPort = mock(BrandQueryPort.class);
        PriceQueryPort priceQueryPort = mock(PriceQueryPort.class);
        ApplicablePricePortUseCases useCases = new ApplicablePricePortUseCases(productQueryPort, brandQueryPort, priceQueryPort);
        OffsetDateTime applicationDate = OffsetDateTime.parse("2020-06-14T10:00:00Z");
        Price price = new Price(1L, 35455L, 1, applicationDate, applicationDate, 1, BigDecimal.valueOf(35.50), "EUR");

        when(productQueryPort.findNameById(35455L)).thenReturn(Optional.of("PRODUCT"));
        when(brandQueryPort.findNameById(1L)).thenReturn(Optional.of("ZARA"));
        when(priceQueryPort.findApplicablePrice(1L, 35455L, applicationDate)).thenReturn(List.of(price));

        Optional<Price> result = useCases.getApplicablePrice(1L, 35455L, applicationDate);

        assertEquals(price, result.orElseThrow());
        verify(brandQueryPort).findNameById(1L);
        verify(priceQueryPort).findApplicablePrice(1L, 35455L, applicationDate);
    }

    @Test
    void shouldThrowWhenApplicationPriceNotExists() {
        ProductQueryPort productQueryPort = mock(ProductQueryPort.class);
        BrandQueryPort brandQueryPort = mock(BrandQueryPort.class);
        PriceQueryPort priceQueryPort = mock(PriceQueryPort.class);
        ApplicablePricePortUseCases useCases = new ApplicablePricePortUseCases(productQueryPort, brandQueryPort, priceQueryPort);
        OffsetDateTime applicationDate = OffsetDateTime.parse("2020-06-14T10:00:00Z");

        when(productQueryPort.findNameById(35455L)).thenReturn(Optional.of("PRODUCT"));
        when(brandQueryPort.findNameById(1L)).thenReturn(Optional.of("ZARA"));
        when(priceQueryPort.findApplicablePrice(1L, 35455L, applicationDate)).thenReturn(List.of());

        assertThrows(ApplicablePriceNotFoundException.class,
                () -> useCases.getApplicablePrice(1L, 35455L, applicationDate));
    }

    @Test
    void shouldThrowWhenBrandDoesNotExist() {
        ProductQueryPort productQueryPort = mock(ProductQueryPort.class);
        BrandQueryPort brandQueryPort = mock(BrandQueryPort.class);
        PriceQueryPort priceQueryPort = mock(PriceQueryPort.class);
        ApplicablePricePortUseCases useCases = new ApplicablePricePortUseCases(productQueryPort, brandQueryPort, priceQueryPort);
        when(productQueryPort.findNameById(35455L)).thenReturn(Optional.of("PRODUCT"));
        when(brandQueryPort.findNameById(2L)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class,
                () -> useCases.getApplicablePrice(2L, 35455L, OffsetDateTime.parse("2020-06-14T10:00:00Z")));
        verify(brandQueryPort).findNameById(2L);
    }

    @Test
    void shouldThrowWhenDuplicatedHighestPriority() {
        ProductQueryPort productQueryPort = mock(ProductQueryPort.class);
        BrandQueryPort brandQueryPort = mock(BrandQueryPort.class);
        PriceQueryPort priceQueryPort = mock(PriceQueryPort.class);
        ApplicablePricePortUseCases useCases = new ApplicablePricePortUseCases(productQueryPort, brandQueryPort, priceQueryPort);
        OffsetDateTime applicationDate = OffsetDateTime.parse("2020-06-14T10:00:00Z");
        Price first = domainPrice(2, BigDecimal.valueOf(45.50));
        Price second = domainPrice(2, BigDecimal.valueOf(35.50));
        when(productQueryPort.findNameById(35455L)).thenReturn(Optional.of("PRODUCT"));
        when(brandQueryPort.findNameById(1L)).thenReturn(Optional.of("ZARA"));
        when(priceQueryPort.findApplicablePrice(1L, 35455L, applicationDate)).thenReturn(List.of(first, second));

        assertThrows(DuplicateConfigurationException.class,
                () -> useCases.getApplicablePrice(1L, 35455L, applicationDate));
    }

    @Test
    void shouldUseJavaFilterAndReturnHighestPriority() {
        ProductQueryPort productQueryPort = mock(ProductQueryPort.class);
        BrandQueryPort brandQueryPort = mock(BrandQueryPort.class);
        PriceQueryPort priceQueryPort = mock(PriceQueryPort.class);
        ApplicablePricePortUseCases useCases = new ApplicablePricePortUseCases(productQueryPort, brandQueryPort, priceQueryPort);
        OffsetDateTime applicationDate = OffsetDateTime.parse("2020-06-14T10:00:00Z");
        Price first = domainPrice(1, BigDecimal.valueOf(45.50));
        Price second = domainPrice(2, BigDecimal.valueOf(35.50));
        when(productQueryPort.findNameById(35455L)).thenReturn(Optional.of("PRODUCT"));
        when(priceQueryPort.findApplicablePrice(1L, 35455L, applicationDate)).thenReturn(List.of(first, second));
        when(brandQueryPort.findNameById(1L)).thenReturn(Optional.of("ZARA"));

        Optional<Price> result = useCases.getApplicablePrice(1L, 35455L, applicationDate);

        assertEquals(second, result.orElseThrow());
        assertEquals(BigDecimal.valueOf(35.50), result.get().price());
        verify(brandQueryPort).findNameById(1L);
        verify(priceQueryPort).findApplicablePrice(1L, 35455L, applicationDate);
    }

    private Price domainPrice(int priority, BigDecimal amount) {
        return new Price(1L, 35455L, 1,
                OffsetDateTime.parse("2020-06-14T00:00:00Z"),
                OffsetDateTime.parse("2020-12-31T23:59:59Z"),
                priority, amount, "EUR");
    }
}
