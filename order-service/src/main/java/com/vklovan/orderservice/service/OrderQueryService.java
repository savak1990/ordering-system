package com.vklovan.orderservice.service;

import com.vklovan.orderservice.dto.PurchaseOrderResponseDto;
import com.vklovan.orderservice.entity.PurchaseOrder;
import com.vklovan.orderservice.repository.PurchaseOrderRepository;
import com.vklovan.orderservice.util.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final PurchaseOrderRepository orderRepository;

    public Flux<PurchaseOrderResponseDto> getOrdersByUserId(int userId) {
        return Flux
                .fromStream(() -> orderRepository.findByUserId(userId).stream())
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic());
    }

}
