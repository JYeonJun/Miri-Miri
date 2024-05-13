package com.miri.goodsservice.service.kafka;

import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
import com.miri.coremodule.vo.KafkaVO;
import com.miri.goodsservice.service.goods.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaReceiver {

    private final GoodsService goodsService;

    public KafkaReceiver(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @KafkaListener(topics = KafkaVO.STOCK_ROLLBACK_TOPIC, containerFactory = "kafkaContainerFactory")
    public void receiveRollbackEvent(StockRollbackEventDto stockRollbackEventDto) {
        log.debug("재고 증가 이벤트 소비, traceId={}, goodsId={}, quantity={}", stockRollbackEventDto.getTraceId(),
                stockRollbackEventDto.getGoodsId(), stockRollbackEventDto.getQuantity());
        goodsService.increaseOrderGoodsStock(stockRollbackEventDto.getGoodsId(), stockRollbackEventDto.getQuantity());
    }
}