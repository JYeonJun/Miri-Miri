package com.miri.orderservice.domain.goods;

import com.miri.orderservice.dto.goods.ResponseGoodsDto.GoodsListDto;
import com.miri.orderservice.dto.goods.ResponseGoodsDto.RegisterGoodsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GoodsRepositoryCustom {

    Page<GoodsListDto> findPagingGoods(Pageable pageable);

    Page<RegisterGoodsDto> findPagingRegisterGoods(Long userId, Pageable pageable);
}
