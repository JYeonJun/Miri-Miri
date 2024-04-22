package com.miri.userservice.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miri.userservice.domain.goods.Goods;
import com.miri.userservice.domain.goods.GoodsCategory;
import com.miri.userservice.domain.order.Order;
import com.miri.userservice.domain.order.OrderDetail;
import com.miri.userservice.domain.wishlist.WishList;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

public class ResponseOrderDto {

    @Data
    public static class CreateOrderRespDto {

        private Long orderId;
        private String orderStatus;
        private List<OrderGoodsRespDto> orderGoods;
        private int totalOrderPrice;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime orderDate;

        public CreateOrderRespDto(Order order, List<OrderGoodsRespDto> orderGoods, int totalOrderPrice) {
            this.orderId = order.getId();
            this.orderStatus = order.getOrderStatus().getValue();
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

        public OrderGoodsRespDto(WishList wishList, Goods goods, int subTotalPrice) {
            this.goodsId = goods.getId();
            this.goodsName = goods.getGoodsName();
            this.quantity = wishList.getQuantity();
            this.unitPrice = goods.getGoodsPrice();
            this.subTotalPrice = subTotalPrice;
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
        private String orderStatus; // 주문 상태
        private int orderQuantity; // 주문한 상품 개수
        private int unitPrice; // 주문한 상품 가격
        private int subTotalPrice; // 주문한 상품 총 가격
        private String category; // 상품 카테고리
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime orderDate; // 주문 날짜

        public OrderGoodsDto(Order order, OrderDetail orderDetail, Goods goods) {
            this.orderId = order.getId();
            this.orderStatus = order.getOrderStatus().getValue();
            this.orderQuantity = orderDetail.getQuantity();
            this.unitPrice = goods.getGoodsPrice();
            this.subTotalPrice = orderDetail.getQuantity() * goods.getGoodsPrice();
            this.category = goods.getCategory().getValue();
            this.orderDate = orderDetail.getCreatedDate();
        }
    }
}
