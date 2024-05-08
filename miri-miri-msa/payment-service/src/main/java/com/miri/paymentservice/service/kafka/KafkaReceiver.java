package com.miri.paymentservice.service.kafka;

import com.miri.coremodule.dto.kafka.OrderUpdateEventDto;
import com.miri.coremodule.dto.kafka.PaymentRequestEventDto;
import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
import com.miri.coremodule.vo.KafkaVO;
import com.miri.paymentservice.service.payment.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaReceiver {

    private final PaymentService paymentService;
    private final KafkaSender kafkaSender;

    public KafkaReceiver(PaymentService paymentService, KafkaSender kafkaSender) {
        this.paymentService = paymentService;
        this.kafkaSender = kafkaSender;
    }

    @KafkaListener(topics = KafkaVO.PAYMENT_REQUEST_TOPIC, containerFactory = "kafkaContainerFactory")
    public void receivePaymentRequestEvent(PaymentRequestEventDto paymentRequestEventDto) {
        try {
            paymentService.processPaymentEvent(paymentRequestEventDto);
        } catch (Exception e) {
            log.error("[상품 주문]: 결제 처리 중 장애 발생 error={}", e.getMessage());
            handlePaymentFailure(paymentRequestEventDto);
        }
    }

    private void handlePaymentFailure(PaymentRequestEventDto paymentRequestEventDto) {
        kafkaSender.sendOrderUpdateRequestEvent(KafkaVO.ORDER_UPDATE_TOPIC, new OrderUpdateEventDto(paymentRequestEventDto));
        kafkaSender.sendRollbackRequestEvent(KafkaVO.STOCK_ROLLBACK_TOPIC, new StockRollbackEventDto(paymentRequestEventDto));
    }
}
