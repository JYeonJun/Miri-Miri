package com.miri.goodsservice.domain.wishlist;

import com.miri.goodsservice.dto.wishlist.ResponseWishListDto.GoodsInWishListRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishListRepositoryCustom {

    Page<GoodsInWishListRespDto> findPagingGoodsInWishList(Long userId, Pageable pageable);
}
