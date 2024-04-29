package com.miri.coremodule.dto.goods;

import lombok.AllArgsConstructor;
import lombok.Data;

public class FeignGoodsRespDto {

    @Data
    @AllArgsConstructor
    public static class GoodsStockDecreaseRespDto {
        private Long goodsId;
        private int remainStockQuantity;
    }
}
