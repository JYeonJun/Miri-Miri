package com.miri.coremodule.dto.kafka;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestEventDto {
    private Long userId;
    private Long goodsId;
    private Integer quantity;
    private Integer goodsPrice;
    private String address;
    private String traceId;

    public OrderRequestEventDto(Long userId, Long goodsId, Integer quantity,
                                Integer goodsPrice, String address, String traceId) {
        this.userId = userId;
        this.goodsId = goodsId;
        this.quantity = quantity;
        this.goodsPrice = goodsPrice;
        this.address = address;
        this.traceId = traceId;
    }
}
