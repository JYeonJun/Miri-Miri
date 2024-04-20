package com.miri.userservice.service.goods;

import com.miri.userservice.dto.goods.RequestGoodsDto.GoodsRegistrationReqDto;
import com.miri.userservice.dto.goods.ResponseGoodsDto.GoodsDetailRespDto;
import com.miri.userservice.dto.goods.ResponseGoodsDto.GoodsListRespDto;
import com.miri.userservice.dto.goods.ResponseGoodsDto.GoodsRegistrationRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GoodsService {

    // 상품 등록
    GoodsRegistrationRespDto createGoods(Long sellerId, GoodsRegistrationReqDto goodsDto);

    // 상품 리스트 조회 (페이징)
    Page<GoodsListRespDto> findGoodsList(Pageable pageable);

    // 상품 상세 정보 조회
    GoodsDetailRespDto findGoods(Long goodsId);
}
