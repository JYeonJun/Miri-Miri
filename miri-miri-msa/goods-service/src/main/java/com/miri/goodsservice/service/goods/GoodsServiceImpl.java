package com.miri.goodsservice.service.goods;

import com.miri.coremodule.dto.goods.FeignGoodsReqDto.GoodsStockIncreaseReqDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.GoodsStockRespDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.OrderedGoodsDetailRespDto;
import com.miri.coremodule.handler.ex.CustomApiException;
import com.miri.goodsservice.client.UserServiceClient;
import com.miri.goodsservice.domain.goods.Goods;
import com.miri.goodsservice.domain.goods.GoodsRepository;
import com.miri.goodsservice.dto.goods.RequestGoodsDto.GoodsRegistrationReqDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsDetailRespDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsListRespDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsRegistrationRespDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.RegisterGoodsListRespDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;
    private final UserServiceClient userServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public GoodsServiceImpl(GoodsRepository goodsRepository, UserServiceClient userServiceClient,
                            CircuitBreakerFactory circuitBreakerFactory) {
        this.goodsRepository = goodsRepository;
        this.userServiceClient = userServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    @Transactional
    public GoodsRegistrationRespDto createGoods(Long sellerId, GoodsRegistrationReqDto goodsDto) {
        Goods goods = goodsRepository.save(new Goods(sellerId, goodsDto));
        return new GoodsRegistrationRespDto(goods);
    }

    @Override
    public GoodsListRespDto findGoodsList(Pageable pageable) {
        return new GoodsListRespDto(goodsRepository.findPagingGoods(pageable));
    }

    @Override
    public GoodsDetailRespDto findGoods(Long goodsId) {
        Goods findGoods = findGoodsByIdOrThrow(goodsId);
        String sellerName = getSellerName(findGoods);
        return new GoodsDetailRespDto(findGoods, sellerName);
    }

    private String getSellerName(Goods goods) {
        return userServiceClient.getUserNameById(goods.getSellerId());
    }

    @Override
    public RegisterGoodsListRespDto findRegisterGoodsList(Long userId, Pageable pageable) {
        return new RegisterGoodsListRespDto(goodsRepository.findPagingRegisterGoods(userId, pageable));
    }

    @Override
    @Transactional
    public List<GoodsStockRespDto> decreaseOrderedGoodsStock(Map<Long, Integer> reqDtos) {
        List<GoodsStockRespDto> responseList = new ArrayList<>();
        List<Long> goodsIds = reqDtos.keySet().stream().toList();

        List<Goods> goodsList = goodsRepository.findAllById(goodsIds);
        for (Goods goods : goodsList) {
            Long goodsId = goods.getId();
            int remainStockQuantity = goods.decreaseStock(reqDtos.get(goodsId));
            // TODO: 재고 부족은 추후 처리 예정
            // BusinessException 발생!! -> ignoreExceptions 속성에 추가
            responseList.add(new GoodsStockRespDto(goodsId, remainStockQuantity));
        }
        return responseList;
    }

    @Override
    @Transactional
    public GoodsStockRespDto increaseOrderedGoodsStock(GoodsStockIncreaseReqDto reqDto) {
        Goods findGoods = findGoodsByIdOrThrow(reqDto.getGoodsId());
        findGoods.increaseStock(reqDto.getQuantity());
        return new GoodsStockRespDto(reqDto.getGoodsId(), findGoods.getStockQuantity());
    }

    @Override
    public Map<Long, OrderedGoodsDetailRespDto> getOrderedGoodsDetailsAsMap(Set<Long> goodsIds) {
        List<Goods> goodsList = goodsRepository.findByIdIn(goodsIds);
        return goodsList.stream()
                .collect(Collectors.toMap(
                        Goods::getId,
                        goods -> new OrderedGoodsDetailRespDto(
                                goods.getId(),
                                goods.getGoodsPrice(),
                                goods.getCategory().getValue()
                        )
                ));
    }

    private Goods findGoodsByIdOrThrow(Long goodsId) {
        return goodsRepository.findById(goodsId)
                .orElseThrow(() -> new CustomApiException("해당 상품이 존재하지 않습니다."));
    }
}
