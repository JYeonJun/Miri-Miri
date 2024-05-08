package com.miri.orderservice.domain.shipping;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ShippingStatus {
    PENDING("배송 대기"),
    IN_TRANSIT("배송 중"),
    DELIVERED("배송 완료"),
    CANCELED("배송 취소");
    private final String value;
}
