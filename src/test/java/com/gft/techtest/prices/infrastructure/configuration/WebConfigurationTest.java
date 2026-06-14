package com.gft.techtest.prices.infrastructure.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class WebConfigurationTest {

    @Test
    void shouldRegisterSwaggerRedirects() {
        WebConfiguration configuration = new WebConfiguration();
        ViewControllerRegistry registry = mock(ViewControllerRegistry.class);

        configuration.addViewControllers(registry);

        verify(registry).addRedirectViewController("/", "/swagger-ui.html");
        verify(registry).addRedirectViewController("/swagger-ui.html", "/swagger-ui/index.html");
        verify(registry).setOrder(1);
        verifyNoMoreInteractions(registry);
    }
}
