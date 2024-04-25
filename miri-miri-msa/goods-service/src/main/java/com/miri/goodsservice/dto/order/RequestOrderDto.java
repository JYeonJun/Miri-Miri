package com.miri.goodsservice.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

public class RequestOrderDto {

    @Data
    public static class CreateOrderReqDto {
        @NotNull(message = "위시리스트 ID 목록은 널이 될 수 없습니다.")
        @Size(min = 1, message = "위시리스트 ID 목록은 최소한 하나 이상의 값이 포함되어야 합니다.")
        private List<Long> wishListIds;

        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z0-9가-힣\\s-_.]{1,50}$", message = "주소 형식이 올바르지 않습니다.")
        private String address;
    }

    @Data
    public static class ReturnOrderReqDto {
        @NotNull(message = "반품 사유는 필수입니다.")
        @Size(min = 10, max = 50, message = "반품 사유는 최소 10글자에서 최대 50글자 사이여야 합니다.")
        private String reason;
    }
}
