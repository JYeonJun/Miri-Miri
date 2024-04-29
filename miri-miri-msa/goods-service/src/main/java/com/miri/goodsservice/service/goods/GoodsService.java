package com.miri.goodsservice.service.goods;

import com.miri.coremodule.dto.goods.FeignGoodsReqDto.GoodsStockDecreaseReqDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.GoodsStockDecreaseRespDto;
import com.miri.goodsservice.dto.goods.RequestGoodsDto.GoodsRegistrationReqDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsDetailRespDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsListRespDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsRegistrationRespDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.RegisterGoodsListRespDto;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

public interface GoodsService {

    // 상품 등록
    GoodsRegistrationRespDto createGoods(Long sellerId, GoodsRegistrationReqDto goodsDto);

    // 상품 리스트 조회 (페이징)
    GoodsListRespDto findGoodsList(Pageable pageable);

    // 상품 상세 정보 조회
    GoodsDetailRespDto findGoods(Long goodsId);

    RegisterGoodsListRespDto findRegisterGoodsList(Long userId, Pageable pageable);

    List<GoodsStockDecreaseRespDto> decreaseOrderedGoodsStock(Map<Long, Integer> reqDtos);
}
