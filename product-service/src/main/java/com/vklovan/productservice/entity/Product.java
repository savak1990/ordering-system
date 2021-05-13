package com.vklovan.productservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String id;
    private String title;
    private String description;
    private BigDecimal price;

}
