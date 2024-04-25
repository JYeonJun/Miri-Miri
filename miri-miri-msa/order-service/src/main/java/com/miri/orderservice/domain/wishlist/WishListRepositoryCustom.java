package com.miri.orderservice.domain.wishlist;

import com.miri.orderservice.dto.wishlist.ResponseWishListDto.GoodsInWishListRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishListRepositoryCustom {

    Page<GoodsInWishListRespDto> findPagingGoodsInWishList(Long userId, Pageable pageable);
}
