package com.miri.paymentservice.service.kafka;

import com.miri.coremodule.dto.kafka.PaymentRequestEventDto;
import com.miri.coremodule.vo.KafkaVO;
import com.miri.paymentservice.service.payment.PaymentInternalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaReceiver {

    private final PaymentInternalService paymentInternalService;

    public KafkaReceiver(PaymentInternalService paymentInternalService) {
        this.paymentInternalService = paymentInternalService;
    }

    @KafkaListener(topics = KafkaVO.PAYMENT_REQUEST_TOPIC, containerFactory = "kafkaContainerFactory")
    public void receivePaymentRequestEvent(PaymentRequestEventDto paymentRequestEventDto) {
        log.debug("traceId={}, 카프카 결제 이벤트 소비", paymentRequestEventDto.getTraceId());
        paymentInternalService.processPaymentEvent(paymentRequestEventDto);
    }
}
