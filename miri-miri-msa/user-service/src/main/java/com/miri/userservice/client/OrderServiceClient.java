package com.miri.userservice.client;

import com.miri.coremodule.dto.ResponseDto;
import com.miri.coremodule.dto.order.FeignOrderRespDto.OrderGoodsListRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    @GetMapping("/api/auth/orders")
    ResponseDto<OrderGoodsListRespDto> getOrderGoodsList(@RequestHeader("X-User-Id") String userId,
                                                        @RequestParam("page") int page);
}
