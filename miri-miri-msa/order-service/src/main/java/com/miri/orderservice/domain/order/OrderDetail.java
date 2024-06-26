package com.miri.orderservice.domain.order;

import com.miri.coremodule.domain.BaseTimeEntity;
import com.miri.coremodule.dto.kafka.OrderRequestEventDto;
import com.miri.coremodule.dto.wishlist.FeignWishListRespDto.WishListOrderedRespDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "order_details")
public class OrderDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Long goodsId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    private int quantity; // 상품 주문 개수

    private int unitPrice; // 주문 상품 가격

    public OrderDetail(Order order, WishListOrderedRespDto foundWishList) {
        this.order = order;
        this.goodsId = foundWishList.getGoodsId();
        this.quantity = foundWishList.getOrderQuantity();
        this.unitPrice = foundWishList.getUnitPrice();
        this.orderStatus = OrderStatus.COMPLETED;
    }

    public OrderDetail(Order order, OrderRequestEventDto orderRequestEventDto) {
        this.order = order;
        this.goodsId = orderRequestEventDto.getGoodsId();
        this.quantity = orderRequestEventDto.getQuantity();
        this.unitPrice = orderRequestEventDto.getGoodsPrice();
        this.orderStatus = OrderStatus.COMPLETED;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
