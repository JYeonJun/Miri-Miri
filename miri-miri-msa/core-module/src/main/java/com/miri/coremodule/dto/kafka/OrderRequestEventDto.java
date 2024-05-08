package com.miri.coremodule.dto.kafka;

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
}
