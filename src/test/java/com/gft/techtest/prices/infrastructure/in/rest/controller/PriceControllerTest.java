package com.gft.techtest.prices.infrastructure.in.rest.controller;

import com.gft.techtest.prices.application.port.in.ApplicablePricePort;
import com.gft.techtest.prices.domain.model.Price;
import com.gft.techtest.prices.infrastructure.in.rest.mapper.PriceMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PriceControllerTest {

    @Test
    void shouldReturnApplicablePrice() throws Exception {
        ApplicablePricePort applicablePricePort = mock(ApplicablePricePort.class);
        PriceMapper priceMapper = Mappers.getMapper(PriceMapper.class);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new PriceController(applicablePricePort, priceMapper)).build();
        OffsetDateTime date = OffsetDateTime.parse("2020-06-14T10:00:00Z");
        Price price = new Price(1L, 35455L, 1,
                OffsetDateTime.parse("2020-06-14T00:00:00Z"),
                OffsetDateTime.parse("2020-12-31T23:59:59Z"),
                1, BigDecimal.valueOf(35.50), "EUR");

        when(applicablePricePort.getApplicablePrice(1L, 35455L, date)).thenReturn(Optional.of(price));

        mockMvc.perform(get("/prices")
                        .param("date", "2020-06-14T10:00:00Z")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("EUR"));
    }
}
