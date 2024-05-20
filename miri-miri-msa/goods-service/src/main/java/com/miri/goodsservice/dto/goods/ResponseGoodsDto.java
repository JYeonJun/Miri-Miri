package com.miri.goodsservice.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miri.goodsservice.domain.goods.Goods;
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
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime reservationStartTime;

        public GoodsRegistrationRespDto(Goods goods) {
            this.sellerId = goods.getSellerId();
            this.goodsName = goods.getGoodsName();
            this.goodsDescription = goods.getGoodsDescription();
            this.goodsPrice = goods.getGoodsPrice();
            this.stockQuantity = goods.getStockQuantity();
            this.category = goods.getCategory().getValue();
            this.reservationStartTime = goods.getReservationStartTime();
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
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime reservationStartTime;

        public GoodsListDto(Goods goods) {
            this.goodsId = goods.getId();
            this.goodsName = goods.getGoodsName();
            this.goodsPrice = goods.getGoodsPrice();
            this.category = goods.getCategory().getValue();
            this.reservationStartTime = goods.getReservationStartTime();
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
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime reservationStartTime;

        public GoodsDetailRespDto(Goods goods, Integer goodsStock, String sellerName) {
            this.goodsId = goods.getId();
            this.sellerId = goods.getSellerId();
            this.sellerName = sellerName;
            this.goodsName = goods.getGoodsName();
            this.goodsDescription = goods.getGoodsDescription();
            this.goodsPrice = goods.getGoodsPrice();
            this.stockQuantity = goodsStock;
            this.category = goods.getCategory().getValue();
            this.reservationStartTime = goods.getReservationStartTime();
        }
    }

    @Data
    public static class UpdateRegisteredGoodsRespDto {
        private Long goodsId;
        private String goodsName;
        private String goodsDescription;
        private int goodsPrice;
        private int stockQuantity;
        private String category;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime reservationStartTime;

        public UpdateRegisteredGoodsRespDto(Goods goods) {
            this.goodsId = goods.getId();
            this.goodsName = goods.getGoodsName();
            this.goodsDescription = goods.getGoodsDescription();
            this.goodsPrice = goods.getGoodsPrice();
            this.stockQuantity = goods.getStockQuantity();
            this.category = goods.getCategory().getValue();
            this.reservationStartTime = goods.getReservationStartTime();
        }
    }

    @Data
    @AllArgsConstructor
    public static class GoodsStockQuantityRespDto {
        private int goodsStockQuantity;
    }
}
