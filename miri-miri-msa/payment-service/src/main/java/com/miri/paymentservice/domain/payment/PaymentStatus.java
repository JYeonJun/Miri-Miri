package com.miri.paymentservice.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentStatus {
    PAYMENT_SCREEN_ENTERED("결제 화면"),
    PAYMENT_IN_PROGRESS("결제 중"),
    PAYMENT_COMPLETED("결제 완료"),
    PAYMENT_ABORTED("결제 이탈");
    private final String value;
}