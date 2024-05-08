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
    private Integer totalPrice;
    private String traceId;

    public PaymentRequestEventDto(OrderRequestEventDto orderRequestEventDto, Long orderId) {
        this.userId = orderRequestEventDto.getUserId();
        this.orderId = orderId;
        this.totalPrice = orderRequestEventDto.getGoodsPrice() * orderRequestEventDto.getQuantity();
        traceId = orderRequestEventDto.getTraceId();
    }
}
