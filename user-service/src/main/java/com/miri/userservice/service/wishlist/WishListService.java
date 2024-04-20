package com.miri.userservice.service.wishlist;

import com.miri.userservice.dto.wishlist.RequestWishListDto.AddToCartReqDto;
import com.miri.userservice.dto.wishlist.ResponseWishListDto.AddToWishListRespDto;
import com.miri.userservice.dto.wishlist.ResponseWishListDto.WishListUpdateRespDto;
import com.miri.userservice.dto.wishlist.ResponseWishListDto.WishListRespDto;

public interface WishListService {

    // 장바구니에 상품 추가 기능
    AddToWishListRespDto addToWishList(Long userId, AddToCartReqDto addToCartReqDto);

    // 장바구니에 담긴 상품 목록 조회 기능
    WishListRespDto getWishListGoods(Long userId);

    // 장바구니에 담은 상품 수량 변경 기능
    WishListUpdateRespDto updateGoodsQuantityInWishList(Long userId, Long wishListId, int goodsQuantity);

    void deleteGoodsInWishList(Long userId, Long wishListId);
}
