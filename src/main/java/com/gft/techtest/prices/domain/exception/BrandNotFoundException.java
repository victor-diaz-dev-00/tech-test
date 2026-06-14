package com.gft.techtest.prices.domain.exception;

public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException(Long brandId) {
        super("Brand not found: " + brandId);
    }
}
