package com.miri.paymentservice.event;

import com.miri.coremodule.dto.kafka.PaymentRequestEventDto;
import org.springframework.context.ApplicationEvent;

public class PaymentAbortedEvent extends ApplicationEvent {

    private final PaymentRequestEventDto paymentRequestEventDto;

    public PaymentAbortedEvent(Object source, PaymentRequestEventDto paymentRequestEventDto) {
        super(source);
        this.paymentRequestEventDto = paymentRequestEventDto;
    }

    public PaymentRequestEventDto getPaymentRequestEventDto() {
        return paymentRequestEventDto;
    }
}
