package com.miri.goodsservice.controller;

import com.miri.coremodule.dto.wishlist.FeignWishListReqDto.WishListOrderedReqDto;
import com.miri.coremodule.dto.wishlist.FeignWishListRespDto.WishListOrderedRespDto;
import com.miri.goodsservice.service.wishlist.WishListService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal")
public class WishListClientController {

    private final WishListService wishListService;

    public WishListClientController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    // 주문한 상품에 대한 위시리스트 목록 조회
    @PostMapping("/ordered-wishlist")
    public ResponseEntity<?> getOrderedWishLists(@RequestBody WishListOrderedReqDto reqDto) {
        List<WishListOrderedRespDto> result
                = wishListService.getOrderedWishLists(reqDto.getUserId(), reqDto.getWishListIds());
        return ResponseEntity.ok(result);
    }

    // 주문한 상품에 대한 위시리스트 삭제 요청
    @PostMapping("/wishlist/delete")
    public ResponseEntity<?> deleteOrderedWishLists(@RequestBody List<Long> wishListIds) {
        wishListService.deleteOrderedWishLists(wishListIds);
        return ResponseEntity.ok().build();
    }
}
