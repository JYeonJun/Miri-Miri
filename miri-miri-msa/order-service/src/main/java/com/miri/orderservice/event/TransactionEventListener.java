package com.miri.orderservice.event;

import com.miri.coremodule.dto.kafka.CancelOrderEventDto;
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
public class TransactionEventListener {

    private final KafkaSender kafkaSender;

    public TransactionEventListener(KafkaSender kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderSuccess(ProcessOrderEvent event) {
        try {
            OrderRequestEventDto orderRequestEventDto = event.getOrderRequestEventDto();
            log.info("traceId={}, 카프카 결제 요청 이벤트 발행", orderRequestEventDto.getTraceId());
            kafkaSender.sendPaymentRequestEvent(KafkaVO.PAYMENT_REQUEST_TOPIC,
                    PaymentRequestEventDto.fromOrderRequest(orderRequestEventDto, event.getOrderId()));
        } catch (Exception e) {
            log.error("결제 요청 이벤트 발행 실패, 재시도 필요", e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderStatusToCanceledSuccess(CancelOrderEvent event) {
        try {
            log.info("카프카 주문 취소 이벤트 발행");
            kafkaSender.sendCancelOrderEvent(KafkaVO.CANCEL_ORDER_TOPIC,
                    new CancelOrderEventDto(event.getOrderId(), event.getGoodsId(), event.getQuantity()));
        } catch (Exception e) {
            log.error("주문 취소 이벤트 발행 실패, 재시도 필요", e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onReturnSuccess(ReturnEvent event) {
        try {
            log.info("카프카 반품 완료 이벤트 발행");
            kafkaSender.sendRollbackRequestEvent(KafkaVO.STOCK_ROLLBACK_TOPIC,
                    new StockRollbackEventDto(event.getGoodsId(), event.getQuantity(), null));
        } catch (Exception e) {
            log.error("반품 완료 이벤트 발행 실패, 재시도 필요", e);
        }
    }
}
