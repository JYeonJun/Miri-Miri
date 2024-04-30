package com.miri.goodsservice.controller;

import com.miri.coremodule.dto.goods.FeignGoodsReqDto.GoodsStockIncreaseReqDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.GoodsStockRespDto;
import com.miri.coremodule.dto.goods.FeignGoodsRespDto.OrderedGoodsDetailRespDto;
import com.miri.goodsservice.service.goods.GoodsService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal")
@Slf4j
public class GoodsClientController {

    private final GoodsService goodsService;

    public GoodsClientController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    // 주문한 상품 재고 감소 요청
    @PostMapping("/goods/decrease")
    public ResponseEntity<?> decreaseOrderedGoodsStock(@RequestBody Map<Long, Integer> reqDtos) {
        List<GoodsStockRespDto> result = goodsService.decreaseOrderedGoodsStock(reqDtos);
        return ResponseEntity.ok(result);
    }

    // 주문한 상품에 대한 상품 정보 조회
    @GetMapping("/ordered-goods/details")
    public ResponseEntity<?> getOrderedGoodsDetailsAsMap(@RequestBody @RequestParam("goodsIds") Set<Long> goodsIds) {
        Map<Long, OrderedGoodsDetailRespDto> result
                = goodsService.getOrderedGoodsDetailsAsMap(goodsIds);
        return ResponseEntity.ok(result);
    }

    // 주문 취소한 상품 재고 증가 요청
    @PostMapping("/goods/increase")
    public ResponseEntity<?> increaseOrderedGoodsStock(@RequestBody GoodsStockIncreaseReqDto reqDto) {
        GoodsStockRespDto result = goodsService.increaseOrderedGoodsStock(reqDto);
        return ResponseEntity.ok(result);
    }
}
