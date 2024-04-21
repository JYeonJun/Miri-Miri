package com.miri.userservice.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miri.userservice.domain.goods.Goods;
import com.miri.userservice.domain.order.Order;
import com.miri.userservice.domain.wishlist.WishList;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

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
}
