package com.vklovan.userservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserTransaction {

    @Id
    private Integer id;

    private Integer userId;

    private BigDecimal amount;

    private LocalDateTime transactionDate;

}
