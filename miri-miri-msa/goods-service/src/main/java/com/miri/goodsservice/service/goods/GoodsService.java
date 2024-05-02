package com.miri.goodsservice.service.goods;

import com.miri.coremodule.dto.goods.FeignGoodsReqDto.GoodsStockIncreaseReqDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.GoodsStockRespDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.OrderedGoodsDetailRespDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.RegisterGoodsListRespDto;
import com.miri.goodsservice.dto.goods.RequestGoodsDto.GoodsRegistrationReqDto;
import com.miri.goodsservice.dto.goods.RequestGoodsDto.UpdateRegisteredGoodsReqDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsDetailRespDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsListRespDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsRegistrationRespDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsStockQuantityRespDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.UpdateRegisteredGoodsRespDto;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface GoodsService {

    // 상품 등록
    GoodsRegistrationRespDto createGoods(Long sellerId, GoodsRegistrationReqDto goodsDto);

    // 상품 리스트 조회 (페이징)
    GoodsListRespDto findGoodsList(Pageable pageable);

    // 상품 상세 정보 조회
    GoodsDetailRespDto findGoods(Long goodsId);

    RegisterGoodsListRespDto findRegisterGoodsList(Long userId, Pageable pageable);

    List<GoodsStockRespDto> decreaseOrderedGoodsStock(Map<Long, Integer> reqDtos);

    GoodsStockRespDto increaseOrderedGoodsStock(GoodsStockIncreaseReqDto reqDto);

    Map<Long, OrderedGoodsDetailRespDto> getOrderedGoodsDetailsAsMap(Set<Long> goodsIds);

    UpdateRegisteredGoodsRespDto updateRegisteredGoods(Long userId, Long goodsId, UpdateRegisteredGoodsReqDto reqDto);

    GoodsStockQuantityRespDto getGoodsStockQuantity(Long goodsId);
}
