package com.miri.goodsservice.dummy;

import com.miri.goodsservice.domain.goods.Goods;
import com.miri.goodsservice.domain.goods.GoodsCategory;
import com.miri.goodsservice.domain.order.Order;
import com.miri.goodsservice.domain.order.OrderDetail;
import com.miri.goodsservice.domain.order.OrderStatus;
import com.miri.goodsservice.domain.user.User;
import com.miri.goodsservice.domain.user.UserRole;
import com.miri.goodsservice.domain.wishlist.WishList;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DummyObject {

    protected User newUser(String email, String userName,
                           String phoneNumber, String address) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");

        return User.builder()
                .email(email)
                .userName(userName)
                .password(encPassword)
                .phoneNumber(phoneNumber)
                .address(address)
                .role(UserRole.USER)
                .build();
    }

    protected Goods newGoods(Long sellerId, String goodsName
            , int goodsPrice, int stockQuantity
            , GoodsCategory category) {

        return Goods.builder()
                .sellerId(sellerId)
                .goodsName(goodsName)
                .goodsDescription("test용 상품")
                .goodsPrice(goodsPrice)
                .stockQuantity(stockQuantity)
                .category(category)
                .build();
    }

    protected WishList newWishList(Long userId, Goods goods, int quantity) {

        return WishList.builder()
                .userId(userId)
                .goods(goods)
                .quantity(quantity)
                .build();
    }

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
