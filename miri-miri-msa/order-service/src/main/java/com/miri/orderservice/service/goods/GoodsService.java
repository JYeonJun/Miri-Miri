package com.miri.orderservice.service.goods;

import com.miri.orderservice.dto.goods.RequestGoodsDto.GoodsRegistrationReqDto;
import com.miri.orderservice.dto.goods.ResponseGoodsDto.GoodsDetailRespDto;
import com.miri.orderservice.dto.goods.ResponseGoodsDto.GoodsListRespDto;
import com.miri.orderservice.dto.goods.ResponseGoodsDto.GoodsRegistrationRespDto;
import com.miri.orderservice.dto.goods.ResponseGoodsDto.RegisterGoodsListRespDto;
import org.springframework.data.domain.Pageable;

public interface GoodsService {

    // 상품 등록
    GoodsRegistrationRespDto createGoods(Long sellerId, GoodsRegistrationReqDto goodsDto);

    // 상품 리스트 조회 (페이징)
    GoodsListRespDto findGoodsList(Pageable pageable);

    // 상품 상세 정보 조회
    GoodsDetailRespDto findGoods(Long goodsId);

    RegisterGoodsListRespDto findRegisterGoodsList(Long userId, Pageable pageable);
}
