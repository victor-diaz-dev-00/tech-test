package com.gft.techtest.prices.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

public record Price(long brandId, long productId, int priceList, OffsetDateTime startDate, OffsetDateTime endDate,
                    int priority, BigDecimal price, String currency) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price(
                long id, long productId1, int list, OffsetDateTime date, OffsetDateTime endDate1, int priority1,
                BigDecimal price2, String currency1
        ))) return false;
        return brandId == id && productId == productId1 && priceList == list && priority == priority1 && Objects.equals(startDate, date) && Objects.equals(endDate, endDate1) && Objects.equals(price, price2) && Objects.equals(currency, currency1);
    }

}
