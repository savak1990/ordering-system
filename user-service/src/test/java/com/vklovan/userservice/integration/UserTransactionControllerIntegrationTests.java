package com.vklovan.userservice.integration;

import com.vklovan.userservice.entity.User;
import com.vklovan.userservice.entity.UserTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.vklovan.userservice.util.TestUtil.transactionRequest;
import static com.vklovan.userservice.util.TestUtil.user;
import static org.springframework.data.relational.core.query.Query.empty;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTransactionControllerIntegrationTests {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    R2dbcEntityTemplate userEntityTemplate;

    @AfterEach
    void cleanup() {
        userEntityTemplate.delete(empty(), UserTransaction.class).block();
        userEntityTemplate.delete(empty(), User.class).block();
    }

    @Test
    void testTransactionApproved() {
        User testUser = userEntityTemplate
                .insert(user("test", 100))
                .block();

        webTestClient.post()
                .uri("/users/{id}/transactions", testUser.getId())
                .bodyValue(transactionRequest(90))
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                    .jsonPath("$.userId").isEqualTo(testUser.getId())
                    .jsonPath("$.amount").isEqualTo(90)
                    .jsonPath("$.status").isEqualTo("APPROVED");

        webTestClient.get()
                .uri("/users/{id}", testUser.getId())
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                    .jsonPath("$.balance").isEqualTo(10);
    }

    @Test
    void testTransactionDeclined() {
        User testUser = userEntityTemplate
                .insert(user("test", 100))
                .block();

        webTestClient.post()
                .uri("/users/{id}/transactions", testUser.getId())
                .bodyValue(transactionRequest(110))
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                    .jsonPath("$.userId").isEqualTo(testUser.getId())
                    .jsonPath("$.amount").isEqualTo(110)
                    .jsonPath("$.status").isEqualTo("DECLINED");

        webTestClient.get()
                .uri("/users/{id}", testUser.getId())
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                    .jsonPath("$.balance").isEqualTo(100);
    }

    @Test
    void testTransaction_noUsersExist() {
        webTestClient.post()
                .uri("/users/{id}/transactions", 1)
                .bodyValue(transactionRequest(90))
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                    .jsonPath("$.userId").isEqualTo(1)
                    .jsonPath("$.amount").isEqualTo(90)
                    .jsonPath("$.status").isEqualTo("DECLINED");
    }
}
