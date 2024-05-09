package com.miri.goodsservice.service.goods;

import com.miri.coremodule.dto.goods.FeignGoodsReqDto.GoodsStockIncreaseReqDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.GoodsStockRespDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.OrderedGoodsDetailRespDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.RegisterGoodsListRespDto;
import com.miri.coremodule.dto.kafka.OrderRequestEventDto;
import com.miri.coremodule.dto.kafka.StockRollbackEventDto;
import com.miri.coremodule.handler.ex.CustomApiException;
import com.miri.coremodule.handler.ex.OrderNotAvailableException;
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
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.GoodsStockQuantityRespDto;
import com.miri.goodsservice.dto.goods.ResponseGoodsDto.UpdateRegisteredGoodsRespDto;
import com.miri.goodsservice.event.GoodsToOrderEvent;
import com.miri.goodsservice.service.kafka.KafkaSender;
import com.miri.goodsservice.service.redis.RedisStockService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class GoodsServiceImpl implements GoodsService {

    private final GoodsRepository goodsRepository;
    private final UserServiceClient userServiceClient;
    private final RedisStockService redisStockService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public GoodsServiceImpl(GoodsRepository goodsRepository, UserServiceClient userServiceClient,
                            RedisStockService redisStockService, ApplicationEventPublisher applicationEventPublisher) {
        this.goodsRepository = goodsRepository;
        this.userServiceClient = userServiceClient;
        this.redisStockService = redisStockService;
        this.applicationEventPublisher = applicationEventPublisher;
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
    public GoodsStockQuantityRespDto getGoodsStockQuantity(Long goodsId) {
        Goods findGoods = findGoodsByIdOrThrow(goodsId);
        return new GoodsStockQuantityRespDto(findGoods.getStockQuantity());
    }

    /**
     * [상품 주문] 1. 레디스에서 상품 재고 조회 - 재고가 부족하다면 재고 부족 예외 발생 - 레디스에 상품 재고 정보가 없다면 데이터베이스에서 조회 후 캐싱
     * 2. 데이터베이스로부터 상품 조회 구매 가능한 시간인지 검사 - 구매가 불가능한 시간이라면 예외 발생
     * 3. 레디스 상품 재고 감소
     * 4. 데이터베이스 상품 재고 감소
     */
    @Override
    @Transactional
    public void processOrderForGoods(Long userId, OrderGoodsReqDto reqDto) {
        Long goodsId = reqDto.getGoodsId();
        Integer quantity = reqDto.getQuantity();
        Goods goods = validatedGoods(goodsId, quantity);
        reduceStocks(goods, quantity);
        applicationEventPublisher.publishEvent(new GoodsToOrderEvent(this, userId, reqDto, goods.getGoodsPrice()));
    }

    private Goods validatedGoods(Long goodsId, Integer quantity) {
        Integer goodsStock = redisStockService.getGoodsStock(goodsId);
        Goods goods;

        if (goodsStock != null) { // 레디스에 캐시된 정보가 존재하는 경우
            checkStockAvailability(quantity, goodsStock);
            goods = findGoodsByIdOrThrow(goodsId); // 상품 정보가 필요한 경우에만 데이터베이스 조회
        } else { // 레디스에 캐시된 정보가 없는 경우
            goods = retrieveAndCacheGoodsStock(goodsId);
            checkStockAvailability(quantity, goods.getStockQuantity());
        }

        LocalDateTime reservationStartTime = goods.getReservationStartTime();
        if (reservationStartTime != null) {
            checkReservationTime(reservationStartTime);
        }
        return goods;
    }

    private Goods retrieveAndCacheGoodsStock(Long goodsId) { // 데이터베이스로부터 상품 조회 후 레디스에 저장
        Goods goods = findGoodsByIdOrThrow(goodsId);
        redisStockService.setGoodsStock(goodsId, goods.getStockQuantity(), 10, TimeUnit.MINUTES);
        return goods;
    }

    private void reduceStocks(Goods goods, int quantity) { // 레디스 & 데이터베이스 상품 재고 감소

        // TODO: 데이터베이스와 레디스 간 정합성 맞추기
        try{
            goods.decreaseStock(quantity);
            redisStockService.decreaseGoodsStock(goods.getId(), quantity);
        } catch (Exception e) {
            log.error("레디스 상품 재고 감소 중 오류가 발생했습니다. goodsId={}, quantity={}", goods.getId(), quantity, e);
            throw new CustomApiException("레디스 상품 재고 증가 처리 중 예외 발생");
        }
    }

    private void checkStockAvailability(int quantity, int goodsStock) {
        if (goodsStock < quantity) {
            throw new StockUnavailableException();
        }
    }

    private void checkReservationTime(LocalDateTime reservationStartTime) {
        if (LocalDateTime.now().isBefore(reservationStartTime)) {
            throw new OrderNotAvailableException();
        }
    }

    @Override
    @Transactional
    public void increaseOrderGoodsStock(Long goodsId, Integer quantity) {

        Goods goods = findGoodsByIdOrThrow(goodsId);
        goods.increaseStock(quantity);

        // 레디스에서 상품 재고 조회
        Integer redisStock = redisStockService.getGoodsStock(goodsId);

        try {
            if (redisStock == null) {
                // 레디스에 정보가 없는 경우, 데이터베이스에서 재고 정보를 가져와 레디스에 설정
                redisStockService.setGoodsStock(goodsId, goods.getStockQuantity(), 10, TimeUnit.MINUTES);
            } else {
                // 레디스에 정보가 있는 경우, 재고 증가
                redisStockService.increaseGoodsStock(goodsId, quantity);
            }
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
