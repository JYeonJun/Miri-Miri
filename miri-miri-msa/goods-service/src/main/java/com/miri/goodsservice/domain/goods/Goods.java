package com.miri.goodsservice.domain.goods;

import com.miri.coremodule.domain.BaseTimeEntity;
import com.miri.coremodule.handler.ex.StockUnavailableException;
import com.miri.goodsservice.dto.goods.RequestGoodsDto.GoodsRegistrationReqDto;
import com.miri.goodsservice.dto.goods.RequestGoodsDto.UpdateRegisteredGoodsReqDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Optional;
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

    private LocalDateTime reservationStartTime; // 예약구매 시작 시간

    public Goods(Long sellerId, GoodsRegistrationReqDto goodsDto) {
        this.sellerId = sellerId;
        this.goodsName = goodsDto.getGoodsName();
        this.goodsDescription = goodsDto.getGoodsDescription();
        this.goodsPrice = goodsDto.getGoodsPrice();
        this.stockQuantity = goodsDto.getStockQuantity();
        this.category = goodsDto.getCategory();
        this.reservationStartTime = goodsDto.getReservationStartTime();
    }

    // 재고 수량 감소 메소드
    public int decreaseStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new StockUnavailableException();
        }
        this.stockQuantity = restStock;
        return this.stockQuantity;
    }

    public void increaseStock(int stockQuantity) {
        this.stockQuantity += stockQuantity;
    }

    public void changeGoodsInfo(UpdateRegisteredGoodsReqDto reqDto) {
        Optional.ofNullable(reqDto.getGoodsName()).ifPresent(goodsName -> this.goodsName = goodsName);
        Optional.ofNullable(reqDto.getGoodsDescription())
                .ifPresent(goodsDescription -> this.goodsDescription = goodsDescription);
        Optional.ofNullable(reqDto.getGoodsPrice()).ifPresent(goodsPrice -> this.goodsPrice = goodsPrice);
        Optional.ofNullable(reqDto.getCategory()).ifPresent(category -> this.category = category);
        Optional.ofNullable(reqDto.getReservationStartTime())
                .ifPresent(reservationStartTime -> this.reservationStartTime = reservationStartTime);
    }
}
