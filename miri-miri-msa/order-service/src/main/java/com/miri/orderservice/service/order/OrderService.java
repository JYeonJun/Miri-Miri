package com.miri.orderservice.service.order;

import com.miri.coremodule.dto.kafka.OrderRequestEventDto;
import com.miri.coremodule.dto.order.FeignOrderRespDto.OrderGoodsListRespDto;
import com.miri.orderservice.dto.order.RequestOrderDto.CreateOrderReqDto;
import com.miri.orderservice.dto.order.RequestOrderDto.ReturnOrderReqDto;
import com.miri.orderservice.dto.order.ResponseOrderDto.CreateOrderRespDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    // 주문한 상품 리스트 조회 기능
    OrderGoodsListRespDto getOrderGoodsList(Long userId, Pageable pageable);

    // 주문 취소 기능(주문 취소는 배송 중인 경우에만 가능)
    void cancelOrder(Long userId, Long orderDetailId);

    // 반품 요청
    void returnOrder(Long userId, Long orderDetailId, ReturnOrderReqDto reqDto);

    void processOrder(OrderRequestEventDto orderRequestEventDto);

    void updateOrderStatusOnFailure(Long orderId);
}
