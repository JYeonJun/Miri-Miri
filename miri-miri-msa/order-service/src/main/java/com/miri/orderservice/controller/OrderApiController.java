package com.miri.orderservice.controller;

import static com.miri.orderservice.dto.order.RequestOrderDto.CreateOrderReqDto;
import static com.miri.orderservice.dto.order.RequestOrderDto.ReturnOrderReqDto;

import com.miri.coremodule.dto.ResponseDto;
import com.miri.orderservice.dto.order.ResponseOrderDto.CreateOrderRespDto;
import com.miri.orderservice.dto.order.ResponseOrderDto.OrderGoodsListRespDto;
import com.miri.orderservice.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class OrderApiController {

    private static final String USER_ID_HEADER = "X-User-Id";

    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 주문 기능
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody @Valid CreateOrderReqDto reqDto,
                                         BindingResult bindingResult,
                                         @RequestHeader(USER_ID_HEADER) Long userId) {
        CreateOrderRespDto result = orderService.createOrder(userId, reqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "주문이 완료되었습니다.", result), HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrderGoodsList(@RequestHeader("X-User-Id") Long userId,
                                               @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        OrderGoodsListRespDto result
                = orderService.getOrderGoodsList(userId, pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "주문한 상품 목록 조회가 완료되었습니다.", result), HttpStatus.OK);
    }

    @PostMapping("/orders/{orderDetailId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable("orderDetailId") Long orderDetailId,
                                         @RequestHeader(USER_ID_HEADER) Long userId) {
        // 주문 취소 기능 구현
        orderService.cancelOrder(userId, orderDetailId);
        // 반품 요청 기능 구현
        return new ResponseEntity<>(new ResponseDto<>(1, "주문 취소 되었습니다.", null), HttpStatus.OK);
    }

    @PostMapping("/orders/{orderDetailId}/return")
    public ResponseEntity<?> returnOrder(@PathVariable("orderDetailId") Long orderDetailId,
                                         @RequestBody @Valid ReturnOrderReqDto reqDto,
                                         BindingResult bindingResult,
                                         @RequestHeader(USER_ID_HEADER) Long userId) {
        // 주문 취소 기능 구현
        orderService.returnOrder(userId, orderDetailId, reqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "반품 완료 되었습니다.", null), HttpStatus.OK);
    }
}
