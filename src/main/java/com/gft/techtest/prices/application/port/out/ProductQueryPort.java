package com.gft.techtest.prices.application.port.out;

import java.util.Optional;

public interface ProductQueryPort {
    Optional<String> findNameById(Long productId);
}
