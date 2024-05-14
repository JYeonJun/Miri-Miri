package com.miri.goodsservice.service.goods;

import java.util.Map;

public interface GoodsInternalService {

    void updateStocksInDatabase(Map<Long, Integer> allStocks);

    void increaseOrderGoodsStockBatch(Map<Long, Integer> goodsIdToQuantityMap);
}
