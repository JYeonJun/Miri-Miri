package com.miri.coremodule.dto.kafka;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PaymentRequestEventDto {
    private Long userId;
    private Long orderId;
    private Long goodsId;
    private Integer quantity;
    private Integer goodsPrice;
    private String traceId;

    public PaymentRequestEventDto(OrderRequestEventDto orderRequestEventDto, Long orderId) {
        this.userId = orderRequestEventDto.getUserId();
        this.orderId = orderId;
        this.goodsId = orderRequestEventDto.getGoodsId();
        this.quantity = orderRequestEventDto.getQuantity();
        this.goodsPrice = orderRequestEventDto.getGoodsPrice();
        traceId = orderRequestEventDto.getTraceId();
    }
}
