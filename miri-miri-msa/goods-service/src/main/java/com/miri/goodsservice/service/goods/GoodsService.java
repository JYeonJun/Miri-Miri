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

    // 등록상 상품 목록 조회
    RegisterGoodsListRespDto findRegisterGoodsList(Long userId, Pageable pageable);

    // 상품 재고 감소
    List<GoodsStockRespDto> decreaseOrderedGoodsStock(Map<Long, Integer> reqDtos);

    // 상품 재고 증가
    GoodsStockRespDto increaseOrderedGoodsStock(GoodsStockIncreaseReqDto reqDto);

    // 주문한 상품에 대한 상품 정보 조회
    Map<Long, OrderedGoodsDetailRespDto> getOrderedGoodsDetailsAsMap(Set<Long> goodsIds);

    // 상품 정보 수정
    UpdateRegisteredGoodsRespDto updateRegisteredGoods(Long userId, Long goodsId, UpdateRegisteredGoodsReqDto reqDto);

    // 상품 재고 조회
    GoodsStockQuantityRespDto getGoodsStockQuantity(Long goodsId);

    // 예약 구매 상품에 대한 재고 감소
    void processOrderForGoods(Long userId, Long goodsId, Integer quantity);
}
