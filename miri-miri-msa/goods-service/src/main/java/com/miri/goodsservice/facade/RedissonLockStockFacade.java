package com.miri.goodsservice.facade;

import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
import com.miri.coremodule.handler.ex.CustomApiException;
import com.miri.goodsservice.dto.goods.RequestGoodsDto.OrderGoodsReqDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsStockQuantityRespDto;
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

    private interface LockCallback<T> {
        T doInLock() throws InterruptedException;
    }

    private <T> T executeWithLock(Long goodsId, LockCallback<T> callback) {
        RLock lock = redissonClient.getLock(GOODS_LOCK_PREFIX + goodsId);
        try {
            if (!lock.tryLock(5, 1, TimeUnit.SECONDS)) {
                log.error("상품 재고 관리 LOCK 획득 실패");
                throw new CustomApiException("요청이 많아 처리에 실패하였습니다.");
            }

            log.debug("락 획득 성공");
            return callback.doInLock();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("처리 중 에러가 발생했습니다.");
            throw new CustomApiException("상품 재고 처리 중 에러가 발생했습니다.");
        } finally {
            lock.unlock();
            log.debug("락 반납");
        }
    }

    public GoodsStockQuantityRespDto getGoodsStockQuantity(Long goodsId) {
        return executeWithLock(goodsId, () -> {
            Integer goodsStock = goodsService.getGoodsStockQuantity(goodsId);
            return new GoodsStockQuantityRespDto(goodsStock);
        });
    }

    public void processOrderForGoods(Long userId, OrderGoodsReqDto reqDto) {
        executeWithLock(reqDto.getGoodsId(), () -> {
//            goodsService.processOrderForGoods(userId, reqDto);
            return null;
        });
    }

    public void increaseGoodsStock(StockRollbackEventDto reqDto) {
        executeWithLock(reqDto.getGoodsId(), () -> {
            goodsService.increaseOrderGoodsStock(reqDto.getGoodsId(), reqDto.getQuantity());
            return null;
        });
    }
}
