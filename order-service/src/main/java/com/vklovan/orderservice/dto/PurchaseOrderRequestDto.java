package com.vklovan.orderservice.dto;

public record PurchaseOrderRequestDto(
        Integer userId,
        String productId
) {
}
