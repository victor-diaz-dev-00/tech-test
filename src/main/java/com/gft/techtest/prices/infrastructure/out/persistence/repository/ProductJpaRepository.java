package com.gft.techtest.prices.infrastructure.out.persistence.repository;

import com.gft.techtest.prices.infrastructure.out.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
}
