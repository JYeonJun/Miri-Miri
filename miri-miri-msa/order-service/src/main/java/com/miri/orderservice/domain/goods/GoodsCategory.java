package com.miri.orderservice.domain.goods;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GoodsCategory {
    FASHION("패션"),
    BEAUTY("뷰티"),
    ETC("기타");
    private final String value;
}
