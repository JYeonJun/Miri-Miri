package com.miri.userservice.domain.goods;

import com.miri.userservice.domain.common.CreatedDateEntity;
import com.miri.userservice.domain.user.User;
import com.miri.userservice.dto.goods.RequestGoodsDto.GoodsRegistrationReqDto;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
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
@Table(name = "goods")
public class Goods extends CreatedDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_id")
    private Long id;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false, length = 20)
    private String goodsName; // 상품 이름

    @Column(nullable = false, length = 255)
    private String goodsDescription; // 상품 설명

    @Column(nullable = false)
    private int goodsPrice; // 가격

    @Column(nullable = false)
    private int stockQuantity; // 재고 수량

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoodsCategory category; // 상품 카테고리

    public Goods(Long sellerId, GoodsRegistrationReqDto goodsDto) {
        this.sellerId = sellerId;
        this.goodsName = goodsDto.getGoodsName();
        this.goodsDescription = goodsDto.getGoodsDescription();
        this.goodsPrice = goodsDto.getGoodsPrice();
        this.stockQuantity = goodsDto.getStockQuantity();
        this.category = goodsDto.getCategory();
    }
}
