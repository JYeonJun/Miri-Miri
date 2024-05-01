package com.miri.coremodule.dto.wishlist;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WishListRespDto {
        Page<GoodsInWishListRespDto> goods;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GoodsInWishListRespDto {
        private Long wishListId;
        private Long goodsId;
        private String goodsName;
        private int unitPrice;
        private int subTotalPrice;
        private int stockQuantity; // 상품의 남은 재고 수
        private String category;
        private int wishGoodsQuantity; // 장바구니에 담은 상품 수량
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime addToWishListDate; // 장바구니에 추가한 시간
    }
}
