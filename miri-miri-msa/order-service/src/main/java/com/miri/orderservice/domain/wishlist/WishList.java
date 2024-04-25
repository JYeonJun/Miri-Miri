package com.miri.orderservice.domain.wishlist;

import com.miri.orderservice.domain.common.BaseTimeEntity;
import com.miri.orderservice.domain.goods.Goods;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "wish_list")
public class WishList extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_list_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    private int quantity; // 관심상품 수량

    public WishList(Long userId, Goods goods, int quantity) {
        this.userId = userId;
        this.goods = goods;
        this.quantity = quantity;
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
