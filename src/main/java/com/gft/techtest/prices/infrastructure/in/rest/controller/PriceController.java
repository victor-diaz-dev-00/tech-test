package com.gft.techtest.prices.infrastructure.in.rest.controller;

import com.gft.techtest.prices.application.port.in.ApplicablePricePort;
import com.gft.techtest.prices.infrastructure.in.rest.dto.ErrorResponse;
import com.gft.techtest.prices.infrastructure.in.rest.dto.PriceRs;
import com.gft.techtest.prices.infrastructure.in.rest.mapper.PriceMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@Validated
@Tag(name = "Price", description = "Price query operations")
public class PriceController {

    private final ApplicablePricePort applicablePricePort;
    private final PriceMapper priceMapper;

    public PriceController(ApplicablePricePort applicablePricePort, PriceMapper priceMapper) {
        this.applicablePricePort = applicablePricePort;
        this.priceMapper = priceMapper;
    }

    @Operation(
            summary = "Get applicable price",
            description = "Retrieves the applicable price for a product and brand at a specific date and time"
    )
    @ApiResponse(responseCode = "200", description = "Price found successfully",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = PriceRs.class)))
    @ApiResponse(responseCode = "404", description = "No applicable price found",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid parameters provided",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/prices")
    public ResponseEntity<PriceRs> getApplicablePrice(
            @Parameter(description = "Application date and time in ISO format (yyyy-MM-ddTHH:mm:ss)", required = true, example = "2020-06-14T10:00:00+00:00")
            @RequestParam(value = "date")
            @NotNull(message = "Date parameter is required")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime date,

            @Parameter(description = "Product identifier", required = true, example = "35455")
            @RequestParam(value = "productId")
            @NotNull(message = "ProductId parameter is required")
            @Min(value = 1, message = "ProductId must be greater than 0") long productId,

            @Parameter(description = "Brand identifier", required = true, example = "1")
            @RequestParam(value = "brandId")
            @NotNull(message = "BrandId parameter is required")
            @Min(value = 1, message = "BrandId must be greater than 0") long brandId
    ) {
        return applicablePricePort.getApplicablePrice(brandId, productId, date)
                .map(priceMapper::mapToPriceRs)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}