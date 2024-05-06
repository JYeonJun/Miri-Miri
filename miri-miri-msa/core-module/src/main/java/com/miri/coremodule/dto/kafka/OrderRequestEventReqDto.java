package com.miri.coremodule.dto.kafka;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestEventReqDto implements Serializable {
    private Long userId;
    private Long goodsId;
    private Integer quantity;
    private Integer goodsPrice;
}
