package com.miri.orderservice.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miri.orderservice.domain.order.Order;
import com.miri.orderservice.domain.order.OrderDetail;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

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
        private int quantity;
        private int unitPrice;
        private int subTotalPrice;
        private String orderStatus;

//        public OrderGoodsRespDto(WishList wishList, OrderDetail orderDetail, Goods goods, int subTotalPrice) {
//            this.goodsId = goods.getId();
//            this.goodsName = goods.getGoodsName();
//            this.quantity = wishList.getQuantity();
//            this.unitPrice = goods.getGoodsPrice();
//            this.subTotalPrice = subTotalPrice;
//            this.orderStatus = orderDetail.getOrderStatus().getValue();
//        }

        public OrderGoodsRespDto(OrderDetail orderDetail, Long goodsId) {
            this.goodsId = goodsId;
//            this.goodsName = goods.getGoodsName();
//            this.quantity = wishList.getQuantity();
//            this.unitPrice = goods.getGoodsPrice();
//            this.subTotalPrice = subTotalPrice;
            this.orderStatus = orderDetail.getOrderStatus().getValue();
        }

//        public OrderGoodsRespDto(OrderDetail orderDetail, Goods goods) {
//            this.goodsId = goods.getId();
//            this.goodsName = goods.getGoodsName();
//            this.quantity = orderDetail.getQuantity();
//            this.unitPrice = orderDetail.getUnitPrice();
//            this.subTotalPrice = this.unitPrice * this.quantity;
//            this.orderStatus = orderDetail.getOrderStatus().getValue();
//        }

        public OrderGoodsRespDto(OrderDetail orderDetail) {
            this.goodsId = orderDetail.getGoodsId();
//            this.goodsName = goods.getGoodsName();
            this.quantity = orderDetail.getQuantity();
            this.unitPrice = orderDetail.getUnitPrice();
            this.subTotalPrice = this.unitPrice * this.quantity;
            this.orderStatus = orderDetail.getOrderStatus().getValue();
        }
    }

    @Data
    @AllArgsConstructor
    public static class OrderGoodsListRespDto {
        private Page<OrderGoodsDto> orders;
    }

    @Data
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

//        public OrderGoodsDto(Order order, OrderDetail orderDetail, Goods goods) {
//            this.orderId = order.getId();
//            this.orderDetailId = orderDetail.getId();
//            this.goodsId = goods.getId();
//            this.orderStatus = orderDetail.getOrderStatus().getValue();
//            this.orderQuantity = orderDetail.getQuantity();
//            this.unitPrice = goods.getGoodsPrice();
//            this.subTotalPrice = orderDetail.getQuantity() * goods.getGoodsPrice();
//            this.category = goods.getCategory().getValue();
//            this.orderDate = orderDetail.getCreatedDate();
//        }

        public OrderGoodsDto(Order order, OrderDetail orderDetail) {
            this.orderId = order.getId();
            this.orderDetailId = orderDetail.getId();
            this.goodsId = orderDetail.getGoodsId();
            this.orderStatus = orderDetail.getOrderStatus().getValue();
            this.orderQuantity = orderDetail.getQuantity();
//            this.unitPrice = goods.getGoodsPrice();
//            this.subTotalPrice = orderDetail.getQuantity() * goods.getGoodsPrice();
//            this.category = goods.getCategory().getValue();
            this.orderDate = orderDetail.getCreatedDate();
        }
    }
}
