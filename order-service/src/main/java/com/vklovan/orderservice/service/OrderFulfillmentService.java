package com.vklovan.orderservice.service;

import com.vklovan.orderservice.client.ProductClient;
import com.vklovan.orderservice.client.UserClient;
import com.vklovan.orderservice.dto.PurchaseOrderRequestDto;
import com.vklovan.orderservice.dto.PurchaseOrderResponseDto;
import com.vklovan.orderservice.dto.RequestContext;
import com.vklovan.orderservice.repository.PurchaseOrderRepository;
import com.vklovan.orderservice.util.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OrderFulfillmentService {

    private final ProductClient productClient;
    private final UserClient userClient;
    private final PurchaseOrderRepository orderRepository;

    public Mono<PurchaseOrderResponseDto> processOrder(Mono<PurchaseOrderRequestDto> requestDtoMono) {
        return requestDtoMono
                .map(RequestContext::new)
                .flatMap(this::productRequestResponse)
                .doOnNext(EntityDtoUtil::setTransactionRequestDto)
                .flatMap(this::userRequestResponse)
                .map(EntityDtoUtil::getPurchaseOrder)
                .map(orderRepository::save) // blocking
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<RequestContext> productRequestResponse(RequestContext rc) {
        return productClient.getProductById(rc.getPurchaseOrderRequestDto().productId())
                .doOnNext(rc::setProductDto)
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
                .thenReturn(rc);
    }

    private Mono<RequestContext> userRequestResponse(RequestContext rc) {
        return userClient
                .authorizeTransaction(
                    rc.getPurchaseOrderRequestDto().userId(),
                    rc.getTransactionRequestDto())
                .doOnNext(rc::setTransactionResponseDto)
                .thenReturn(rc);
    }
}
