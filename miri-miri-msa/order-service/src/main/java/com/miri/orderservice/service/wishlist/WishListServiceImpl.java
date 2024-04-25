package com.miri.orderservice.service.wishlist;

import com.miri.coremodule.handler.ex.CustomApiException;
import com.miri.orderservice.domain.goods.Goods;
import com.miri.orderservice.domain.goods.GoodsRepository;
import com.miri.orderservice.domain.wishlist.WishList;
import com.miri.orderservice.domain.wishlist.WishListRepository;
import com.miri.orderservice.dto.wishlist.RequestWishListDto.AddToCartReqDto;
import com.miri.orderservice.dto.wishlist.ResponseWishListDto.AddToWishListRespDto;
import com.miri.orderservice.dto.wishlist.ResponseWishListDto.GoodsInWishListRespDto;
import com.miri.orderservice.dto.wishlist.ResponseWishListDto.WishListRespDto;
import com.miri.orderservice.dto.wishlist.ResponseWishListDto.WishListUpdateRespDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class WishListServiceImpl implements WishListService {

    private final GoodsRepository goodsRepository;
    private final WishListRepository wishListRepository;

    public WishListServiceImpl(GoodsRepository goodsRepository,
                               WishListRepository wishListRepository) {
        this.goodsRepository = goodsRepository;
        this.wishListRepository = wishListRepository;
    }

    @Override
    @Transactional
    public AddToWishListRespDto addToWishList(Long userId, AddToCartReqDto addToCartReqDto) {

        // 장바구니에 같은 상품이 이미 있는지 확인 후 예외 발생
        if (wishListRepository.existsByUserIdAndGoods_Id(userId, addToCartReqDto.getGoodsId())) {
            throw new CustomApiException("장바구니에 동일한 상품이 존재합니다.");
        }

        Goods findGoods = findGoodsByIdOrThrow(addToCartReqDto.getGoodsId());

        WishList wishList
                = wishListRepository.save(new WishList(userId, findGoods, addToCartReqDto.getGoodsQuantity()));

        return new AddToWishListRespDto(findGoods, wishList);
    }

    @Override
    public WishListRespDto getWishListGoods(Long userId, Pageable pageable) {
        Page<GoodsInWishListRespDto> goodsInWishList
                = wishListRepository.findPagingGoodsInWishList(userId, pageable);

        return new WishListRespDto(goodsInWishList);
    }

    @Override
    @Transactional
    public WishListUpdateRespDto updateGoodsQuantityInWishList(
            Long userId, Long wishListId, int goodsQuantity) {

        WishList findWishList
                = wishListRepository.findByIdAndUserId(wishListId, userId)
                .orElseThrow(() -> new CustomApiException("유효하지 않은 상품 수량 변경 요청입니다."));

        findWishList.changeQuantity(goodsQuantity);

        return new WishListUpdateRespDto(findWishList.getId(), findWishList.getGoods().getId(), goodsQuantity);
    }

    @Override
    @Transactional
    public void deleteGoodsInWishList(Long userId, Long wishListId) {
        wishListRepository.deleteByIdAndUserId(wishListId, userId);

    }

    private Goods findGoodsByIdOrThrow(Long goodsId) {
        return goodsRepository.findById(goodsId)
                .orElseThrow(() -> new CustomApiException("해당 상품이 존재하지 않습니다."));
    }
}
