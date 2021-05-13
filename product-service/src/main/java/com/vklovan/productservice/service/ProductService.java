package com.vklovan.productservice.service;

import com.vklovan.productservice.entity.Product;
import com.vklovan.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Flux<Product> getAll() {
        return repository.findAll();
    }

    public Flux<Product> priceRange(BigDecimal min, BigDecimal max) {
        return repository.findByPriceBetween(min, max);
    }

    public Mono<Product> getById(String id) {
        return repository.findById(id);
    }

    public Mono<Product> insert(Product product) {
        return repository.insert(product);
    }

    public Mono<Product> update(String id, Product newProduct) {
        return repository.findById(id)
                .map(p -> newProduct)
                .doOnNext(p -> p.setId(id))
                .flatMap(repository::save);
    }

    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }
}
