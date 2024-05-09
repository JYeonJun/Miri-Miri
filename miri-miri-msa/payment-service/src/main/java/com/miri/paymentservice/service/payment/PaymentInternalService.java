package com.miri.paymentservice.service.payment;

import com.miri.coremodule.dto.kafka.PaymentRequestEventDto;

public interface PaymentInternalService {

    void processPaymentEvent(PaymentRequestEventDto paymentRequestEventDto);
}
