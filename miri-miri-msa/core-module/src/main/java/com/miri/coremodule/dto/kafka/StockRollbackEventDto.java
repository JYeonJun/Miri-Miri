package com.miri.coremodule.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockRollbackEventDto {
    private Long goodsId;
    private Integer quantity;
    private String traceId;

    public StockRollbackEventDto(OrderRequestEventDto orderRequestEventDto) {
        this.goodsId = orderRequestEventDto.getGoodsId();
        this.quantity = orderRequestEventDto.getQuantity();
        this.traceId = orderRequestEventDto.getTraceId();
    }
}
