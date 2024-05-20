package com.miri.goodsservice.service.goods;

import com.miri.coremodule.dto.goods.FeignGoodsReqDto.GoodsStockIncreaseReqDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.GoodsStockRespDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.OrderedGoodsDetailRespDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.RegisterGoodsListRespDto;
import com.miri.coremodule.dto.kafka.OrderRequestEventDto;
import com.miri.coremodule.handler.ex.CustomApiException;
import com.miri.coremodule.handler.ex.OrderNotAvailableException;
import com.miri.coremodule.handler.ex.StockNotFoundException;
import com.miri.coremodule.handler.ex.StockUnavailableException;
import com.miri.coremodule.vo.KafkaVO;
import com.miri.goodsservice.client.UserServiceClient;
import com.miri.goodsservice.domain.goods.Goods;
import com.miri.goodsservice.domain.goods.GoodsRepository;
import com.miri.goodsservice.dto.goods.RequestGoodsDto.GoodsRegistrationReqDto;
import com.miri.goodsservice.dto.goods.RequestGoodsDto.OrderGoodsReqDto;
import com.miri.goodsservice.dto.goods.RequestGoodsDto.UpdateRegisteredGoodsReqDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsDetailRespDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsListRespDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsRegistrationRespDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.UpdateRegisteredGoodsRespDto;
import com.miri.goodsservice.service.kafka.KafkaSender;
import com.miri.goodsservice.service.redis.RedisStockService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;
    private final UserServiceClient userServiceClient;
    private final RedisStockService redisStockService;
    private final KafkaSender kafkaSender;

    public GoodsServiceImpl(GoodsRepository goodsRepository, UserServiceClient userServiceClient,
                            RedisStockService redisStockService, KafkaSender kafkaSender) {
        this.goodsRepository = goodsRepository;
        this.userServiceClient = userServiceClient;
        this.redisStockService = redisStockService;
        this.kafkaSender = kafkaSender;
    }

    @Override
    @Transactional
    public GoodsRegistrationRespDto createGoods(Long sellerId, GoodsRegistrationReqDto goodsDto) {
        Goods goods = goodsRepository.save(new Goods(sellerId, goodsDto));
        redisStockService.setGoodsStock(goods.getId(), goods.getStockQuantity());
        return new GoodsRegistrationRespDto(goods);
    }

    @Override
    @Transactional(readOnly = true)
    public GoodsListRespDto findGoodsList(Pageable pageable) {
        return new GoodsListRespDto(goodsRepository.findPagingGoods(pageable));
    }

    @Override
    @Transactional(readOnly = true)
    public GoodsDetailRespDto findGoods(Long goodsId) {
        Goods findGoods = findGoodsByIdOrThrow(goodsId);
        String sellerName = getSellerName(findGoods);
        Integer goodsStock = redisStockService.getGoodsStock(goodsId);
        return new GoodsDetailRespDto(findGoods, goodsStock, sellerName);
    }

    private String getSellerName(Goods goods) {
        return userServiceClient.getUserNameById(goods.getSellerId());
    }

    @Override
    @Transactional(readOnly = true)
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
            // BusinessException 발생!! -> ignoreExceptions 속성에 추가
            responseList.add(new GoodsStockRespDto(goodsId, remainStockQuantity));
        }
        return responseList;
    }

    @Override
    @Transactional(readOnly = true)
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

    @Override
    @Transactional
    public UpdateRegisteredGoodsRespDto updateRegisteredGoods(Long userId, Long goodsId,
                                                              UpdateRegisteredGoodsReqDto reqDto) {
        Goods findGoods = goodsRepository.findByIdAndSellerId(goodsId, userId)
                .orElseThrow(() -> new CustomApiException("상품 수정 권한이 없습니다."));
        findGoods.changeGoodsInfo(reqDto);
        return new UpdateRegisteredGoodsRespDto(findGoods);
    }

    @Override
    public Integer getGoodsStockQuantity(Long goodsId) {
        Integer goodsStock = redisStockService.getGoodsStock(goodsId);
        if (goodsStock == null) {
            throw new StockNotFoundException();
        }
        return goodsStock;
    }

    @Override
    public void processOrderForGoods(Long userId, int goodsPrice, OrderGoodsReqDto reqDto) {
        Long goodsId = reqDto.getGoodsId();
        Integer quantity = reqDto.getQuantity();
        decreaseStockSafely(goodsId, quantity); // 레디스 캐시 상품 재고 감소
        sendOrderRequestToKafka(userId, goodsId, quantity, goodsPrice, reqDto.getAddress()); // 카프카 주문 이벤트 발행
    }

    private void decreaseStockSafely(Long goodsId, Integer quantity) {
        try {
            redisStockService.decreaseGoodsStockWithLua(goodsId, quantity);
        } catch (StockUnavailableException | StockNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("레디스 상품 재고 감소 중 오류가 발생했습니다. goodsId={}, quantity={}", goodsId, quantity, e);
            throw new CustomApiException("레디스 상품 재고 감소 처리 중 예외 발생");
        }
    }

    private void sendOrderRequestToKafka(Long userId, Long goodsId, Integer quantity, int goodsPrice, String address) {
        try {
            kafkaSender.sendOrderRequestEvent(KafkaVO.ORDER_REQUEST_TOPIC,
                    new OrderRequestEventDto(userId, goodsId, quantity, goodsPrice, address, UUID.randomUUID().toString()));
        } catch (Exception e) {
            log.error("카프카로 주문 요청 이벤트 발행 중 오류 발생. userId={}, goodsId={}, quantity={}",
                    userId, goodsId, quantity, e);
            increaseOrderGoodsStock(goodsId, quantity);
            throw new CustomApiException("카프카 이벤트 발행 처리 중 예외 발생");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Goods checkOrderTime(Long goodsId) {
        Goods goods = findGoodsByIdOrThrow(goodsId);
        validateReservationTime(goods.getReservationStartTime());
        return goods;
    }

    private void validateReservationTime(LocalDateTime reservationStartTime) {
        if (isReservationTimeInvalid(reservationStartTime)) {
            throw new OrderNotAvailableException();
        }
    }

    private boolean isReservationTimeInvalid(LocalDateTime reservationStartTime) {
        return reservationStartTime != null && LocalDateTime.now().isBefore(reservationStartTime);
    }

    @Override
    public void increaseOrderGoodsStock(Long goodsId, Integer quantity) {
        try {
            redisStockService.increaseGoodsStockWithLua(goodsId, quantity);
        } catch (StockNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("레디스 상품 재고 증가 중 오류가 발생했습니다. goodsId={}, quantity={}", goodsId, quantity, e);
            throw new CustomApiException("레디스 상품 재고 증가 처리 중 예외 발생");
        }
    }

    private Goods findGoodsByIdOrThrow(Long goodsId) {
        return goodsRepository.findById(goodsId)
                .orElseThrow(() -> new CustomApiException("해당 상품이 존재하지 않습니다."));
    }
}