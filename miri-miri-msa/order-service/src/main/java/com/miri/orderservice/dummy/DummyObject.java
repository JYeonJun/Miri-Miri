package com.miri.orderservice.dummy;

import com.miri.orderservice.domain.order.Order;
import com.miri.orderservice.domain.order.OrderDetail;
import com.miri.orderservice.domain.order.OrderStatus;

public class DummyObject {

    protected Order newOrder(Long userId) {

        return Order.builder()
                .userId(userId)
                .build();
    }

    protected OrderDetail newOrderDetail(Order order, Long goodsId
            , OrderStatus orderStatus, int quantity, int unitPrice) {

        return OrderDetail.builder()
                .order(order)
                .goodsId(goodsId)
                .orderStatus(orderStatus)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .build();
    }
}
