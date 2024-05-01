package com.miri.orderservice.domain.order;

import com.miri.coremodule.dto.order.FeignOrderRespDto.OrderGoodsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {

    Page<OrderGoodsDto> findPagingOrderList(Long userId, Pageable pageable);
}
