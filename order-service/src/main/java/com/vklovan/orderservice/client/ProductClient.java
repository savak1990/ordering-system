package com.vklovan.orderservice.client;

import com.vklovan.orderservice.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductClient {

    private final WebClient webClient;

    public ProductClient(@Value("${service.url.products}") String url) {
        log.info("Products endpoint: {}", url);
        webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public Flux<ProductDto> getProducts() {
        return webClient
                .get()
                .retrieve()
                .bodyToFlux(ProductDto.class);
    }

    public Mono<ProductDto> getProductById(final String productId) {
        return webClient
                .get()
                .uri("{id}", productId)
                .retrieve()
                .bodyToMono(ProductDto.class);
    }
}
