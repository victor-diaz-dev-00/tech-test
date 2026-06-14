package com.gft.techtest.prices.infrastructure.out.persistence.mapper;

import com.gft.techtest.prices.domain.model.Price;
import com.gft.techtest.prices.infrastructure.out.persistence.entity.PriceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PricePersistenceMapper {

    Price toDomain(PriceEntity entity);
}
