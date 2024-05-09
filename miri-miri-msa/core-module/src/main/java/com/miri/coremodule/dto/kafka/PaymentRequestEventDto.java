package com.miri.coremodule.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestEventDto {
    private Long userId;
    private Long orderId;
    private Long goodsId;
    private Integer quantity;
    private Integer goodsPrice;
    private String traceId;

    public static PaymentRequestEventDto fromOrderRequest(OrderRequestEventDto orderRequestEventDto, Long orderId) {
        return new PaymentRequestEventDto(orderRequestEventDto.getUserId(), orderId, orderRequestEventDto.getGoodsId(),
                orderRequestEventDto.getQuantity(), orderRequestEventDto.getGoodsPrice(),
                orderRequestEventDto.getTraceId());
    }
}
