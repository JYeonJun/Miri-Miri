package com.miri.orderservice.service.order;

import com.miri.coremodule.dto.kafka.OrderRequestEventDto;

public interface OrderInternalService {

    void processOrder(OrderRequestEventDto orderRequestEventDto);

    void updateOrderStatusOnFailure(Long orderId);
}
