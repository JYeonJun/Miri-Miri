package com.miri.coremodule.dto.goods;

import lombok.AllArgsConstructor;
import lombok.Data;

public class FeignGoodsReqDto {
    @Data
    @AllArgsConstructor
    public static class GoodsStockIncreaseReqDto {
        private Long goodsId;
        private int quantity;
    }
}
