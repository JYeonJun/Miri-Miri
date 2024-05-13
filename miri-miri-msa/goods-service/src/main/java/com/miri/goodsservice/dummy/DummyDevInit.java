package com.miri.goodsservice.dummy;

import com.miri.goodsservice.domain.goods.Goods;
import com.miri.goodsservice.domain.goods.GoodsRepository;
import com.miri.goodsservice.service.redis.RedisStockService;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
public class DummyDevInit extends DummyObject {

    private final GoodsRepository goodsRepository;
    private final RedisStockService redisStockService;

    public DummyDevInit(GoodsRepository goodsRepository, RedisStockService redisStockService) {
        this.goodsRepository = goodsRepository;
        this.redisStockService = redisStockService;
    }

    @Bean
    @Profile("dev")
    CommandLineRunner devInit() {
        return (args) -> {
            List<Goods> allGoods = goodsRepository.findAll();

            for (Goods goods : allGoods) {
                redisStockService.setGoodsStock(goods.getId(), goods.getStockQuantity());
            }
        };
    }
}
