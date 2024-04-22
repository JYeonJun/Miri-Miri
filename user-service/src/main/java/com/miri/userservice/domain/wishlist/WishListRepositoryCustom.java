package com.miri.userservice.domain.wishlist;

import com.miri.userservice.dto.wishlist.ResponseWishListDto.GoodsInWishListRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishListRepositoryCustom {

    Page<GoodsInWishListRespDto> findPagingGoodsInWishList(Long userId, Pageable pageable);
}
