package com.miri.userservice.client;

import com.miri.coremodule.dto.ResponseDto;
import com.miri.coremodule.dto.order.FeignOrderRespDto.OrderGoodsListRespDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    Logger log = LoggerFactory.getLogger(OrderServiceClient.class);

    @CircuitBreaker(name = "orderGoodsCircuitBreaker", fallbackMethod = "orderGoodsFallback")
    @GetMapping("/api/auth/orders")
    ResponseDto<OrderGoodsListRespDto> getOrderGoodsList(@RequestHeader("X-User-Id") String userId,
                                                        @RequestParam("page") int page);

    default ResponseDto<OrderGoodsListRespDto> orderGoodsFallback(Throwable e) {
        log.error("마이페이지 정보 조회: 주문 목록 조회 실패");
        return null;
    }
}
