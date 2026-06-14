package com.gft.techtest.prices.infrastructure.out.persistence.adapter;

import com.gft.techtest.prices.application.port.out.PriceQueryPort;
import com.gft.techtest.prices.domain.model.Price;
import com.gft.techtest.prices.infrastructure.out.persistence.mapper.PricePersistenceMapper;
import com.gft.techtest.prices.infrastructure.out.persistence.repository.PriceJpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class PriceQueryAdapter implements PriceQueryPort {
    private final PriceJpaRepository priceJpaRepository;
    private final PricePersistenceMapper pricePersistenceMapper;
    private final String queryMode;

    public PriceQueryAdapter(PriceJpaRepository priceJpaRepository,
                             PricePersistenceMapper pricePersistenceMapper,
                             @Value("${feature.query-mode:java-filter}") String queryMode) {
        this.priceJpaRepository = priceJpaRepository;
        this.pricePersistenceMapper = pricePersistenceMapper;
        this.queryMode = queryMode;
    }

    @Override
    public List<Price> findApplicablePrice(long brandId, long productId, OffsetDateTime applicationDate) {
        return switch (queryMode) {
            case "database-filter" -> findApplicablePriceWithDatabaseFilter(brandId, productId, applicationDate);
            case "java-filter" -> findApplicablePriceWithJavaFilter(brandId, productId, applicationDate);
            default -> throw new IllegalArgumentException("Unsupported feature.query-mode: " + queryMode);
        };
    }

    private List<Price> findApplicablePriceWithDatabaseFilter(long brandId, long productId, OffsetDateTime applicationDate) {
        return priceJpaRepository.findByBrand_IdAndProduct_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                        brandId,
                        productId,
                        applicationDate,
                        applicationDate
                ).stream()
                .map(pricePersistenceMapper::toDomain)
                .toList();
    }

    private List<Price> findApplicablePriceWithJavaFilter(long brandId, long productId, OffsetDateTime applicationDate) {
        return priceJpaRepository.findByBrand_IdAndProduct_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        brandId,
                        productId,
                        applicationDate,
                        applicationDate
                )
                .stream()
                .map(pricePersistenceMapper::toDomain)
                .toList();
    }

}
