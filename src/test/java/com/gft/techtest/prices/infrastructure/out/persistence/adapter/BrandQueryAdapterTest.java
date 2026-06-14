package com.gft.techtest.prices.infrastructure.out.persistence.adapter;

import com.gft.techtest.prices.infrastructure.out.persistence.entity.BrandEntity;
import com.gft.techtest.prices.infrastructure.out.persistence.repository.BrandJpaRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BrandQueryAdapterTest {

    @Test
    void shouldReturnBrandNameWhenBrandExists() {
        BrandJpaRepository brandJpaRepository = mock(BrandJpaRepository.class);
        BrandQueryAdapter adapter = new BrandQueryAdapter(brandJpaRepository);
        BrandEntity brand = new BrandEntity(1L, "ZARA");

        when(brandJpaRepository.findById(1L)).thenReturn(Optional.of(brand));

        assertEquals(Optional.of("ZARA"), adapter.findNameById(1L));
    }

    @Test
    void shouldReturnEmptyWhenBrandDoesNotExist() {
        BrandJpaRepository brandJpaRepository = mock(BrandJpaRepository.class);
        BrandQueryAdapter adapter = new BrandQueryAdapter(brandJpaRepository);

        when(brandJpaRepository.findById(99L)).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), adapter.findNameById(99L));
    }
}
