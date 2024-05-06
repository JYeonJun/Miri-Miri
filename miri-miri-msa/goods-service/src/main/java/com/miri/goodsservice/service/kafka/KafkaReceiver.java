package com.miri.goodsservice.service.kafka;

import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
import com.miri.coremodule.vo.KafkaVO;
import com.miri.goodsservice.facade.RedissonLockStockFacade;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaReceiver {

    private final RedissonLockStockFacade redissonLockStockFacade;

    public KafkaReceiver(RedissonLockStockFacade redissonLockStockFacade) {
        this.redissonLockStockFacade = redissonLockStockFacade;
    }

    @KafkaListener(topics = KafkaVO.STOCK_ROLLBACK_TOPIC, containerFactory = "kafkaContainerFactory")
    public void receiveRollbackEvent(StockRollbackEventDto stockRollbackEventDto) {
        redissonLockStockFacade.increaseGoodsStock(stockRollbackEventDto);
    }
}