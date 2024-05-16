package com.miri.paymentservice.service.kafka;

import com.miri.coremodule.dto.kafka.CancelOrderEventDto;
import com.miri.coremodule.dto.kafka.PaymentRequestEventDto;
import com.miri.coremodule.vo.KafkaVO;
import com.miri.paymentservice.service.payment.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaReceiver {

    private final PaymentService paymentService;

    public KafkaReceiver(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = KafkaVO.PAYMENT_REQUEST_TOPIC, containerFactory = "kafkaPaymentRequestContainerFactory")
    public void receivePaymentRequestEvent(PaymentRequestEventDto paymentRequestEventDto) {
        log.debug("traceId={}, 카프카 결제 이벤트 소비", paymentRequestEventDto.getTraceId());
        paymentService.processPaymentEvent(paymentRequestEventDto);
    }

    @KafkaListener(topics = KafkaVO.CANCEL_ORDER_TOPIC, containerFactory = "kafkaCancelOrderContainerFactory")
    public void receiveCancelOrderEvent(CancelOrderEventDto eventDto) {
        log.debug("주문 취소 이벤트 소비, orderId={}, goodsId={}, quantity={}", eventDto.getOrderId(),
                eventDto.getGoodsId(), eventDto.getQuantity());
        paymentService.updatePaymentStatusOnCancelOrder(eventDto.getOrderId());
    }
}
