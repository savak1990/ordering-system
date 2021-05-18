package com.vklovan.userservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.reactive.server.WebTestClient;

@Configuration
public class TestConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public WebTestClient webTestClient() {
        return WebTestClient
                .bindToApplicationContext(applicationContext)
                .build();
    }

}
