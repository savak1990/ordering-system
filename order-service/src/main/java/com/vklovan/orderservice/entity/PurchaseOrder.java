package com.vklovan.orderservice.entity;

import com.vklovan.orderservice.dto.OrderStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class PurchaseOrder {

    @Id
    @GeneratedValue
    private int id;
    private String productId;
    private int userId;
    private double amount;
    private OrderStatus status;
}
