package com.vklovan.orderservice.client;

import com.vklovan.orderservice.dto.TransactionRequestDto;
import com.vklovan.orderservice.dto.TransactionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserClient {

    private final WebClient webClient;

    public UserClient(@Value("${service.url.users}") String url) {
        log.info("Users endpoint: {}", url);
        webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public Mono<TransactionResponseDto> authorizeTransaction(int userId, TransactionRequestDto requestDto) {
        return webClient
                .post()
                .uri("{id}/transactions", userId)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(TransactionResponseDto.class);
    }
}
