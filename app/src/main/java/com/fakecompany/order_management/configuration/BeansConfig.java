package com.fakecompany.order_management.configuration;

import com.fakecompany.order_management.shared.infrastructure.persistence.UuidIdentifiedEntityEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public UuidIdentifiedEntityEventListener uuidIdentifiedEntityEventListener() {
        return new UuidIdentifiedEntityEventListener();
    }

}
