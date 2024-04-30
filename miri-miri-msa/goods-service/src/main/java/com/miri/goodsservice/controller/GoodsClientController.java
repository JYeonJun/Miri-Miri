package com.miri.goodsservice.controller;

import com.miri.coremodule.dto.goods.FeignGoodsRespDto.GoodsStockDecreaseRespDto;
import com.miri.goodsservice.service.goods.GoodsService;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        List<GoodsStockDecreaseRespDto> result = goodsService.decreaseOrderedGoodsStock(reqDtos);
        return ResponseEntity.ok(result);
    }
}
