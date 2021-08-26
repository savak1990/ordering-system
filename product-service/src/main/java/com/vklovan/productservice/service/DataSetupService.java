package com.vklovan.productservice.service;

import com.github.javafaker.Faker;
import com.vklovan.productservice.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@Service
@Profile("dev")
@Slf4j
public class DataSetupService implements CommandLineRunner {

    Faker faker = Faker.instance();

    @Autowired
    ProductService productService;

    @Autowired
    ReactiveMongoTemplate mongoTemplate;

    @Override
    public void run(String... args) {
        log.info("Try populate the Product DB with dev data");

        mongoTemplate.collectionExists(Product.class)
                .flatMapMany(exists -> exists
                        ? Flux.error(new RuntimeException("Product DB already exists"))
                        : Flux.range(0,1000))
                .map(i -> generateProduct())
                .flatMap(productService::insert)
                .subscribe(
                        p -> {},
                        err -> log.info(err.getMessage()),
                        () -> log.info("Product DB populated with dev data"));
    }

    private Product generateProduct() {
        Product product = new Product();
        product.setTitle(faker.commerce().productName());
        product.setDescription(faker.commerce().material());
        product.setPrice(Double.parseDouble(faker.commerce().price(5, 1000)));
        return product;
    }
}
