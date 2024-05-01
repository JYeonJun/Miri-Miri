package com.miri.goodsservice.domain.goods;

import com.miri.coremodule.dto.goods.FeignGoodsRespDto.RegisterGoodsDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GoodsRepositoryCustom {

    Page<GoodsListDto> findPagingGoods(Pageable pageable);

    Page<RegisterGoodsDto> findPagingRegisterGoods(Long userId, Pageable pageable);
}
