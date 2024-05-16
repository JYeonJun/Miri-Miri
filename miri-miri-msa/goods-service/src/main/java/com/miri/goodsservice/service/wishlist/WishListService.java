package com.miri.goodsservice.service.wishlist;

import com.miri.coremodule.dto.wishlist.FeignWishListRespDto.WishListOrderedRespDto;
import com.miri.coremodule.dto.wishlist.FeignWishListRespDto.WishListRespDto;
import com.miri.goodsservice.dto.wishlist.RequestWishListDto.AddToCartReqDto;
import com.miri.goodsservice.dto.wishlist.ResponseWishListDto.AddToWishListRespDto;
import com.miri.goodsservice.dto.wishlist.ResponseWishListDto.WishListUpdateRespDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface WishListService {

    // 장바구니에 상품 추가 기능
    AddToWishListRespDto addToWishList(Long userId, AddToCartReqDto addToCartReqDto);

    // 장바구니에 담긴 상품 목록 조회 기능
    WishListRespDto getWishListGoods(Long userId, Pageable pageable);

    // 장바구니에 담은 상품 수량 변경 기능
    WishListUpdateRespDto updateGoodsQuantityInWishList(Long userId, Long wishListId, int goodsQuantity);

    void deleteGoodsInWishList(Long userId, Long wishListId);

    List<WishListOrderedRespDto> getOrderedWishLists(Long userId, List<Long> wishListIds);

    void deleteOrderedWishLists(List<Long> wishListIds);
}
