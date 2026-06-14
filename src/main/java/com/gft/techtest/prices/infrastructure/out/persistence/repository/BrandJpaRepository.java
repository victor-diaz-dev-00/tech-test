package com.gft.techtest.prices.infrastructure.out.persistence.repository;

import com.gft.techtest.prices.infrastructure.out.persistence.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandJpaRepository extends JpaRepository<BrandEntity, Long> {
}
