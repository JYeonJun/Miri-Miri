package com.miri.coremodule.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

public class FeignGoodsRespDto {

    @Data
    @AllArgsConstructor
    public static class GoodsStockRespDto {
        private Long goodsId;
        private int remainStockQuantity;
    }

    @Data
    @AllArgsConstructor
    public static class OrderedGoodsDetailRespDto {
        private Long goodsId;
        private int goodsPrice;
        private String category;
    }

    @Data
    public static class RegisterGoodsListRespDto {
        Page<RegisterGoodsDto> registerGoods;
    }

    @Data
    public static class RegisterGoodsDto {
        private Long goodsId;
        private String goodsName;
        private int goodsPrice;
        private int stockQuantity;
        private String category;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime registerDate;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime lastModifiedDate;
    }

    @Data
    public static class WishListRespDto {
        Page<GoodsInWishListRespDto> goods;
    }

    @Data
    public static class GoodsInWishListRespDto {
        private Long wishListId;
        private Long goodsId;
        private String goodsName;
        private int unitPrice;
        private long subTotalPrice;
        private int stockQuantity; // 상품의 남은 재고 수
        private String category;
        private int wishGoodsQuantity; // 장바구니에 담은 상품 수량
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate addToWishListDate; // 장바구니에 추가한 날짜
    }
}
