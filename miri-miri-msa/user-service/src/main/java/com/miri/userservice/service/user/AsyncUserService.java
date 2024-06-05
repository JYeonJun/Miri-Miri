package com.miri.userservice.service.user;

import com.miri.coremodule.dto.goods.FeignGoodsRespDto.RegisterGoodsListRespDto;
import com.miri.coremodule.dto.order.FeignOrderRespDto.OrderGoodsListRespDto;
import com.miri.coremodule.dto.wishlist.FeignWishListRespDto.WishListRespDto;
import java.util.concurrent.CompletableFuture;

public interface AsyncUserService {
    CompletableFuture<RegisterGoodsListRespDto> getRegisteredGoodsListAsync(Long userId);
    CompletableFuture<WishListRespDto> getWishListGoodsAsync(Long userId);
    CompletableFuture<OrderGoodsListRespDto> getOrderGoodsListAsync(Long userId);
}

