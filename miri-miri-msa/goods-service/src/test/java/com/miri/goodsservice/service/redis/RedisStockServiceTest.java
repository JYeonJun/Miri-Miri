package com.miri.goodsservice.service.redis;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class RedisStockServiceTest {

    @Autowired
    private RedisStockService redisStockService;

    private Long goodsId;
    private final int initialStock = 100;
    private final int timeout = 5;
    private final TimeUnit unit = TimeUnit.MINUTES;

    @BeforeEach
    void setUp() {
        goodsId = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE); // 임의의 goodsId 생성
        redisStockService.setGoodsStock(goodsId, initialStock, timeout, unit);
    }

    @Test
    void givenInitialStock_whenGetGoodsStock_thenEqualsInitialStock() {
        Integer goodsStock = redisStockService.getGoodsStock(goodsId);
        Assertions.assertThat(goodsStock).isEqualTo(initialStock);
    }

    @Test
    void givenIncreasedStock_whenGetGoodsStock_thenStockIncreased() {
        redisStockService.increaseGoodsStock(goodsId, 10);
        Integer increasedStock = redisStockService.getGoodsStock(goodsId);
        Assertions.assertThat(increasedStock).isEqualTo(initialStock + 10);
    }

    @Test
    void givenDecreasedStock_whenGetGoodsStock_thenStockDecreased() {
        redisStockService.decreaseGoodsStock(goodsId, 10);
        Integer decreasedStock = redisStockService.getGoodsStock(goodsId);
        Assertions.assertThat(decreasedStock).isEqualTo(initialStock - 10);
    }
}
