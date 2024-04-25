package com.miri.goodsservice.dto.wishlist;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miri.goodsservice.domain.goods.Goods;
import com.miri.goodsservice.domain.wishlist.WishList;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

public class ResponseWishListDto {

    @Data
    public static class AddToWishListRespDto {

        private Long wishListId;
        private String goodsName; // 장바구니에 담은 상품명
        private int goodsQuantity; // 장바구니에 담은 상품 수량
        private int unitPrice; // 장바구니에 담은 상품 가격
        private int subTotalPrice; // 장바구니에 담은 현재 상품의 총 가격

        public AddToWishListRespDto(Goods goods, WishList wishList) {
            this.wishListId = wishList.getId();
            this.goodsName = goods.getGoodsName();
            this.goodsQuantity = wishList.getQuantity();
            this.unitPrice = goods.getGoodsPrice();
            this.subTotalPrice = unitPrice * goodsQuantity;
        }
    }

    @Data
    @AllArgsConstructor
    public static class WishListUpdateRespDto {
        private Long wishListId;
        private Long goodsId;
        private int wishGoodsQuantity; // 장바구니에 담은 상품 수량
    }

    @Data
    @AllArgsConstructor
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

        public GoodsInWishListRespDto(WishList wishList, Goods goods) {
            this.wishListId = wishList.getId();
            this.goodsId = goods.getId();
            this.goodsName = goods.getGoodsName();
            this.unitPrice = goods.getGoodsPrice();
            this.subTotalPrice = (long) wishList.getQuantity() * goods.getGoodsPrice();
            this.stockQuantity = goods.getStockQuantity();
            this.category = goods.getCategory().getValue();
            this.wishGoodsQuantity = wishList.getQuantity();
            this.addToWishListDate = wishList.getCreatedDate().toLocalDate();
        }
    }
}
