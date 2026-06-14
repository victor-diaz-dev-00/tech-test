package com.gft.techtest.prices.application.port.out;

import com.gft.techtest.prices.domain.model.Price;

import java.time.OffsetDateTime;
import java.util.List;

public interface PriceQueryPort {
    List<Price> findApplicablePrice(long brandId, long productId, OffsetDateTime applicationDate);
}
