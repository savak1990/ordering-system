package com.vklovan.productservice.controller;

import com.vklovan.productservice.dto.ProductDto;
import com.vklovan.productservice.service.ProductService;
import com.vklovan.productservice.util.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    private final ProductMapper mapper;

    @GetMapping
    public Flux<ProductDto> getAll() {
        return service.getAll().map(mapper::toDto);
    }

    @GetMapping("price-range")
    public Flux<ProductDto> priceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max) {
        return service.priceRange(min, max)
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
    public Mono<ProductDto> insert(@RequestBody Mono<ProductDto> productDtoMono) {
        return productDtoMono
                .map(mapper::toEntity)
                .flatMap(service::insert)
                .map(mapper::toDto);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<ProductDto>> update(
            @PathVariable String id,
            @RequestBody Mono<ProductDto> productDtoMono) {
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
