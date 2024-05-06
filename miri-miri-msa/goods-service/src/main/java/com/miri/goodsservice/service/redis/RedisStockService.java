package com.miri.goodsservice.service.redis;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisStockService {
    private static final String GOODS_STOCK_KEY_NAMESPACE = "goods_stock:";

    private RedisTemplate<String, Integer> redisTemplate;
    private ValueOperations<String, Integer> valueOperations;

    public RedisStockService(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    // 상품 재고 저장
    public void setGoodsStock(Long goodsId, int quantity, long timeout, TimeUnit unit) {
        valueOperations.set(GOODS_STOCK_KEY_NAMESPACE + goodsId, quantity, timeout, unit);
    }

    // 상품 재고 조회
    public Integer getGoodsStock(Long goodsId) {
        return valueOperations.get(GOODS_STOCK_KEY_NAMESPACE + goodsId);
    }

    // 상품 재고 증가
    public void increaseGoodsStock(Long goodsId, int quantity) {
        valueOperations.increment(GOODS_STOCK_KEY_NAMESPACE + goodsId, quantity);
    }

    // 상품 재고 감소
    public void decreaseGoodsStock(Long goodsId, int quantity) {
        valueOperations.increment(GOODS_STOCK_KEY_NAMESPACE + goodsId, -quantity);
    }

    // 모든 상품 재고 데이터 삭제
    public void deleteAllGoodsStock() {
        // 상품 재고에 해당하는 모든 키를 찾음
        Set<String> keys = redisTemplate.keys(GOODS_STOCK_KEY_NAMESPACE + "*");
        if (keys != null && !keys.isEmpty()) {
            // 찾은 모든 키를 삭제
            redisTemplate.delete(keys);
            log.info("모든 상품 재고 데이터가 삭제되었습니다.");
        } else {
            log.info("삭제할 상품 재고 데이터가 없습니다.");
        }
    }
}
