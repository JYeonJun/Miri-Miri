package com.miri.orderservice.domain.returnrequest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReturnStatus {

    RETURN_IN_PROGRESS("반품 처리 중"),
    RETURN_COMPLETED("반품 완료");
    private final String value;
}
