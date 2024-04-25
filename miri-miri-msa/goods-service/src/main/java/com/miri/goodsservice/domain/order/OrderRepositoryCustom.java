package com.miri.goodsservice.domain.order;

import com.miri.goodsservice.dto.order.ResponseOrderDto.OrderGoodsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {

    Page<OrderGoodsDto> findPagingOrderList(Long userId, Pageable pageable);
}