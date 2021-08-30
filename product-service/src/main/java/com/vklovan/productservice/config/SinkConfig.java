package com.vklovan.productservice.config;

import com.vklovan.productservice.dto.ProductDto;
import com.vklovan.productservice.entity.Product;
import com.vklovan.productservice.util.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
@RequiredArgsConstructor
public class SinkConfig {

    private final ProductMapper mapper;

    @Bean
    public Sinks.Many<Product> sink() {
        return Sinks.many().replay().limit(1);
    }

    @Bean
    public Flux<ProductDto> productBroadcast(Sinks.Many<Product> sink) {
        return sink.asFlux().map(mapper::toDto);
    }

}
