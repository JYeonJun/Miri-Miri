package com.miri.coremodule.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

public class FeignGoodsRespDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GoodsStockRespDto {
        private Long goodsId;
        private int remainStockQuantity;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderedGoodsDetailRespDto {
        private Long goodsId;
        private int goodsPrice;
        private String category;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegisterGoodsListRespDto {
        Page<RegisterGoodsDto> registerGoods;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
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
}
