package com.vklovan.orderservice.dto;

import java.math.BigDecimal;

// TODO Use client library approach or openApi generator to generate dto classes
public record TransactionRequestDto(BigDecimal amount) {
}
