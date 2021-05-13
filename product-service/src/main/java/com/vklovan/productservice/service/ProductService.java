package com.vklovan.productservice.service;

import com.vklovan.productservice.dto.CountOffsetCriteria;
import com.vklovan.productservice.dto.PriceCriteria;
import com.vklovan.productservice.entity.Product;
import com.vklovan.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Flux<Product> getAll(CountOffsetCriteria countOffsetCriteria) {
        return repository.findAll()
                .skip(countOffsetCriteria.getOffset())
                .take(countOffsetCriteria.getCount());
    }

    public Flux<Product> priceRange(PriceCriteria priceCriteria, CountOffsetCriteria countOffsetCriteria) {
        return repository.findByPriceBetween(
                Range.leftOpen(priceCriteria.getMin(), priceCriteria.getMax()))
                .skip(countOffsetCriteria.getOffset())
                .take(countOffsetCriteria.getCount());
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
