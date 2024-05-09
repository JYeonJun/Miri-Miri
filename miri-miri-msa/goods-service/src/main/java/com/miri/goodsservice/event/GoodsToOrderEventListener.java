package com.miri.goodsservice.event;

import com.miri.coremodule.dto.kafka.OrderRequestEventDto;
import com.miri.coremodule.vo.KafkaVO;
import com.miri.goodsservice.dto.goods.RequestGoodsDto.OrderGoodsReqDto;
import com.miri.goodsservice.service.kafka.KafkaSender;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class GoodsToOrderEventListener {

    private final KafkaSender kafkaSender;

    public GoodsToOrderEventListener(KafkaSender kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderSuccess(GoodsToOrderEvent event) {
        String traceId = UUID.randomUUID().toString();
        log.info("traceId={}, 카프카 결제 요청 이벤트 발행", traceId);
        kafkaSender.sendOrderRequestEvent(KafkaVO.ORDER_REQUEST_TOPIC,
                createOrderRequestEventDto(event, traceId));
    }

    private OrderRequestEventDto createOrderRequestEventDto(GoodsToOrderEvent event, String traceId) {
        OrderGoodsReqDto dto = event.getOrderGoodsReqDto();
        return new OrderRequestEventDto(event.getUserId(), dto.getGoodsId(), dto.getQuantity(),
                event.getGoodsPrice(), dto.getAddress(), traceId);
    }
}
