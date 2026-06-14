package com.gft.techtest.prices.infrastructure.out.persistence.adapter;

import com.gft.techtest.prices.application.port.out.ProductQueryPort;
import com.gft.techtest.prices.infrastructure.out.persistence.entity.ProductEntity;
import com.gft.techtest.prices.infrastructure.out.persistence.repository.ProductJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductQueryAdapter implements ProductQueryPort {
    private final ProductJpaRepository productJpaRepository;

    public ProductQueryAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Optional<String> findNameById(Long productId) {
        return productJpaRepository.findById(productId).map(ProductEntity::getName);
    }
}
