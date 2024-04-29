package com.miri.coremodule.dto.wishlist;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

public class FeignWishListReqDto {

    @Data
    @AllArgsConstructor
    public static class WishListOrderedReqDto {
        private Long userId;
        private List<Long> wishListIds;
    }
}
