package com.vklovan.userservice.util;

import com.vklovan.userservice.dto.TransactionRequestDto;
import com.vklovan.userservice.dto.TransactionResponseDto;
import com.vklovan.userservice.dto.TransactionStatus;
import com.vklovan.userservice.entity.UserTransaction;

import java.time.LocalDateTime;

public class UserTransactionMapper {

    public static UserTransaction toEntity(
            TransactionRequestDto requestDto,
            final int userId) {
        UserTransaction userTransaction = new UserTransaction();
        userTransaction.setUserId(userId);
        userTransaction.setAmount(requestDto.getAmount());
        userTransaction.setTransactionDate(LocalDateTime.now());
        return userTransaction;
    }

    public static TransactionResponseDto toDto(
            TransactionRequestDto requestDto,
            final int userId,
            TransactionStatus status) {
        TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
        transactionResponseDto.setAmount(requestDto.getAmount());
        transactionResponseDto.setUserId(userId);
        transactionResponseDto.setStatus(status);
        return transactionResponseDto;
    }

}
