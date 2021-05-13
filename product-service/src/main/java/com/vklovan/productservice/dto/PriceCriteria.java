package com.vklovan.productservice.dto;

import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PriceCriteria {

    @NotNull(message = "Minimum price should not be null")
    @Digits(integer = 5, fraction = 2, message = "Wrong price format for minimum price")
    @DecimalMin(value = "0", message = "Price should be greater then 0")
    private BigDecimal min;

    @NotNull(message = "Maximum price should not be null")
    @Digits(integer = 5, fraction = 2, message = "Wrong price format for maximum price")
    @DecimalMin(value = "0", message = "Price should be greated then 0")
    private BigDecimal max;
}
