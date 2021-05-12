package com.vklovan.productservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.vklovan.productservice.config.MongoTestConfig;
import com.vklovan.productservice.dto.ProductDto;
import com.vklovan.productservice.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(MongoTestConfig.class)
class ProductServiceIntegrationTests {

    @Container
    static MongoDBContainer mongoDBContainer
            = new MongoDBContainer("mongo:latest");

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ReactiveMongoTemplate mongoTemplate;

    @Autowired
    ObjectMapper mapper;

    Faker faker = Faker.instance();

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @AfterEach
    void cleanup() {
        mongoTemplate.dropCollection(Product.class).block();
    }


    @Test
    void getAll_NoProductsInDb_EmptyResponse() {
        webTestClient.get()
                .uri("/products")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("[]");
    }

    @Test
    void getAll_OneProductInDb_ReturnOneProduct() {

        Product product = product("test", "1");
        String id = mongoTemplate.insert(product).block().getId();

        webTestClient.get()
                .uri("/products")
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                    .jsonPath("$[0].id").isEqualTo(id)
                    .jsonPath("$[0].description").isEqualTo("test")
                    .jsonPath("$[0].price").isEqualTo("1");

    }

    @Test
    void getAll_TenRandomProductInDb_ReturnTenProducts() {
        List<Product> products = generateProducts(10);
        List<ProductDto> expected = new ArrayList<>();

        mongoTemplate.insertAll(products)
                .doOnNext(p -> expected.add(productDto(p.getId(), p.getDescription(), p.getPrice())))
                .blockLast();

        webTestClient.get()
                .uri("/products")
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBodyList(ProductDto.class)
                    .hasSize(10)
                    .contains(expected.toArray(new ProductDto[0]));
    }

    @Test
    void getById_ProductExists_ReturnProduct() {
        Product productToInsert = product("test", "1");
        Product product = mongoTemplate.insert(productToInsert).block();

        webTestClient.get()
                .uri("/products/{id}", product.getId())
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                    .jsonPath("$.id").isEqualTo(product.getId())
                    .jsonPath("$.description").isEqualTo(product.getDescription())
                    .jsonPath("$.price").isEqualTo(product.getPrice());
    }

    @Test
    void getById_NoProduct_NotFound() {
        webTestClient.get()
                .uri("/products/{id}", "dffdfdkfdjf")
                .exchange()
                .expectStatus()
                    .isEqualTo(HttpStatus.NOT_FOUND)
                .expectBody()
                    .isEmpty();
    }

    @Test
    void create_NoProductInDb_Created() {
        ProductDto product = productDto("test", "1");
        webTestClient.post()
                .uri("/products")
                .bodyValue(product)
                .exchange()
                .expectStatus()
                    .isCreated()
                .expectBody()
                    .jsonPath("$.description").isEqualTo("test")
                    .jsonPath("$.price").isEqualTo("1");

        assertEquals(1, mongoTemplate.estimatedCount(Product.class).block());
    }

    @Test
    void update_ProductUnavailable_NotFound() {
        ProductDto newProductDto = productDto("test1", "2");
        webTestClient.put()
                .uri("/products/{id}", "test")
                .bodyValue(newProductDto)
                .exchange()
                .expectStatus()
                    .isEqualTo(HttpStatus.NOT_FOUND)
                .expectBody()
                    .isEmpty();
    }

    @Test
    void update_ProductExists_ProductUpdated() {

        Product existingProduct = product("test", "1");
        String id = mongoTemplate.insert(existingProduct).block().getId();

        ProductDto newProductDto = productDto("test1", "2");
        webTestClient.put()
                .uri("/products/{id}", id)
                .bodyValue(newProductDto)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                    .jsonPath("$.id").isEqualTo(id)
                    .jsonPath("$.description").isEqualTo("test1")
                    .jsonPath("$.price").isEqualTo("2");

        Product updatedDbProduct = mongoTemplate.findById(id, Product.class).block();
        assertNotNull(updatedDbProduct);
        assertEquals(newProductDto.getDescription(), updatedDbProduct.getDescription());
        assertEquals(newProductDto.getPrice(), updatedDbProduct.getPrice());
    }

    @Test
    void delete_ProductExists_NoContent() {
        Product existingProduct = product("test", "1");
        String id = mongoTemplate.insert(existingProduct).block().getId();

        webTestClient.delete()
                .uri("/products/{id}", id)
                .exchange()
                .expectStatus()
                    .isNoContent()
                .expectBody()
                    .isEmpty();

        StepVerifier.create(mongoTemplate.findAll(Product.class))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void delete_ProductUnavailable_NoContent() {
        webTestClient.delete()
                .uri("/products/{id}", "test")
                .exchange()
                .expectStatus()
                    .isNoContent()
                .expectBody()
                    .isEmpty();
    }

    private static Product product(String description, String price) {
        return new Product(null, description, price);
    }

    private static ProductDto productDto(String description, String price) {
        return new ProductDto(null, description, price);
    }

    private static Product product(String id, String description, String price) {
        return new Product(id, description, price);
    }

    private static ProductDto productDto(String id, String description, String price) {
        return new ProductDto(id, description, price);
    }

    private List<Product> generateProducts(int count) {
        return IntStream
                .range(0, count)
                .mapToObj(i -> product(faker.commerce().productName(), faker.commerce().price()))
                .collect(Collectors.toList());
    }

}
