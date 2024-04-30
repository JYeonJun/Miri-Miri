package com.miri.userservice.client;

import com.miri.coremodule.dto.ResponseDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.RegisterGoodsListRespDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.WishListRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "goods-service")
public interface GoodsServiceClient {
    @GetMapping("/api/auth/my/goods")
    ResponseDto<RegisterGoodsListRespDto> getRegisteredGoodsList(@RequestHeader("X-User-Id") String userId,
                                                                 @RequestParam("page") int page);

    @GetMapping("/api/auth/wishlist")
    ResponseDto<WishListRespDto> getWishListGoods(@RequestHeader("X-User-Id") String userId,
                                                  @RequestParam("page") int page);
}
