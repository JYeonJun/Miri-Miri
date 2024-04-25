package com.miri.goodsservice.controller;

import static com.miri.goodsservice.dto.wishlist.RequestWishListDto.WishListUpdateReqDto;

import com.miri.coremodule.dto.ResponseDto;
import com.miri.goodsservice.dto.wishlist.RequestWishListDto.AddToCartReqDto;
import com.miri.goodsservice.dto.wishlist.ResponseWishListDto.AddToWishListRespDto;
import com.miri.goodsservice.dto.wishlist.ResponseWishListDto.WishListRespDto;
import com.miri.goodsservice.dto.wishlist.ResponseWishListDto.WishListUpdateRespDto;
import com.miri.goodsservice.service.wishlist.WishListService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
                                           @RequestHeader("X-User-Id") String userId) {

        AddToWishListRespDto result
                = wishListService.addToWishList(Long.valueOf(userId), addToCartReqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "장바구니에 상품이 추가되었습니다.", result), HttpStatus.CREATED);
    }

    @GetMapping("/wishlist")
    public ResponseEntity<?> getWishListGoods(@RequestHeader("X-User-Id") String userId,
                                              @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        WishListRespDto result
                = wishListService.getWishListGoods(Long.valueOf(userId), pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "장바구니 목록 조회에 성공하였습니다.", result), HttpStatus.OK);
    }

    @PatchMapping("/wishlist/{wishListId}")
    public ResponseEntity<?> updateGoodsQuantityInWishList(@PathVariable("wishListId") Long wishListId,
                                                           @RequestBody @Valid WishListUpdateReqDto reqDto,
                                                           BindingResult bindingResult,
                                                           @RequestHeader("X-User-Id") String userId) {

        WishListUpdateRespDto result
                = wishListService.updateGoodsQuantityInWishList(
                        Long.valueOf(userId), wishListId, reqDto.getGoodsQuantity());
        return new ResponseEntity<>(new ResponseDto<>(1, "장바구니 상품 수량 변경에 성공하였습니다.", result), HttpStatus.OK);
    }

    @DeleteMapping("/wishlist/{wishListId}")
    public ResponseEntity<?> deleteGoodsInWishList(@PathVariable("wishListId") Long wishListId,
                                                   @RequestHeader("X-User-Id") String userId) {
        wishListService.deleteGoodsInWishList(Long.valueOf(userId), wishListId);
        return new ResponseEntity<>(new ResponseDto<>(1, "장바구니 상품 삭제에 성공하였습니다.", null), HttpStatus.OK);
    }
}
