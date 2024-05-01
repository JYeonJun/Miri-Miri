package com.miri.orderservice.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miri.orderservice.domain.order.Order;
import com.miri.orderservice.domain.order.OrderDetail;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

public class ResponseOrderDto {

    @Data
    public static class CreateOrderRespDto {

        private Long orderId;
        private List<OrderGoodsRespDto> orderGoods;
        private int totalOrderPrice;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime orderDate;

        public CreateOrderRespDto(Order order, List<OrderGoodsRespDto> orderGoods, int totalOrderPrice) {
            this.orderId = order.getId();
            this.orderGoods = orderGoods;
            this.totalOrderPrice = totalOrderPrice;
            this.orderDate = order.getCreatedDate();
        }
    }

    @Data
    public static class OrderGoodsRespDto {
        private Long goodsId;
        private String goodsName;
        private int quantity; // 상품의 주문 개수
        private int unitPrice;
        private int subTotalPrice;
        private String orderStatus;

        public OrderGoodsRespDto(OrderDetail orderDetail, String goodsName) {
            this.goodsId = orderDetail.getGoodsId();
            this.goodsName = goodsName;
            this.quantity = orderDetail.getQuantity();
            this.unitPrice = orderDetail.getUnitPrice();
            this.subTotalPrice = this.unitPrice * this.quantity;
            this.orderStatus = orderDetail.getOrderStatus().getValue();
        }
    }
}
