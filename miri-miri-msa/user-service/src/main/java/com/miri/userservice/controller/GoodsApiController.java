package com.miri.userservice.controller;

import com.miri.coremodule.dto.ResponseDto;
import com.miri.userservice.dto.goods.RequestGoodsDto.GoodsRegistrationReqDto;
import com.miri.userservice.dto.goods.ResponseGoodsDto.GoodsDetailRespDto;
import com.miri.userservice.dto.goods.ResponseGoodsDto.GoodsListRespDto;
import com.miri.userservice.dto.goods.ResponseGoodsDto.GoodsRegistrationRespDto;
import com.miri.userservice.dto.goods.ResponseGoodsDto.RegisterGoodsListRespDto;
import com.miri.userservice.security.PrincipalDetails;
import com.miri.userservice.service.goods.GoodsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class GoodsApiController {

    private final GoodsService goodsService;

    public GoodsApiController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @PostMapping("/auth/goods")
    public ResponseEntity<?> registerGoods(@RequestBody @Valid GoodsRegistrationReqDto goodsRegistrationReqDto,
                                           BindingResult bindingResult,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        GoodsRegistrationRespDto result
                = goodsService.createGoods(principalDetails.getUser().getId(), goodsRegistrationReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "상품 등록에 성공했습니다.", result), HttpStatus.CREATED);
    }

    @GetMapping("/goods")
    public ResponseEntity<?> getGoodsList(
            @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        GoodsListRespDto result = goodsService.findGoodsList(pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "상품 목록 조회에 성공했습니다.", result), HttpStatus.OK);
    }

    @GetMapping("/goods/{id}")
    public ResponseEntity<?> getGoodsDetail(@PathVariable("id") Long goodsId) {
        GoodsDetailRespDto result = goodsService.findGoods(goodsId);
        return new ResponseEntity<>(new ResponseDto<>(1, "상품 상세 조회에 성공했습니다.", result), HttpStatus.OK);
    }

    @GetMapping("/auth/my/goods")
    public ResponseEntity<?> getRegisterGoodsList(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                  @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        RegisterGoodsListRespDto result
                = goodsService.findRegisterGoodsList(principalDetails.getUser().getId(), pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "등록한 상품 목록 조회에 성공하였습니다.", result), HttpStatus.OK);
    }

    // TODO: 상품 정보 수정!!
}