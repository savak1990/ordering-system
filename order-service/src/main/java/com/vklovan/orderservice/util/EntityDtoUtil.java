package com.vklovan.orderservice.util;

import com.vklovan.orderservice.dto.*;
import com.vklovan.orderservice.entity.PurchaseOrder;
import org.hibernate.criterion.Order;

public class EntityDtoUtil {

    public static void setTransactionRequestDto(RequestContext rc) {
        TransactionRequestDto dto = new TransactionRequestDto(rc.getProductDto().price());
        rc.setTransactionRequestDto(dto);
    }

    public static PurchaseOrder getPurchaseOrder(RequestContext rc) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setUserId(rc.getPurchaseOrderRequestDto().userId());
        purchaseOrder.setProductId(rc.getPurchaseOrderRequestDto().productId());
        purchaseOrder.setAmount(rc.getProductDto().price().doubleValue());

        TransactionStatus status = rc.getTransactionResponseDto().status();
        OrderStatus orderStatus = TransactionStatus.APPROVED.equals(status)
                ? OrderStatus.COMPLETED
                : OrderStatus.FAILED;
        purchaseOrder.setStatus(orderStatus);
        return purchaseOrder;
    }

    public static PurchaseOrderResponseDto getPurchaseOrderResponseDto(PurchaseOrder po) {
        return new PurchaseOrderResponseDto(
                po.getId(),
                po.getUserId(),
                po.getProductId(),
                po.getAmount(),
                po.getStatus());
    }
}
