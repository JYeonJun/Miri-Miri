package com.miri.coremodule.dto.goods;

import lombok.Data;

public class FeignGoodsReqDto {

    @Data
    public static class GoodsStockDecreaseReqDto {
        private Long goodsId;
        private int quantity;
    }
}
