package com.vklovan.userservice.integration;

import com.vklovan.userservice.entity.User;
import com.vklovan.userservice.entity.UserTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.vklovan.userservice.util.TestUtil.*;
import static org.springframework.data.relational.core.query.Query.empty;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTests {

    @Autowired
    R2dbcEntityTemplate userEntityTemplate;

    @Autowired
    WebTestClient webTestClient;

    @AfterEach
    void cleanup() {
        userEntityTemplate.delete(empty(), UserTransaction.class).block();
        userEntityTemplate.delete(empty(), User.class).block();
    }

    @Test
    void testGetAllEmpty() {
        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                    .json("[]");
    }

    @Test
    void testGetAll() {
        userEntityTemplate
                .insert(user("test", 500.0))
                .block();

        webTestClient.get()
                .uri("/users")
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                    .jsonPath("$[0].name").isEqualTo("test")
                    .jsonPath("$[0].balance").isEqualTo(500.0);
    }

    @Test
    void testGetById_Exists() {
        User insertedUser = userEntityTemplate
                .insert(user("test", 500.0))
                .block();

        webTestClient.get()
                .uri("/users/{id}", insertedUser.getId())
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                    .jsonPath("$.name").isEqualTo(insertedUser.getName())
                    .jsonPath("$.balance").isEqualTo(insertedUser.getBalance())
                    .jsonPath("$.id").isEqualTo(insertedUser.getId());
    }

    @Test
    void testGetById_NotFound() {
        webTestClient.get()
                .uri("/users/{id}", 1)
                .exchange()
                .expectStatus()
                    .isNotFound()
                .expectBody()
                    .isEmpty();
    }

    @Test
    void testPostUser() {
        webTestClient.post()
                .uri("/users")
                .bodyValue(user("test", 500))
                .exchange()
                .expectStatus()
                    .isCreated()
                .expectBody()
                    .jsonPath("$.name").isEqualTo("test")
                    .jsonPath("$.balance").isEqualTo(500)
                    .jsonPath("$.id").exists();
    }

    @Test
    void testPutUser_NotFound() {
        webTestClient.put()
                .uri("/users/{id}", 1)
                .bodyValue(userDto("test", 500))
                .exchange()
                .expectStatus()
                    .isNotFound()
                .expectBody()
                    .isEmpty();
    }

    @Test
    void testPutUser_Exists() {
        User insertedUser = userEntityTemplate
                .insert(user("beforeChange", 0))
                .block();

        webTestClient.put()
                .uri("/users/{id}", insertedUser.getId())
                .bodyValue(user("test", 500))
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                    .jsonPath("$.id").isEqualTo(insertedUser.getId())
                    .jsonPath("$.name").isEqualTo("test")
                    .jsonPath("$.balance").isEqualTo(500);

    }

    @Test
    void testDeleteUser_NotFound() {
        webTestClient.delete()
                .uri("/users/{id}", 1)
                .exchange()
                .expectStatus()
                    .isNoContent()
                .expectBody()
                    .isEmpty();
    }

    @Test
    void testDeleteUser_Exists() {
        User insertedUser = userEntityTemplate
                .insert(user("test", 500))
                .block();

        webTestClient.delete()
                .uri("/users/{id}", insertedUser.getId())
                .exchange()
                .expectStatus()
                    .isNoContent()
                .expectBody()
                    .isEmpty();
    }

    @Test
    void testDeleteUser_UserTransactionsExist() {
        User testUser = userEntityTemplate
                .insert(user("test", 500))
                .block();

        UserTransaction testTransaction = userEntityTemplate
                .insert(transaction(testUser.getId(), 100))
                .block();

        webTestClient.delete()
                .uri("/users/{id}", testUser.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
    }

}
