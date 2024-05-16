package com.miri.paymentservice.event;

import com.miri.coremodule.dto.kafka.OrderUpdateEventDto;
import com.miri.coremodule.dto.kafka.PaymentRequestEventDto;
import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
import com.miri.coremodule.vo.KafkaVO;
import com.miri.paymentservice.service.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class PaymentAbortedEventListener {

    private final KafkaSender kafkaSender;

    public PaymentAbortedEventListener(KafkaSender kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void onPaymentAborted(PaymentAbortedEvent event) {
        PaymentRequestEventDto paymentRequestEventDto = event.getPaymentRequestEventDto();
        log.info("traceId={}, 카프카 상품 주문 실패 이벤트 발행",paymentRequestEventDto.getTraceId());
        kafkaSender.sendOrderUpdateRequestEvent(KafkaVO.ORDER_UPDATE_TOPIC,
                OrderUpdateEventDto.fromPaymentRequest(paymentRequestEventDto));
        kafkaSender.sendRollbackRequestEvent(KafkaVO.STOCK_ROLLBACK_TOPIC,
                StockRollbackEventDto.fromPaymentRequest(paymentRequestEventDto));
    }
}
