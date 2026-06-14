package com.gft.techtest.prices.infrastructure.in.rest.mapper;

import com.gft.techtest.prices.domain.model.Price;
import com.gft.techtest.prices.infrastructure.in.rest.dto.PriceRs;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceMapperTest {

    private final PriceMapper mapper = Mappers.getMapper(PriceMapper.class);

    @Test
    void shouldMapDomainToRestDto() {
        Price price = new Price(1L, 35455L, 1,
                OffsetDateTime.parse("2020-06-14T00:00:00Z"),
                OffsetDateTime.parse("2020-12-31T23:59:59Z"),
                1,
                BigDecimal.valueOf(35.50),
                "EUR");

        PriceRs result = mapper.mapToPriceRs(price);

        assertEquals("EUR", result.currency());
        assertEquals(35455L, result.productId());
    }
}
