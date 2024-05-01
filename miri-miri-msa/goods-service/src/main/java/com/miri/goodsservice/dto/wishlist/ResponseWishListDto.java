package com.miri.goodsservice.dto.wishlist;

import com.miri.goodsservice.domain.goods.Goods;
import com.miri.goodsservice.domain.wishlist.WishList;
import lombok.AllArgsConstructor;
import lombok.Data;

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
}
