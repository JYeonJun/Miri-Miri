package com.miri.orderservice.client;

import com.miri.coremodule.dto.goods.FeignGoodsReqDto.GoodsStockIncreaseReqDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.GoodsStockRespDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.OrderedGoodsDetailRespDto;
import com.miri.coremodule.dto.wishlist.FeignWishListReqDto.WishListOrderedReqDto;
import com.miri.coremodule.dto.wishlist.FeignWishListRespDto.WishListOrderedRespDto;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "goods-service")
public interface GoodsServiceClient {

    // 주문한 상품에 대한 위시리스트 목록 조회
    @PostMapping("/api/internal/ordered-wishlist")
    List<WishListOrderedRespDto> getOrderedWishLists(WishListOrderedReqDto wishListOrderedReqDto);

    // 상품 재고 감소 요청
    @PostMapping("/api/internal/goods/decrease")
    List<GoodsStockRespDto> decreaseStock(@RequestBody Map<Long, Integer> decreaseStockRequests);

    // 주문 완료된 위시리스트 목록 삭제 요청
    @PostMapping("/api/internal/wishlist/delete")
    void deleteOrderedWishLists(List<Long> wishListIds);

    // 주문한 상품에 대한 상품 정보 조회
    @GetMapping("/api/internal/ordered-goods/details")
    Map<Long, OrderedGoodsDetailRespDto> getOrderedGoodsDetailsAsMap(@RequestParam("goodsIds") Set<Long> goodsIds);

    @PostMapping("/api/internal/goods/increase")
    GoodsStockRespDto increaseStock(@RequestBody GoodsStockIncreaseReqDto reqDto);
}
