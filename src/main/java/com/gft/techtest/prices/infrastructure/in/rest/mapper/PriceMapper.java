package com.gft.techtest.prices.infrastructure.in.rest.mapper;

import com.gft.techtest.prices.domain.model.Price;
import com.gft.techtest.prices.infrastructure.in.rest.dto.PriceRs;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    PriceRs mapToPriceRs(Price model);
}
