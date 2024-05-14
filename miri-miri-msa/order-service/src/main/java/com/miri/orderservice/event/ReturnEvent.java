package com.miri.orderservice.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReturnEvent extends ApplicationEvent {
    private final Long goodsId;
    private final Integer quantity;

    public ReturnEvent(Object source, Long goodsId, Integer quantity) {
        super(source);
        this.goodsId = goodsId;
        this.quantity = quantity;
    }
}
