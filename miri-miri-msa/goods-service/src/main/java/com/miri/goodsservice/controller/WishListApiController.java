package com.miri.goodsservice.controller;

import static com.miri.goodsservice.dto.wishlist.RequestWishListDto.WishListUpdateReqDto;

import com.miri.goodsservice.dto.common.ResponseDto;
import com.miri.goodsservice.dto.wishlist.RequestWishListDto.AddToCartReqDto;
import com.miri.goodsservice.dto.wishlist.ResponseWishListDto.AddToWishListRespDto;
import com.miri.goodsservice.dto.wishlist.ResponseWishListDto.WishListRespDto;
import com.miri.goodsservice.dto.wishlist.ResponseWishListDto.WishListUpdateRespDto;
import com.miri.goodsservice.security.PrincipalDetails;
import com.miri.goodsservice.service.wishlist.WishListService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class WishListApiController {

    private final WishListService wishListService;

    public WishListApiController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @PostMapping("/wishlist")
    public ResponseEntity<?> addToWishList(@RequestBody @Valid AddToCartReqDto addToCartReqDto,
                                           BindingResult bindingResult,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {

        AddToWishListRespDto result
                = wishListService.addToWishList(principalDetails.getUser().getId(), addToCartReqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "장바구니에 상품이 추가되었습니다.", result), HttpStatus.CREATED);
    }

    @GetMapping("/wishlist")
    public ResponseEntity<?> getWishListGoods(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        WishListRespDto result
                = wishListService.getWishListGoods(principalDetails.getUser().getId(), pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "장바구니 목록 조회에 성공하였습니다.", result), HttpStatus.OK);
    }

    @PatchMapping("/wishlist/{wishListId}")
    public ResponseEntity<?> updateGoodsQuantityInWishList(@PathVariable("wishListId") Long wishListId,
                                                           @RequestBody @Valid WishListUpdateReqDto reqDto,
                                                           BindingResult bindingResult,
                                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {

        WishListUpdateRespDto result
                = wishListService.updateGoodsQuantityInWishList(
                        principalDetails.getUser().getId(), wishListId, reqDto.getGoodsQuantity());
        return new ResponseEntity<>(new ResponseDto<>(1, "장바구니 상품 수량 변경에 성공하였습니다.", result), HttpStatus.OK);
    }

    @DeleteMapping("/wishlist/{wishListId}")
    public ResponseEntity<?> deleteGoodsInWishList(@PathVariable("wishListId") Long wishListId,
                                                   @AuthenticationPrincipal PrincipalDetails principalDetails) {
        wishListService.deleteGoodsInWishList(principalDetails.getUser().getId(), wishListId);
        return new ResponseEntity<>(new ResponseDto<>(1, "장바구니 상품 삭제에 성공하였습니다.", null), HttpStatus.OK);
    }
}
