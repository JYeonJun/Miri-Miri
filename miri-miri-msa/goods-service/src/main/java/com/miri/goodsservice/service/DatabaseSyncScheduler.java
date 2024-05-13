package com.miri.goodsservice.service;

import com.miri.goodsservice.service.goods.GoodsInternalService;
import com.miri.goodsservice.service.redis.RedisStockService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DatabaseSyncScheduler {
    private final RedisStockService redisStockService;
    private final GoodsInternalService goodsInternalService;

    public DatabaseSyncScheduler(RedisStockService redisStockService, GoodsInternalService goodsInternalService) {
        this.redisStockService = redisStockService;
        this.goodsInternalService = goodsInternalService;
    }
    @Scheduled(fixedRate = 1800000) // 30분마다 수행
    public void updateStocksInDatabase() {
        log.info("변경된 레디스 상품 재고 정보를 데이터베이스로 동기화 시작");

        try {
            Map<Long, Integer> dirtyGoodsStock = redisStockService.getDirtyGoodsStockWithLua();
            goodsInternalService.updateStocksInDatabase(dirtyGoodsStock);
        } catch (Exception e) {
            log.error("데이터베이스 업데이트 중 오류 발생", e);
        }
        redisStockService.clearDirtyGoodsListWithLua();
        log.info("변경된 레디스 상품 재고 정보를 데이터베이스로 동기화 완료");
    }
}

