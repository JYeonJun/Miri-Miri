package com.miri.goodsservice.facade;

import com.miri.coremodule.handler.ex.CustomApiException;
import com.miri.goodsservice.service.goods.GoodsService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedissonLockStockFacade {

    private static final String GOODS_LOCK_PREFIX = "goods-lock-";

    private final RedissonClient redissonClient;
    private final GoodsService goodsService;

    public RedissonLockStockFacade(RedissonClient redissonClient, GoodsService goodsService) {
        this.redissonClient = redissonClient;
        this.goodsService = goodsService;
    }

    public void processOrderForGoods(Long userId, Long goodsId, Integer quantity) {
        RLock lock = redissonClient.getLock(GOODS_LOCK_PREFIX + goodsId);

        try {
            if (!lock.tryLock(60, 10, TimeUnit.SECONDS)) {
                log.error("상품 재고 감소 LOCK 획득 실패");
                throw new CustomApiException("수요가 많아 주문 요청에 실패하였습니다.");
            }

            goodsService.processOrderForGoods(userId, goodsId, quantity);
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("스레드 인터럽트 에러, userId={}, goodsId={}", userId, goodsId, e);
            throw new CustomApiException("주문 처리 중 에러가 발생했습니다.");
        } finally {
            lock.unlock();
        }
    }
}
