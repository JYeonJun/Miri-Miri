package com.miri.coremodule.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockRollbackEventDto {
    private Long goodsId;
    private Integer quantity;
    private String traceId;

    public static StockRollbackEventDto fromPaymentRequest(PaymentRequestEventDto paymentRequestEventDto) {
        return new StockRollbackEventDto(paymentRequestEventDto.getGoodsId(), paymentRequestEventDto.getQuantity(),
                paymentRequestEventDto.getTraceId());
    }

    public static StockRollbackEventDto fromOrderRequest(OrderRequestEventDto orderRequestEventDto) {
        return new StockRollbackEventDto(orderRequestEventDto.getGoodsId(), orderRequestEventDto.getQuantity(),
                orderRequestEventDto.getTraceId());
    }
}
