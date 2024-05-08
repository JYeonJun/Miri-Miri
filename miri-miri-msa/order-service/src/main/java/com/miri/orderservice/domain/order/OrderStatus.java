package com.miri.orderservice.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    COMPLETED("주문 완료"),
    CANCELED("주문 취소");
    private final String value;
}
