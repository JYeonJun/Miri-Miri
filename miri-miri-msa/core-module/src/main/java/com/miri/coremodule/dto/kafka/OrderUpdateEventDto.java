package com.miri.coremodule.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateEventDto {
    private Long userId;
    private Long orderId;
    private String traceId;

    public static OrderUpdateEventDto fromPaymentRequest(PaymentRequestEventDto paymentRequestEventDto) {
        return new OrderUpdateEventDto(paymentRequestEventDto.getUserId(), paymentRequestEventDto.getOrderId(),
                paymentRequestEventDto.getTraceId());
    }
}
