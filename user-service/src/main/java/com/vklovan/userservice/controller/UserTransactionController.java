package com.vklovan.userservice.controller;

import com.vklovan.userservice.dto.TransactionRequestDto;
import com.vklovan.userservice.dto.TransactionResponseDto;
import com.vklovan.userservice.entity.UserTransaction;
import com.vklovan.userservice.service.UserTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("users/{id}/transactions")
@RequiredArgsConstructor
public class UserTransactionController {

    private final UserTransactionService userTransactionService;

    @GetMapping
    public Flux<UserTransaction> getByUserId(@PathVariable int id) {
        return userTransactionService.getByUserId(id);
    }

    @PostMapping
    public Mono<TransactionResponseDto> createTransaction(
            @PathVariable int id,
            @RequestBody Mono<TransactionRequestDto> requestDtoMono) {
        return requestDtoMono
                .flatMap(request -> userTransactionService.createTransaction(id, request));
    }
}
