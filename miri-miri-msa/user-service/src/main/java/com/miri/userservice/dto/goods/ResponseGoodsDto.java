package com.miri.userservice.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miri.userservice.domain.goods.Goods;
import com.miri.userservice.domain.goods.GoodsCategory;
import com.miri.userservice.domain.user.User;
import java.time.LocalDateTime;
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

    @Data
    @AllArgsConstructor
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

        public RegisterGoodsDto(Goods goods) {
            this.goodsId = goods.getId();
            this.goodsName = goods.getGoodsName();
            this.goodsPrice = goods.getGoodsPrice();
            this.stockQuantity = goods.getStockQuantity();
            this.category = goods.getCategory().getValue();
            this.registerDate = goods.getCreatedDate();
            this.lastModifiedDate = goods.getLastModifiedDate();
        }
    }
}
