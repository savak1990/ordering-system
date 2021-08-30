package com.vklovan.orderservice.controller;

import com.vklovan.orderservice.dto.PurchaseOrderRequestDto;
import com.vklovan.orderservice.dto.PurchaseOrderResponseDto;
import com.vklovan.orderservice.service.OrderFulfillmentService;
import com.vklovan.orderservice.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final OrderFulfillmentService orderFulfillmentService;
    private final OrderQueryService orderQueryService;

    @PostMapping
    public Mono<PurchaseOrderResponseDto> order(
            @RequestBody Mono<PurchaseOrderRequestDto> requestDtoMono) {
        return orderFulfillmentService.processOrder(requestDtoMono);
    }

    @GetMapping("user/{userId}")
    public Flux<PurchaseOrderResponseDto> getOrdersByUserId(
            @PathVariable int userId) {
        return orderQueryService.getOrdersByUserId(userId);
    }
}
