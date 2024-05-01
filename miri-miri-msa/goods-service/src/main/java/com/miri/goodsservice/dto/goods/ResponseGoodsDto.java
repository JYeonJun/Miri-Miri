package com.miri.goodsservice.dto.goods;

import com.miri.goodsservice.domain.goods.Goods;
import com.miri.goodsservice.domain.goods.GoodsCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

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
    @AllArgsConstructor
    public static class GoodsListRespDto {
        Page<GoodsListDto> goodsList;
    }

    @Data
    public static class GoodsListDto {
        private Long goodsId;
        private String goodsName;
        private int goodsPrice;
        private String category;

        public GoodsListDto(Long goodsId, String goodsName, int goodsPrice, GoodsCategory category) {
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

        public GoodsDetailRespDto(Goods goods, String sellerName) {
            this.goodsId = goods.getId();
            this.sellerId = goods.getSellerId();
            this.sellerName = sellerName;
            this.goodsName = goods.getGoodsName();
            this.goodsDescription = goods.getGoodsDescription();
            this.goodsPrice = goods.getGoodsPrice();
            this.stockQuantity = goods.getStockQuantity();
            this.category = goods.getCategory().getValue();
        }
    }
}
