package com.vklovan.productservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CountOffsetCriteria {

    @Min(value = 0, message = "Count should be greated then 0")
    @Max(value = Long.MAX_VALUE, message = "Count should be less then Integer max value")
    private long count = Long.MAX_VALUE;

    @Min(value = 0, message = "Offset should be greated then 0")
    @Max(value = Long.MAX_VALUE, message = "Count should be less then Integer max value")
    private long offset = 0;
}
