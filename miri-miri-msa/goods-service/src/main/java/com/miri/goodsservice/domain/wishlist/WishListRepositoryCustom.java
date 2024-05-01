package com.miri.goodsservice.domain.wishlist;

import com.miri.coremodule.dto.wishlist.FeignWishListRespDto.GoodsInWishListRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishListRepositoryCustom {

    Page<GoodsInWishListRespDto> findPagingGoodsInWishList(Long userId, Pageable pageable);
}
