package com.miri.orderservice.client;

import com.miri.coremodule.dto.goods.FeignGoodsRespDto.GoodsStockRespDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.OrderedGoodsDetailRespDto;
import com.miri.coremodule.dto.wishlist.FeignWishListReqDto.WishListOrderedReqDto;
import com.miri.coremodule.dto.wishlist.FeignWishListRespDto.WishListOrderedRespDto;
import com.miri.coremodule.handler.ex.CustomApiException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "goods-service")
public interface GoodsServiceClient {

    Logger log = LoggerFactory.getLogger(GoodsServiceClient.class);

    // 주문한 상품에 대한 위시리스트 목록 조회
    @CircuitBreaker(name = "orderedWishlistCircuitBreaker", fallbackMethod = "orderedWishlistFallback")
    @PostMapping("/api/internal/ordered-wishlist")
    List<WishListOrderedRespDto> getOrderedWishLists(WishListOrderedReqDto wishListOrderedReqDto);

    default List<WishListOrderedRespDto> orderedWishlistFallback(Throwable e) {
        log.error("상품 주문: 위시리스트 목록 조회 실패");
        return null;
    }

    // 상품 재고 감소 요청
    @CircuitBreaker(name = "decreaseGoodsStockCircuitBreaker", fallbackMethod = "decreaseGoodsStockFallback")
    @PostMapping("/api/internal/goods/decrease")
    List<GoodsStockRespDto> decreaseStock(@RequestBody Map<Long, Integer> decreaseStockRequests);

    default void decreaseGoodsStockFallback(Throwable e) {
        log.error("상품 주문: 상품 재고 감소 요청 실패");
        throw new CustomApiException("재고 감소 요청이 실패했습니다.");
    }

    // 주문 완료된 위시리스트 목록 삭제 요청
    @CircuitBreaker(name = "deleteWishListCircuitBreaker", fallbackMethod = "deleteWishListFallback")
    @PostMapping("/api/internal/wishlist/delete")
    void deleteOrderedWishLists(List<Long> wishListIds);

    default void deleteWishListFallback(Throwable e) {
        log.error("상품 주문: 위시리스트 목록 삭제 요청 실패");
        // TODO: 롤백 요청
        throw new CustomApiException("위시리스트 목록 삭제에 실패하였습니다.");
    }

    // 주문한 상품에 대한 상품 정보 조회
    @CircuitBreaker(name = "orderedGoodsCircuitBreaker", fallbackMethod = "orderedGoodsFallback")
    @GetMapping("/api/internal/ordered-goods/details")
    Map<Long, OrderedGoodsDetailRespDto> getOrderedGoodsDetailsAsMap(@RequestParam("goodsIds") Set<Long> goodsIds);

    default Map<Long, OrderedGoodsDetailRespDto> orderedGoodsFallback(Throwable e) {
        log.error("주문 목록 조회: 상품 상세 정보 조회 에러");
        return Collections.emptyMap();
    }

    @CircuitBreaker(name = "increaseGoodsStockCircuitBreaker", fallbackMethod = "increaseGoodsStockFallback")
    @PostMapping("/api/internal/goods/increase")
    GoodsStockRespDto increaseStock(@RequestBody Map<Long, Integer> goodsIdToQuantityMap);

    default void increaseGoodsStockFallback(Throwable e) {
        log.error("상품 재고 증가 요청 실패");
        throw new CustomApiException("재고 증가 요청에 실패했습니다.");
    }
}
