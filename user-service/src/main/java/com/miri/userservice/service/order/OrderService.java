package com.miri.userservice.service.order;

import com.miri.userservice.dto.order.RequestOrderDto.CreateOrderReqDto;
import com.miri.userservice.dto.order.ResponseOrderDto.CreateOrderRespDto;

public interface OrderService {

    // 장바구니 리스트 주문하는 기능

    // 주문한 상품 리스트 조회 기능

    // 장바구니에 담은 상품 주문 기능
    CreateOrderRespDto createOrder(Long userId, CreateOrderReqDto reqDto);

    // 주문 취소 기능(주문 취소는 배송 중인 경우에만 가능)

    // 주문 취소 시 재고 복구 기능도 이 서비스에서 담당합니다.

    // 장바구니에 담은 상품들 주문 기능
}
