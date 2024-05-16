package com.miri.coremodule.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

public class FeignOrderRespDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderGoodsListRespDto {
        private Page<OrderGoodsDto> orders;
    }

    @Data
    @NoArgsConstructor
    public static class OrderGoodsDto {
        private Long orderId; // 주문 ID
        private Long orderDetailId; // 상세 주문 ID
        private Long goodsId; // 상품 ID
        private String orderStatus; // 주문 상태
        private int orderQuantity; // 주문한 상품 개수
        private int unitPrice; // 주문한 상품 가격
        private int subTotalPrice; // 주문한 상품 총 가격
        private String category; // 상품 카테고리
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime orderDate; // 주문 날짜

        public OrderGoodsDto(Long orderId, Long orderDetailId, Long goodsId, String orderStatus, int orderQuantity,
                             LocalDateTime orderDate) {
            this.orderId = orderId;
            this.orderDetailId = orderDetailId;
            this.goodsId = goodsId;
            this.orderStatus = orderStatus;
            this.orderQuantity = orderQuantity;
            this.orderDate = orderDate;
        }
    }
}
