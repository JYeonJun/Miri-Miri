package com.miri.coremodule.dto.wishlist;

import lombok.Builder;
import lombok.Data;

public class FeignWishListRespDto {

    @Data
    @Builder
    public static class WishListOrderedRespDto {
        private Long wishListId;
        private int orderQuantity;
        private Long goodsId;
        private String goodsName;
        private int unitPrice;
    }
}
