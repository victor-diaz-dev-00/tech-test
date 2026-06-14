package com.gft.techtest.prices.application.port.out;

import java.util.Optional;

public interface BrandQueryPort {
    Optional<String> findNameById(Long brandId);
}
