package com.miri.orderservice.event;

import com.miri.coremodule.dto.kafka.OrderRequestEventDto;
import com.miri.coremodule.dto.kafka.PaymentRequestEventDto;
import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
import com.miri.coremodule.vo.KafkaVO;
import com.miri.orderservice.service.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class OrderToPaymentEventListener {

    private final KafkaSender kafkaSender;

    public OrderToPaymentEventListener(KafkaSender kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderSuccess(OrderToPaymentEvent event) {
        OrderRequestEventDto orderRequestEventDto = event.getOrderRequestEventDto();
        log.info("traceId={}, 카프카 결제 요청 이벤트 발행", orderRequestEventDto.getTraceId());
        kafkaSender.sendPaymentRequestEvent(KafkaVO.PAYMENT_REQUEST_TOPIC,
                PaymentRequestEventDto.fromOrderRequest(orderRequestEventDto, event.getOrderId()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onOrderFailure(OrderToPaymentEvent orderToPaymentEvent) {
        OrderRequestEventDto orderRequestEventDto = orderToPaymentEvent.getOrderRequestEventDto();
        log.info("traceId={}, 주문 실패 이벤트 발행", orderRequestEventDto.getTraceId());
        kafkaSender.sendRollbackRequestEvent(KafkaVO.STOCK_ROLLBACK_TOPIC,
                StockRollbackEventDto.fromOrderRequest(orderRequestEventDto));
    }
}
