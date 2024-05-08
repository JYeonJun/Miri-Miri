package com.miri.coremodule.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdateEventDto {
    private Long userId;
    private Long orderId;
    private String traceId;
}
