package com.vklovan.userservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Table("users")
public class User {

    @Id
    private Integer id;
    private String name;
    private BigDecimal balance;

}
