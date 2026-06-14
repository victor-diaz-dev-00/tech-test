package com.gft.techtest.prices.application.usecases;

import com.gft.techtest.prices.application.port.in.ApplicablePricePort;
import com.gft.techtest.prices.application.port.out.BrandQueryPort;
import com.gft.techtest.prices.application.port.out.PriceQueryPort;
import com.gft.techtest.prices.application.port.out.ProductQueryPort;
import com.gft.techtest.prices.domain.exception.ApplicablePriceNotFoundException;
import com.gft.techtest.prices.domain.exception.BrandNotFoundException;
import com.gft.techtest.prices.domain.exception.DuplicateConfigurationException;
import com.gft.techtest.prices.domain.exception.ProductNotFoundException;
import com.gft.techtest.prices.domain.model.Price;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicablePricePortUseCase implements ApplicablePricePort {
    private final ProductQueryPort productQueryPort;
    private final BrandQueryPort brandQueryPort;
    private final PriceQueryPort priceQueryPort;

    public ApplicablePricePortUseCase(ProductQueryPort productQueryPort, BrandQueryPort brandQueryPort, PriceQueryPort priceQueryPort) {
        this.productQueryPort = productQueryPort;
        this.brandQueryPort = brandQueryPort;
        this.priceQueryPort = priceQueryPort;
    }

    @Override
    public Optional<Price> getApplicablePrice(long brandId, long productId, OffsetDateTime applicationDateFrom) {
        validateDomainConstraints(brandId, productId);

        List<Price> prices = priceQueryPort.findApplicablePrice(brandId, productId, applicationDateFrom);

        if (prices.isEmpty()) {
            throw new ApplicablePriceNotFoundException(brandId, productId, applicationDateFrom);
        }

        return selectSinglePrice(prices.stream().sorted(Comparator.comparingInt(Price::priority).reversed()).toList());
    }

    private Optional<Price> selectSinglePrice(List<Price> prices) {
        Price selected = prices.get(0);
        if (prices.size() > 1 && prices.get(1).priority() == (selected.priority())) {
            throw new DuplicateConfigurationException("Multiple prices share the highest priority for the same criteria");
        }

        return Optional.of(selected);
    }

    private void validateDomainConstraints(long brandId, long productId) {
        productQueryPort.findNameById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        brandQueryPort.findNameById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));
    }
}
