package com.miri.goodsservice.domain.goods;

import com.miri.coremodule.handler.ex.CustomApiException;
import com.miri.goodsservice.domain.common.BaseTimeEntity;
import com.miri.goodsservice.dto.goods.RequestGoodsDto.GoodsRegistrationReqDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "goods")
public class Goods extends BaseTimeEntity {

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

    // 재고 수량 감소 메소드
    public void decreaseStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new CustomApiException("상품의 재고가 부족합니다.");
        }
        this.stockQuantity = restStock;
    }

    public void increaseStock(int stockQuantity) {
        this.stockQuantity += stockQuantity;
    }
}
