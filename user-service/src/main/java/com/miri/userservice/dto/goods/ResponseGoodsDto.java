package com.miri.userservice.dto.goods;

import com.miri.userservice.domain.goods.Goods;
import com.miri.userservice.domain.goods.GoodsCategory;
import com.miri.userservice.domain.user.User;
import lombok.Data;

public class ResponseGoodsDto {

    @Data
    public static class GoodsRegistrationRespDto {
        private Long sellerId;
        private String goodsName;
        private String goodsDescription;
        private int goodsPrice;
        private int stockQuantity;
        private String category;

        public GoodsRegistrationRespDto(Goods goods) {
            this.sellerId = goods.getSellerId();
            this.goodsName = goods.getGoodsName();
            this.goodsDescription = goods.getGoodsDescription();
            this.goodsPrice = goods.getGoodsPrice();
            this.stockQuantity = goods.getStockQuantity();
            this.category = goods.getCategory().getValue();
        }
    }

    @Data
    public static class GoodsListRespDto {
        private Long goodsId;
        private String goodsName;
        private int goodsPrice;
        private String category;

        public GoodsListRespDto(Long goodsId, String goodsName, int goodsPrice, GoodsCategory category) {
            this.goodsId = goodsId;
            this.goodsName = goodsName;
            this.goodsPrice = goodsPrice;
            this.category = category.getValue();
        }
    }

    @Data
    public static class GoodsDetailRespDto {
        private Long goodsId;
        private Long sellerId;
        private String sellerName;
        private String goodsName;
        private String goodsDescription;
        private int goodsPrice;
        private int stockQuantity;
        private String category;

        public GoodsDetailRespDto(Goods goods, User user) {
            this.goodsId = goods.getId();
            this.sellerId = user.getId();
            this.sellerName = user.getUserName();
            this.goodsName = goods.getGoodsName();
            this.goodsDescription = goods.getGoodsDescription();
            this.goodsPrice = goods.getGoodsPrice();
            this.stockQuantity = goods.getStockQuantity();
            this.category = goods.getCategory().getValue();
        }
    }
}
