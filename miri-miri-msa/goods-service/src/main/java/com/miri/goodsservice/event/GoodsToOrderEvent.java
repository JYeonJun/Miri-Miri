package com.miri.goodsservice.event;

import com.miri.goodsservice.dto.goods.RequestGoodsDto.OrderGoodsReqDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GoodsToOrderEvent extends ApplicationEvent {
    private final Long userId;
    private final OrderGoodsReqDto orderGoodsReqDto;
    private final Integer goodsPrice;

    public GoodsToOrderEvent(Object source, Long userId, OrderGoodsReqDto orderGoodsReqDto, Integer goodsPrice) {
        super(source);
        this.userId = userId;
        this.orderGoodsReqDto = orderGoodsReqDto;
        this.goodsPrice = goodsPrice;
    }
}
