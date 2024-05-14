package com.miri.orderservice.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CancelOrderEvent extends ApplicationEvent {
    private final Long orderId;
    private final Long goodsId;
    private final Integer quantity;

    public CancelOrderEvent(Object source, Long orderId, Long goodsId, Integer quantity) {
        super(source);
        this.orderId = orderId;
        this.goodsId = goodsId;
        this.quantity = quantity;
    }
}
