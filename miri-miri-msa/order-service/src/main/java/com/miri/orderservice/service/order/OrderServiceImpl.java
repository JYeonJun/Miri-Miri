package com.miri.orderservice.service.order;

import com.miri.coremodule.dto.goods.FeignGoodsRespDto.OrderedGoodsDetailRespDto;
import com.miri.coremodule.dto.kafka.OrderRequestEventDto;
import com.miri.coremodule.dto.order.FeignOrderRespDto.OrderGoodsDto;
import com.miri.coremodule.dto.order.FeignOrderRespDto.OrderGoodsListRespDto;
import com.miri.coremodule.dto.wishlist.FeignWishListReqDto.WishListOrderedReqDto;
import com.miri.coremodule.dto.wishlist.FeignWishListRespDto.WishListOrderedRespDto;
import com.miri.coremodule.handler.ex.CustomApiException;
import com.miri.orderservice.client.GoodsServiceClient;
import com.miri.orderservice.domain.order.Order;
import com.miri.orderservice.domain.order.OrderDetail;
import com.miri.orderservice.domain.order.OrderDetailRepository;
import com.miri.orderservice.domain.order.OrderRepository;
import com.miri.orderservice.domain.order.OrderStatus;
import com.miri.orderservice.domain.returnrequest.ReturnRequest;
import com.miri.orderservice.domain.returnrequest.ReturnRequestRepository;
import com.miri.orderservice.domain.shipping.Shipping;
import com.miri.orderservice.domain.shipping.ShippingRepository;
import com.miri.orderservice.domain.shipping.ShippingStatus;
import com.miri.orderservice.dto.order.RequestOrderDto.CreateOrderReqDto;
import com.miri.orderservice.dto.order.RequestOrderDto.ReturnOrderReqDto;
import com.miri.orderservice.dto.order.ResponseOrderDto.CreateOrderRespDto;
import com.miri.orderservice.dto.order.ResponseOrderDto.OrderGoodsRespDto;
import com.miri.orderservice.event.CancelOrderEvent;
import com.miri.orderservice.event.ProcessOrderEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ShippingRepository shippingRepository;
    private final ReturnRequestRepository returnRequestRepository;
    private final GoodsServiceClient goodsServiceClient;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository,
                            ShippingRepository shippingRepository, ReturnRequestRepository returnRequestRepository,
                            GoodsServiceClient goodsServiceClient,
                            ApplicationEventPublisher applicationEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.shippingRepository = shippingRepository;
        this.returnRequestRepository = returnRequestRepository;
        this.goodsServiceClient = goodsServiceClient;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public OrderGoodsListRespDto getOrderGoodsList(Long userId, Pageable pageable) {
        Page<OrderGoodsDto> pagingOrderList = orderRepository.findPagingOrderList(userId, pageable);

        // 상품 정보 조회를 위한 상품 ID 추출 및 상품 정보 조회를 별도의 메소드로 분리하여 가독성 향상
        Set<Long> goodsIds = extractGoodsIds(pagingOrderList);
        Map<Long, OrderedGoodsDetailRespDto> orderedGoodsDetails = fetchOrderedGoodsDetails(goodsIds);

        // OrderGoodsDto에 상품 정보 매핑
        pagingOrderList.getContent()
                .forEach(orderGoodsDto -> mapGoodsInfoToOrderGoodsDto(orderGoodsDto, orderedGoodsDetails));

        return new OrderGoodsListRespDto(pagingOrderList);
    }

    private Set<Long> extractGoodsIds(Page<OrderGoodsDto> pagingOrderList) {
        return pagingOrderList.getContent().stream()
                .map(OrderGoodsDto::getGoodsId)
                .collect(Collectors.toSet());
    }

    private Map<Long, OrderedGoodsDetailRespDto> fetchOrderedGoodsDetails(Set<Long> goodsIds) {
        return goodsServiceClient.getOrderedGoodsDetailsAsMap(goodsIds);
    }

    private void mapGoodsInfoToOrderGoodsDto(OrderGoodsDto orderGoodsDto,
                                             Map<Long, OrderedGoodsDetailRespDto> orderedGoodsDetails) {
        OrderedGoodsDetailRespDto goodsInfo = orderedGoodsDetails.get(orderGoodsDto.getGoodsId());
        if (goodsInfo != null) {
            orderGoodsDto.setUnitPrice(goodsInfo.getGoodsPrice());
            orderGoodsDto.setSubTotalPrice(orderGoodsDto.getOrderQuantity() * goodsInfo.getGoodsPrice());
            orderGoodsDto.setCategory(goodsInfo.getCategory());
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderDetailId) {

        OrderDetail orderDetail = validateOrderDetail(userId, orderDetailId);
        Shipping shipping = findShippingByOrderDetailIdOrElseThrow(orderDetail.getId());
        validateCancelCondition(shipping);

        updateOrderStatusToCanceled(orderDetail);
        updateShippingStatusToCanceled(shipping);

        // 재고 증가 이벤트 발행
        applicationEventPublisher.publishEvent(
                new CancelOrderEvent(this, orderDetail.getOrder().getId(), orderDetail.getGoodsId(), orderDetail.getQuantity()));
    }

    private void validateCancelCondition(Shipping shipping) {
        if (shipping.getShippingStatus() != ShippingStatus.PENDING) {
            throw new CustomApiException("주문 취소가 불가능한 상태입니다.");
        }
    }

    private void updateOrderStatusToCanceled(OrderDetail findOrderDetail) {
        findOrderDetail.changeOrderStatus(OrderStatus.CANCELED);
    }

    private void updateShippingStatusToCanceled(Shipping shipping) {
        shipping.changeShippingStatus(ShippingStatus.CANCELED);
    }

    @Override
    @Transactional
    public void returnOrder(Long userId, Long orderDetailId, ReturnOrderReqDto reqDto) {
        OrderDetail findOrderDetail = validateOrderDetail(userId, orderDetailId);

        Shipping shipping = findShippingByOrderDetailIdOrElseThrow(findOrderDetail.getId());

        validateReturnCondition(shipping);

        processReturnRequest(findOrderDetail, reqDto);
    }

    private OrderDetail validateOrderDetail(Long userId, Long orderDetailId) {
        return orderDetailRepository.findByIdAndUserId(orderDetailId, userId)
                .orElseThrow(() -> new CustomApiException("유효하지 않은 주문입니다."));
    }

    private void validateReturnCondition(Shipping shipping) {
        LocalDate now = LocalDate.now();
        if (shipping.getShippingStatus() == ShippingStatus.DELIVERED
                && !now.isAfter(shipping.getLastModifiedDate().toLocalDate().plusDays(1))) {
            return;
        }
        throw new CustomApiException("반품이 불가능한 상태입니다.");
    }

    private void processReturnRequest(OrderDetail findOrderDetail, ReturnOrderReqDto reqDto) {
        returnRequestRepository.save(new ReturnRequest(findOrderDetail, reqDto.getReason()));
    }

    @Override
    @Transactional
    public void processOrder(OrderRequestEventDto orderRequestEventDto) {
        Order order = orderRepository.save(new Order(orderRequestEventDto.getUserId()));
        OrderDetail orderDetail = orderDetailRepository.save(new OrderDetail(order, orderRequestEventDto));
        shippingRepository.save(new Shipping(orderDetail.getId(), orderRequestEventDto.getAddress()));
        applicationEventPublisher.publishEvent(new ProcessOrderEvent(this, orderRequestEventDto, order.getId()));
    }

    @Override
    @Transactional
    public void updateOrderStatusOnFailure(Long orderId) {
        orderRepository.findById(orderId).orElseThrow(() -> new CustomApiException("존재하지 않는 주문입니다."));
        orderDetailRepository.updateOrderStatusByOrderId(orderId, OrderStatus.CANCELED);
        shippingRepository.updateShippingStatusByOrderDetailsOrderId(orderId, ShippingStatus.CANCELED);
    }

    private Shipping findShippingByOrderDetailIdOrElseThrow(Long orderDetailId) {
        return shippingRepository.findByOrderDetailId(orderDetailId)
                .orElseThrow(() -> new CustomApiException("고객센터 문의 부탁드립니다."));
    }
}
