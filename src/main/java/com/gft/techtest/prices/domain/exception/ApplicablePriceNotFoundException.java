package com.gft.techtest.prices.domain.exception;

import java.time.OffsetDateTime;

public class ApplicablePriceNotFoundException extends RuntimeException {
    public ApplicablePriceNotFoundException(Long brandId, Long productId, OffsetDateTime date) {
        super("Applicable price not found for brand: " + brandId+ " and product: "+ productId + " for date: "+date);
    }
}
