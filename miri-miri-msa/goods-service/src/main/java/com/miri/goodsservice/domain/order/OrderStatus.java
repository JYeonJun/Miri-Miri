package com.miri.goodsservice.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    PENDING("대기"),
    IN_TRANSIT("배송 중"),
    DELIVERED("배송 완료"),
    CANCELED("주문 취소"),
    RETURN_IN_PROGRESS("반품 중"),
    RETURN_COMPLETED("반품 완료");
    private final String value;
}
