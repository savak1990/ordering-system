package com.vklovan.userservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionResponseDto {

    private Integer userId;
    private BigDecimal amount;
    private TransactionStatus status;
}
