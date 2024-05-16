package com.miri.goodsservice.service.goods;

import com.miri.goodsservice.domain.goods.Goods;
import com.miri.goodsservice.domain.goods.GoodsRepository;
import com.miri.goodsservice.service.redis.RedisStockService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class GoodsInternalServiceImpl implements GoodsInternalService{

    private final GoodsRepository goodsRepository;
    private final RedisStockService redisStockService;

    public GoodsInternalServiceImpl(GoodsRepository goodsRepository, RedisStockService redisStockService) {
        this.goodsRepository = goodsRepository;
        this.redisStockService = redisStockService;
    }

    @Override
    @Transactional
    public void updateStocksInDatabase(Map<Long, Integer> allStocks) {
        Set<Long> goodsIds = allStocks.keySet();
        List<Goods> goodsList = goodsRepository.findAllById(goodsIds);
        goodsList.forEach(goods -> goods.updateStock(allStocks.get(goods.getId())));
    }

    @Override
    public void increaseOrderGoodsStockBatch(Map<Long, Integer> goodsIdToQuantityMap) {
        redisStockService.increaseGoodsStockWithLuaBatch(goodsIdToQuantityMap);
    }
}
