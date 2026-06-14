package com.gft.techtest.prices.application.port.in;

import com.gft.techtest.prices.domain.model.Price;


import java.time.OffsetDateTime;
import java.util.Optional;

public interface ApplicablePricePort {
     Optional<Price> getApplicablePrice(long brandId, long productId, OffsetDateTime applicationDateFrom);
}
