package com.vklovan.userservice.service;

import com.vklovan.userservice.dto.TransactionRequestDto;
import com.vklovan.userservice.dto.TransactionResponseDto;
import com.vklovan.userservice.dto.TransactionStatus;
import com.vklovan.userservice.entity.UserTransaction;
import com.vklovan.userservice.repository.UserRepository;
import com.vklovan.userservice.repository.UserTransactionRepository;
import com.vklovan.userservice.util.UserTransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserTransactionService {

    private final UserRepository userRepository;
    private final UserTransactionRepository userTransactionRepository;

    public Mono<TransactionResponseDto> createTransaction(
            final int userId,
            final TransactionRequestDto requestDto) {
        return userRepository.updateUserBalance(userId, requestDto.getAmount())
                .filter(Boolean::booleanValue)
                .map(b -> UserTransactionMapper.toEntity(requestDto, userId))
                .flatMap(userTransactionRepository::save)
                .map(ut -> UserTransactionMapper.toDto(requestDto, userId, TransactionStatus.APPROVED))
                .defaultIfEmpty(UserTransactionMapper.toDto(requestDto, userId, TransactionStatus.DECLINED));
    }

    public Flux<UserTransaction> getByUserId(int userId) {
        return userTransactionRepository.findByUserId(userId);
    }
}
