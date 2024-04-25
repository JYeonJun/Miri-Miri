package com.miri.userservice.domain.goods;

import com.miri.userservice.dto.goods.ResponseGoodsDto.GoodsListDto;
import com.miri.userservice.dto.goods.ResponseGoodsDto.RegisterGoodsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GoodsRepositoryCustom {

    Page<GoodsListDto> findPagingGoods(Pageable pageable);

    Page<RegisterGoodsDto> findPagingRegisterGoods(Long userId, Pageable pageable);
}
