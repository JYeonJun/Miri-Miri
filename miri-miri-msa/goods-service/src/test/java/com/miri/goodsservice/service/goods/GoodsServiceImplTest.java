package com.miri.goodsservice.service.goods;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
import com.miri.coremodule.handler.ex.OrderNotAvailableException;
import com.miri.coremodule.handler.ex.StockUnavailableException;
import com.miri.goodsservice.domain.goods.Goods;
import com.miri.goodsservice.domain.goods.GoodsCategory;
import com.miri.goodsservice.domain.goods.GoodsRepository;
import com.miri.goodsservice.dto.goods.RequestGoodsDto.OrderGoodsReqDto;
import com.miri.goodsservice.facade.RedissonLockStockFacade;
import com.miri.goodsservice.service.redis.RedisStockService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
class GoodsServiceImplTest {

    @Autowired
    private RedissonLockStockFacade redissonLockStockFacade;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private RedisStockService redisStockService;

    @Autowired
    private GoodsService goodsService;

/*    @BeforeEach
    public void insert() {

        List<Goods> goodsList = new ArrayList<>();

        goodsList.add(Goods.builder()
                .sellerId(1L)
                .goodsName("testGoods")
                .goodsDescription("testDescription")
                .goodsPrice(10000)
                .stockQuantity(200)
                .category(GoodsCategory.FASHION)
                .reservationStartTime(LocalDateTime.now().minusMinutes(10))
                .build());

        goodsList.add(Goods.builder()
                .sellerId(2L)
                .goodsName("testGoods")
                .goodsDescription("testDescription")
                .goodsPrice(10000)
                .stockQuantity(200)
                .category(GoodsCategory.ETC)
                .reservationStartTime(LocalDateTime.now().plusDays(10))
                .build());

        redisStockService.setGoodsStock(2L, 200, 10, TimeUnit.MINUTES);
        goodsRepository.saveAllAndFlush(goodsList);
    }

    @AfterEach
    public void delete() {
        redisStockService.deleteAllGoodsStock();
    }*/

  /*  @Test
    @DisplayName("재고 조회 레디스 락 테스트")
    public void getGoodsStockQuantity_test() throws InterruptedException {

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockStockFacade.getGoodsStockQuantity(1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
    }

    @Test
    @DisplayName("100명의 사용자가 재고 감소 요청")
    public void processOrderForGoods_success() throws InterruptedException {

        OrderGoodsReqDto orderGoodsReqDto = new OrderGoodsReqDto();
        orderGoodsReqDto.setGoodsId(1L);
        orderGoodsReqDto.setQuantity(1);
        orderGoodsReqDto.setAddress("경상도");
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockStockFacade.processOrderForGoods(1L, orderGoodsReqDto);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Goods findGoods = goodsRepository.findById(1L).orElseThrow();

        // 100 - (100 * 1) = 0
        Assertions.assertThat(findGoods.getStockQuantity()).isEqualTo(100);
    }

    @Test
    @DisplayName("재고 부족 예외 테스트")
    public void processOrderForGoods_StockUnavailableException() {
        assertThatThrownBy(() -> {
            redissonLockStockFacade.processOrderForGoods(1L, 2L, 201);
        })
                .isInstanceOf(StockUnavailableException.class)
                .hasMessageContaining("재고가 부족합니다.");
    }

    @Test
    @DisplayName("예약 구매 불가능 예외 테스트")
    public void processOrderForGoods_OrderNotAvailableException() {
        assertThatThrownBy(() -> {
            redissonLockStockFacade.processOrderForGoods(1L, 2L, 100);
        })
                .isInstanceOf(OrderNotAvailableException.class)
                .hasMessageContaining("주문 가능 시간이 아닙니다.");
    }

    @Test
    @DisplayName("100명의 사용자가 재고 증가 요청")
    public void increaseOrderGoodsStock_success() throws InterruptedException {

        long goodsId = 1L;

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockStockFacade.increaseGoodsStock(new StockRollbackEventDto(goodsId, 1));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Goods findGoods = goodsRepository.findById(goodsId).orElseThrow();

        // 100 - (100 * 1) = 0
        Assertions.assertThat(findGoods.getStockQuantity()).isEqualTo(300);
    }*/
}