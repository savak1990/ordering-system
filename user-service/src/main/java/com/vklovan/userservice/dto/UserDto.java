package com.vklovan.userservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDto {

    private Integer id;
    private String name;
    private BigDecimal balance;

}
