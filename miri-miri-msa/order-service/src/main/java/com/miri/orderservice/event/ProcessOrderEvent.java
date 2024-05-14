package com.miri.orderservice.event;

import com.miri.coremodule.dto.kafka.OrderRequestEventDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProcessOrderEvent extends ApplicationEvent {

    private final OrderRequestEventDto orderRequestEventDto;
    private final Long orderId;

    public ProcessOrderEvent(Object source, OrderRequestEventDto orderRequestEventDto, Long orderId) {
        super(source);
        this.orderRequestEventDto = orderRequestEventDto;
        this.orderId = orderId;
    }
}
