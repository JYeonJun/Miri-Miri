package com.miri.userservice.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;

public class RequestOrderDto {

    @Getter
    public static class CreateOrderReqDto {
        @NotNull(message = "위시리스트 ID 목록은 널이 될 수 없습니다.")
        @Size(min = 1, message = "위시리스트 ID 목록은 최소한 하나 이상의 값이 포함되어야 합니다.")
        private List<Long> wishListIds;
    }
}
