package com.miri.orderservice.dto.wishlist;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class RequestWishListDto {

    @Data
    public static class AddToCartReqDto {

        @NotNull(message = "상품 ID는 필수입니다.")
        private Long goodsId;

        @NotNull(message = "상품 수량은 필수입니다.")
        @Min(value = 1, message = "상품 수량은 최소 1개 이상이어야 합니다.")
        private Integer goodsQuantity;
    }

    @Data
    public static class WishListUpdateReqDto {

        @NotNull(message = "상품 수량은 필수입니다.")
        @Min(value = 1, message = "상품 수량은 최소 1개 이상이어야 합니다.")
        private Integer goodsQuantity;
    }
}
