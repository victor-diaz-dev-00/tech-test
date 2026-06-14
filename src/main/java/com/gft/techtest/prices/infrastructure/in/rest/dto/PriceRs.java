package com.gft.techtest.prices.infrastructure.in.rest.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Schema(description = "Response containing the applicable price information")
public record PriceRs(@Schema(description = "Product identifier", example = "35455") long productId,
                      @Schema(description = "Brand identifier", example = "1") long brandId,
                      @Schema(description = "Price list identifier", example = "1") int priceList,
                      @Schema(description = "Start date of price validity", example = "2020-06-14T00:00:00+00:00") OffsetDateTime startDate,
                      @Schema(description = "End date of price validity", example = "2020-12-31T23:59:59+00:00") OffsetDateTime endDate,
                      @Schema(description = "Final price to apply", example = "35.50") BigDecimal price,
                      @Schema(description = "Currency code", example = "EUR") String currency) {
    @JsonCreator
    public PriceRs(
            @JsonProperty("productId") long productId,
            @JsonProperty("brandId") long brandId,
            @JsonProperty("priceList") int priceList,
            @JsonProperty("startDate") OffsetDateTime startDate,
            @JsonProperty("endDate") OffsetDateTime endDate,
            @JsonProperty("price") BigDecimal price,
            @JsonProperty("currency") String currency) {
        this.productId = productId;
        this.brandId = brandId;
        this.priceList = priceList;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.currency = currency;
    }
}
