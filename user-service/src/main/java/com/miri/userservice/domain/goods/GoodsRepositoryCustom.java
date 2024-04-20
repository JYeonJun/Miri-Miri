package com.miri.userservice.domain.goods;

import com.miri.userservice.dto.goods.ResponseGoodsDto.GoodsListRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GoodsRepositoryCustom {

    Page<GoodsListRespDto> findPagingGoods(Pageable pageable);
}
