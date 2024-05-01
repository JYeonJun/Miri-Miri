package com.miri.userservice.client;

import com.miri.coremodule.dto.ResponseDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.RegisterGoodsListRespDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.WishListRespDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "goods-service")
public interface GoodsServiceClient {

    Logger log = LoggerFactory.getLogger(GoodsServiceClient.class);

    @CircuitBreaker(name = "registerGoodsCircuitBreaker", fallbackMethod = "registerGoodsFallback")
    @GetMapping("/api/auth/my/goods")
    ResponseDto<RegisterGoodsListRespDto> getRegisteredGoodsList(@RequestHeader("X-User-Id") String userId,
                                                                 @RequestParam("page") int page);

    default ResponseDto<RegisterGoodsListRespDto> registerGoodsFallback(Throwable e) {
        log.error("마이페이지 정보 조회: 등록한 상품 목록 조회 실패");
        return null;
    }

    @CircuitBreaker(name = "wishlistGoodsCircuitBreaker", fallbackMethod = "wishlistGoodsFallback")
    @GetMapping("/api/auth/wishlist")
    ResponseDto<WishListRespDto> getWishListGoods(@RequestHeader("X-User-Id") String userId,
                                                  @RequestParam("page") int page);

    default ResponseDto<WishListRespDto> wishlistGoodsFallback(Throwable e) {
        log.error("마이페이지 정보 조회: 위시리스트 목록 조회 실패");
        return null;
    }
}
