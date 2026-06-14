package com.gft.techtest.prices.infrastructure.out.persistence.repository;

import com.gft.techtest.prices.infrastructure.out.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.OffsetDateTime;
import java.util.List;

public interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {

    List<PriceEntity> findByBrand_IdAndProduct_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(long brandId,
                                                                                                    long productId,
                                                                                                    OffsetDateTime applicationDate,
                                                                                                    OffsetDateTime applicationDate2
    );

    List<PriceEntity> findByBrand_IdAndProduct_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
            long brandId,
            long productId,
            OffsetDateTime applicationDate,
            OffsetDateTime applicationDate2
    );
}
