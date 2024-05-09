package com.miri.orderservice.service.kafka;

import com.miri.coremodule.dto.kafka.OrderRequestEventDto;
import com.miri.coremodule.dto.kafka.OrderUpdateEventDto;
import com.miri.coremodule.vo.KafkaVO;
import com.miri.orderservice.service.order.OrderInternalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaReceiver {

    private final OrderInternalService orderInternalService;

    public KafkaReceiver(OrderInternalService orderInternalService) {
        this.orderInternalService = orderInternalService;
    }

    @KafkaListener(topics = KafkaVO.ORDER_REQUEST_TOPIC, containerFactory = "kafkaOrderRequestContainerFactory")
    public void receiveOrderRequestEvent(OrderRequestEventDto orderRequestEventDto) {
        log.info("traceId={}, 상품 주문 이벤트 소비", orderRequestEventDto.getTraceId());
        orderInternalService.processOrder(orderRequestEventDto);
    }

    @KafkaListener(topics = KafkaVO.ORDER_UPDATE_TOPIC, containerFactory = "kafkaOrderUpdateContainerFactory")
    public void receiveOrderUpdateEvent(OrderUpdateEventDto orderUpdateEventDto) {
        log.info("traceId={}, 주문 상태 변경 이벤트 소비, userId={}, orderId={}",
                orderUpdateEventDto.getTraceId(), orderUpdateEventDto.getUserId(), orderUpdateEventDto.getOrderId());
        try {
            orderInternalService.updateOrderStatusOnFailure(orderUpdateEventDto.getOrderId());
        } catch (Exception e) {
            log.error("주문 상태 변경 이벤트 장애 발생!!");
        }
    }
}