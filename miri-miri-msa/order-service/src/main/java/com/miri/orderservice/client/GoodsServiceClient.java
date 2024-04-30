package com.miri.orderservice.client;

import com.miri.coremodule.dto.goods.FeignGoodsRespDto.GoodsStockDecreaseRespDto;
import com.miri.coremodule.dto.wishlist.FeignWishListReqDto.WishListOrderedReqDto;
import com.miri.coremodule.dto.wishlist.FeignWishListRespDto.WishListOrderedRespDto;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "goods-service")
public interface GoodsServiceClient {

    // 주문한 상품에 대한 위시리스트 목록 조회
    @PostMapping("/api/internal/ordered-wishlist")
    List<WishListOrderedRespDto> getOrderedWishLists(WishListOrderedReqDto wishListOrderedReqDto);

    // 상품 재고 감소 요청
    @PostMapping("/api/internal/goods/decrease")
    List<GoodsStockDecreaseRespDto> decreaseStock(@RequestBody Map<Long, Integer> decreaseStockRequests);

    // 주문 완료된 위시리스트 목록 삭제 요청
    @PostMapping("/api/internal/wishlist/delete")
    void deleteOrderedWishLists(List<Long> wishListIds);

    // 주문한 상품에 대한 상품 목록 조회
}
