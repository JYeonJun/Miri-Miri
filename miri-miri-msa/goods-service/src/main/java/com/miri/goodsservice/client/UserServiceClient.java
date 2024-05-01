package com.miri.goodsservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    Logger log = LoggerFactory.getLogger(UserServiceClient.class);

    @CircuitBreaker(name = "getUserNameCircuitBreaker", fallbackMethod = "getUserNameFallback")
    @GetMapping("/api/internal/users/{userId}/name")
    String getUserNameById(@PathVariable("userId") Long userId);

    default String getUserNameFallback(Throwable e) {
        log.error("등록 상품 조회: 상품 판매자 이름 조회 실패");
        return null;
    }
}
