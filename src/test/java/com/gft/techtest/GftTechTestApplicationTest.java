package com.gft.techtest;

import com.gft.techtest.prices.application.port.in.ApplicablePricePort;
import com.gft.techtest.prices.infrastructure.in.rest.controller.PriceController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class GftTechTestApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
        assertTrue(applicationContext.containsBean("gftTechTestApplication"));
        assertTrue(applicationContext.getBeanNamesForType(PriceController.class).length > 0);
        assertTrue(applicationContext.getBeanNamesForType(ApplicablePricePort.class).length > 0);
    }
}
