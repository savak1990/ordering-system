package com.vklovan.userservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequestDto {

    private BigDecimal amount;

}
