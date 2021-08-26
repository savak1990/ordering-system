package com.vklovan.productservice.controller;

import com.vklovan.productservice.dto.CountOffsetCriteria;
import com.vklovan.productservice.dto.PriceCriteria;
import com.vklovan.productservice.dto.ProductDto;
import com.vklovan.productservice.service.ProductService;
import com.vklovan.productservice.util.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService service;
    private final ProductMapper mapper;

    @GetMapping
    public Flux<ProductDto> getAll(
            @Valid CountOffsetCriteria countOffsetCriteria) {
        return service.getAll(countOffsetCriteria).map(mapper::toDto);
    }

    @GetMapping("price-range")
    public Flux<ProductDto> priceRange(
            @Valid PriceCriteria priceCriteria,
            @Valid CountOffsetCriteria countOffsetCriteria) {
        log.info("priceRange: {} countOffset: {}", priceCriteria, countOffsetCriteria);
        return service.priceRange(priceCriteria, countOffsetCriteria)
                .map(mapper::toDto);
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<ProductDto>> getById(@PathVariable String id) {
        return service.getById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductDto> insert(
            @RequestBody @Valid Mono<ProductDto> productDtoMono) {
        return productDtoMono
                .map(mapper::toEntity)
                .flatMap(service::insert)
                .map(mapper::toDto);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<ProductDto>> update(
            @PathVariable String id,
            @RequestBody @Valid Mono<ProductDto> productDtoMono) {
        return productDtoMono
                .map(mapper::toEntity)
                .flatMap(p -> service.update(id, p))
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return service.delete(id);
    }

}
