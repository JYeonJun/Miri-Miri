package com.miri.userservice.service.goods;

import com.miri.userservice.domain.goods.Goods;
import com.miri.userservice.domain.goods.GoodsRepository;
import com.miri.userservice.domain.user.User;
import com.miri.userservice.domain.user.UserRepository;
import com.miri.userservice.dto.goods.RequestGoodsDto.GoodsRegistrationReqDto;
import com.miri.userservice.dto.goods.ResponseGoodsDto.GoodsDetailRespDto;
import com.miri.userservice.dto.goods.ResponseGoodsDto.GoodsListRespDto;
import com.miri.userservice.dto.goods.ResponseGoodsDto.GoodsRegistrationRespDto;
import com.miri.userservice.dto.goods.ResponseGoodsDto.RegisterGoodsListRespDto;
import com.miri.userservice.handler.ex.CustomApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class GoodsServiceImpl implements GoodsService {

    private final UserRepository userRepository;
    private final GoodsRepository goodsRepository;

    public GoodsServiceImpl(UserRepository userRepository, GoodsRepository goodsRepository) {
        this.userRepository = userRepository;
        this.goodsRepository = goodsRepository;
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
        User findUser = findUserByIdOrThrow(findGoods.getSellerId());
        return new GoodsDetailRespDto(findGoods, findUser);
    }

    @Override
    public RegisterGoodsListRespDto findRegisterGoodsList(Long userId, Pageable pageable) {
        return new RegisterGoodsListRespDto(goodsRepository.findPagingRegisterGoods(userId, pageable));
    }

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("해당 사용자가 존재하지 않습니다."));
    }

    private Goods findGoodsByIdOrThrow(Long goodsId) {
        return goodsRepository.findById(goodsId)
                .orElseThrow(() -> new CustomApiException("해당 상품이 존재하지 않습니다."));
    }
}
