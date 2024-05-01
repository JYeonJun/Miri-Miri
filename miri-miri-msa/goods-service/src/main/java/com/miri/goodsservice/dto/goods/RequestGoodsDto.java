package com.miri.goodsservice.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miri.goodsservice.domain.goods.GoodsCategory;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Data;

public class RequestGoodsDto {

    @Data
    public static class GoodsRegistrationReqDto {
        @NotBlank(message = "상품 이름은 필수 입력 값입니다.")
        @Size(max = 20, message = "상품 이름은 최대 20자까지 입력 가능합니다.")
        private String goodsName;

        @NotBlank(message = "상품 설명은 필수 입력 값입니다.")
        @Size(max = 255, message = "상품 설명은 최대 255자까지 입력 가능합니다.")
        private String goodsDescription;

        @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
        private int goodsPrice;

        @Min(value = 0, message = "재고 수량은 0개 이상이어야 합니다.")
        private int stockQuantity;

        @NotNull(message = "상품 카테고리는 필수 입력 값입니다.")
        private GoodsCategory category;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Future(message = "예약구매 시작 시간은 현재 시간 이후여야 합니다.")
        private LocalDateTime reservationStartTime;
    }

    @Data
    public static class UpdateRegisteredGoodsReqDto {
        @Size(min = 1, max = 20, message = "상품 이름은 최대 20자까지 입력 가능합니다.")
        private String goodsName;

        @Size(min = 10, max = 255, message = "상품 설명은 최소 10자부터 최대 255자까지 입력 가능합니다.")
        private String goodsDescription;

        @Min(value = 0, message = "상품 가격은 0원 이상이어야 합니다.")
        private Integer goodsPrice;

        private GoodsCategory category;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Future(message = "예약구매 시작 시간은 현재 시간 이후여야 합니다.")
        private LocalDateTime reservationStartTime;
    }
}
