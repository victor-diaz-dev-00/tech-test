package com.gft.techtest.prices.infrastructure.out.persistence.adapter;

import com.gft.techtest.prices.application.port.out.BrandQueryPort;
import com.gft.techtest.prices.infrastructure.out.persistence.entity.BrandEntity;
import com.gft.techtest.prices.infrastructure.out.persistence.repository.BrandJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BrandQueryAdapter implements BrandQueryPort {
    private final BrandJpaRepository brandJpaRepository;

    public BrandQueryAdapter(BrandJpaRepository brandJpaRepository) {
        this.brandJpaRepository = brandJpaRepository;
    }

    @Override
    public Optional<String> findNameById(Long brandId) {
        return brandJpaRepository.findById(brandId).map(BrandEntity::getName);
    }
}
