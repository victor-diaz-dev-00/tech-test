package com.gft.techtest.prices.infrastructure.out.persistence.adapter;

import com.gft.techtest.prices.domain.model.Price;
import com.gft.techtest.prices.infrastructure.out.persistence.entity.BrandEntity;
import com.gft.techtest.prices.infrastructure.out.persistence.entity.PriceEntity;
import com.gft.techtest.prices.infrastructure.out.persistence.entity.ProductEntity;
import com.gft.techtest.prices.infrastructure.out.persistence.mapper.PricePersistenceMapper;
import com.gft.techtest.prices.infrastructure.out.persistence.repository.PriceJpaRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PriceQueryAdapterTest {

    @Test
    void shouldUseDatabaseFilterAndReturnSinglePrice() {
        PriceJpaRepository priceJpaRepository = mock(PriceJpaRepository.class);
        PricePersistenceMapper mapper = mock(PricePersistenceMapper.class);
        PriceQueryAdapter adapter = new PriceQueryAdapter(priceJpaRepository, mapper, "database-filter");
        OffsetDateTime date = OffsetDateTime.parse("2020-06-14T10:00:00Z");
        PriceEntity priceEntity = priceEntity();
        Price domain = domainPrice();

        when(priceJpaRepository.findByBrand_IdAndProduct_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(1L, 35455L, date, date))
                .thenReturn(List.of(priceEntity));
        when(mapper.toDomain(priceEntity)).thenReturn(domain);

        List<Price> result = adapter.findApplicablePrice(1L, 35455L, date);

        assertEquals(List.of(domain), result);
    }

    @Test
    void shouldThrowOnUnsupportedQueryMode() {
        PriceJpaRepository priceJpaRepository = mock(PriceJpaRepository.class);
        PricePersistenceMapper mapper = mock(PricePersistenceMapper.class);
        PriceQueryAdapter adapter = new PriceQueryAdapter(priceJpaRepository, mapper, "other");

        assertThrows(IllegalArgumentException.class,
                () -> adapter.findApplicablePrice(1L, 35455L, OffsetDateTime.parse("2020-06-14T10:00:00Z")));
    }

    private PriceEntity priceEntity() {
        PriceEntity entity = new PriceEntity();
        entity.setBrand(new BrandEntity(1L, "ZARA"));
        entity.setProduct(new ProductEntity(35455L, "PRODUCT"));
        entity.setPriceList(1);
        entity.setStartDate(OffsetDateTime.parse("2020-06-14T00:00:00Z"));
        entity.setEndDate(OffsetDateTime.parse("2020-12-31T23:59:59Z"));
        entity.setPriority(1);
        entity.setPrice(BigDecimal.valueOf(35.50));
        entity.setCurrency("EUR");
        return entity;
    }

    private Price domainPrice() {
        return new Price(1L, 35455L, 1,
                OffsetDateTime.parse("2020-06-14T00:00:00Z"),
                OffsetDateTime.parse("2020-12-31T23:59:59Z"),
                2, BigDecimal.valueOf(35.50), "EUR");
    }
}
