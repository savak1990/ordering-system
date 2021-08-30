package com.vklovan.orderservice.dto;

public record PurchaseOrderResponseDto(
        Integer orderId,
        Integer userId,
        String productId,
        double amount,
        OrderStatus status
) {
}
