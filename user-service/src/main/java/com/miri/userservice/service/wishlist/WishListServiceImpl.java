package com.miri.userservice.service.wishlist;

import com.miri.userservice.domain.goods.Goods;
import com.miri.userservice.domain.goods.GoodsRepository;
import com.miri.userservice.domain.wishlist.WishList;
import com.miri.userservice.domain.wishlist.WishListRepository;
import com.miri.userservice.dto.wishlist.RequestWishListDto.AddToCartReqDto;
import com.miri.userservice.dto.wishlist.ResponseWishListDto.AddToWishListRespDto;
import com.miri.userservice.dto.wishlist.ResponseWishListDto.GoodsInWishListRespDto;
import com.miri.userservice.dto.wishlist.ResponseWishListDto.WishListRespDto;
import com.miri.userservice.dto.wishlist.ResponseWishListDto.WishListUpdateRespDto;
import com.miri.userservice.handler.ex.CustomApiException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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
    public WishListRespDto getWishListGoods(Long userId) {
        // 사용자의 장바구니 목록 조회 및 최신순으로 정렬
        List<WishList> wishLists = wishListRepository.findByUserIdWithGoods(userId);
        wishLists.sort(Comparator.comparing(WishList::getCreatedDate).reversed());

        // 조회된 목록을 DTO로 변환
        List<GoodsInWishListRespDto> goodsInWishListRespDtos = convertToWishGoodsRespDtos(wishLists);
        // 전체 상품 가격 계산
        long totalPrice = calculateTotalPrice(goodsInWishListRespDtos);

        return new WishListRespDto(goodsInWishListRespDtos, goodsInWishListRespDtos.size(), totalPrice);
    }

    private List<GoodsInWishListRespDto> convertToWishGoodsRespDtos(List<WishList> wishLists) {
        List<GoodsInWishListRespDto> goodsInWishListRespDtos = new ArrayList<>();
        for (WishList wishList : wishLists) {
            goodsInWishListRespDtos.add(createWishGoodsRespDto(wishList));
        }
        return goodsInWishListRespDtos;
    }

    private GoodsInWishListRespDto createWishGoodsRespDto(WishList wishList) {
        Goods goods = wishList.getGoods();
        long subTotalPrice = (long) goods.getGoodsPrice() * wishList.getQuantity();
        return new GoodsInWishListRespDto(goods, wishList, subTotalPrice);
    }

    private long calculateTotalPrice(List<GoodsInWishListRespDto> goodsInWishListRespDtos) {
        long totalPrice = 0;
        for (GoodsInWishListRespDto dto : goodsInWishListRespDtos) {
            totalPrice += dto.getSubTotalPrice();
        }
        return totalPrice;
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
