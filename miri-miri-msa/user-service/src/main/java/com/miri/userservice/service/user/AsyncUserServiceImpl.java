package com.miri.userservice.service.user;

import com.miri.coremodule.dto.ResponseDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.RegisterGoodsListRespDto;
import com.miri.coremodule.dto.order.FeignOrderRespDto.OrderGoodsListRespDto;
import com.miri.coremodule.dto.wishlist.FeignWishListRespDto.WishListRespDto;
import com.miri.userservice.client.GoodsServiceClient;
import com.miri.userservice.client.OrderServiceClient;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncUserServiceImpl implements AsyncUserService {
    private final GoodsServiceClient goodsServiceClient;
    private final OrderServiceClient orderServiceClient;

    public AsyncUserServiceImpl(GoodsServiceClient goodsServiceClient, OrderServiceClient orderServiceClient) {
        this.goodsServiceClient = goodsServiceClient;
        this.orderServiceClient = orderServiceClient;
    }

    @Async
    @Override
    public CompletableFuture<RegisterGoodsListRespDto> getRegisteredGoodsListAsync(Long userId) {
        return CompletableFuture.supplyAsync(() ->
                fetchServiceData(RegisterGoodsListRespDto.class, userId, goodsServiceClient::getRegisteredGoodsList));
    }

    @Async
    @Override
    public CompletableFuture<WishListRespDto> getWishListGoodsAsync(Long userId) {
        return CompletableFuture.supplyAsync(() ->
                fetchServiceData(WishListRespDto.class, userId, goodsServiceClient::getWishListGoods));
    }

    @Async
    @Override
    public CompletableFuture<OrderGoodsListRespDto> getOrderGoodsListAsync(Long userId) {
        return CompletableFuture.supplyAsync(() ->
                fetchServiceData(OrderGoodsListRespDto.class, userId, orderServiceClient::getOrderGoodsList));
    }

    private <T> T fetchServiceData(Class<T> clazz, Long userId, BiFunction<String, Integer, ResponseDto<T>> serviceMethod) {
        ResponseDto<T> response = serviceMethod.apply(String.valueOf(userId), 0);
        return Optional.ofNullable(response).map(ResponseDto::getData).orElse(null);
    }
}

