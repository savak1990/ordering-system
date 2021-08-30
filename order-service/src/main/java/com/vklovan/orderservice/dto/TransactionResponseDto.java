package com.vklovan.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;

// TODO Use client library approach or openApi generator to generate dto classes
public record TransactionResponseDto(
        Integer userId,
        BigDecimal amount,
        TransactionStatus status
) {
}
