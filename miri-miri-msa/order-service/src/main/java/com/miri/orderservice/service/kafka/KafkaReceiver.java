package com.miri.orderservice.service.kafka;

import com.miri.coremodule.dto.kafka.OrderRequestEventDto;
import com.miri.coremodule.dto.kafka.OrderUpdateEventDto;
import com.miri.coremodule.dto.kafka.PaymentRequestEventDto;
import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
import com.miri.coremodule.vo.KafkaVO;
import com.miri.orderservice.domain.order.Order;
import com.miri.orderservice.domain.order.OrderDetail;
import com.miri.orderservice.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaReceiver {

    private final OrderService orderService;
    private final KafkaSender kafkaSender;

    public KafkaReceiver(OrderService orderService, KafkaSender kafkaSender) {
        this.orderService = orderService;
        this.kafkaSender = kafkaSender;
    }

    @KafkaListener(topics = KafkaVO.ORDER_REQUEST_TOPIC, containerFactory = "kafkaOrderRequestContainerFactory")
    public void receiveOrderRequestEvent(OrderRequestEventDto orderRequestEventDto) {
        log.info("[상품 주문]: Order Consumer 동작");
        try {
            orderService.processOrder(orderRequestEventDto);
        } catch (Exception e) {
            log.error("[상품 주문]: 주문 처리 중 에러 발생, traceId={}, userId={}", orderRequestEventDto.getTraceId(),
                    orderRequestEventDto.getUserId(), e);
            // 주문 처리 중 예외가 발생하는 경우, stock-rollback 토픽에 예외를 발생하여 롤백 처리
            kafkaSender.sendRollbackRequestEvent(KafkaVO.STOCK_ROLLBACK_TOPIC,
                    new StockRollbackEventDto(orderRequestEventDto));
        }
    }

    @KafkaListener(topics = KafkaVO.ORDER_UPDATE_TOPIC, containerFactory = "kafkaOrderUpdateContainerFactory")
    public void receiveOrderUpdateEvent(OrderUpdateEventDto orderUpdateEventDto) {
        log.info("[상품 주문 실패]: 주문 상태 변경 이벤트 소비, traceId={}, userId={}, orderId={}",
                orderUpdateEventDto.getTraceId(), orderUpdateEventDto.getUserId(), orderUpdateEventDto.getOrderId());
        try {
            orderService.updateOrderStatusOnFailure(orderUpdateEventDto.getOrderId());
        } catch (Exception e) {
            log.error("주문 상태 변경 에러");
        }
    }
}
