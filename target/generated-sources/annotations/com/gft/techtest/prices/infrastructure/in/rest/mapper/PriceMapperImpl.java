package com.gft.techtest.prices.infrastructure.in.rest.mapper;

import com.gft.techtest.prices.domain.model.Price;
import com.gft.techtest.prices.infrastructure.in.rest.dto.PriceRs;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-14T12:48:57+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class PriceMapperImpl implements PriceMapper {

    @Override
    public PriceRs mapToPriceRs(Price model) {
        if ( model == null ) {
            return null;
        }

        long productId = 0L;
        long brandId = 0L;
        int priceList = 0;
        OffsetDateTime startDate = null;
        OffsetDateTime endDate = null;
        BigDecimal price = null;
        String currency = null;

        productId = model.productId();
        brandId = model.brandId();
        priceList = model.priceList();
        startDate = model.startDate();
        endDate = model.endDate();
        price = model.price();
        currency = model.currency();

        PriceRs priceRs = new PriceRs( productId, brandId, priceList, startDate, endDate, price, currency );

        return priceRs;
    }
}
