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

//    @Override
//    @Transactional
//    public CreateOrderRespDto createOrder(Long userId, CreateOrderReqDto reqDto) {
//        List<Long> wishListIds = reqDto.getWishListIds();
//
//        // 위시리스트 유효성 검사
//        List<WishListOrderedRespDto> foundWishLists = validateWishLists(userId, wishListIds);
//
//        // 주문하려는 상품이 구매 가능한 시간인지 검사하기!!
//        validateReservationTime(foundWishLists);
//
//        // 재고 감소 처리
//        Map<Long, Integer> goodsIdToOrderQuantityMap = generateGoodsIdToOrderQuantityMap(foundWishLists);
//        decreaseStocks(goodsIdToOrderQuantityMap);
//
//        // 주문 생성
//        Order order = orderRepository.save(new Order(userId));
//
//        // 주문 상세 및 배송 정보 생성
//        List<OrderGoodsRespDto> orderGoods = createOrderDetailsAndShippings(order, foundWishLists, reqDto.getAddress());
//
//        // 위시리스트 삭제 요청
//        deleteOrderedWishLists(wishListIds);
//
//        int totalOrderPrice = calculateTotalOrderPrice(foundWishLists);
//
//        return new CreateOrderRespDto(order, orderGoods, totalOrderPrice);
//    }
//
//    private List<WishListOrderedRespDto> validateWishLists(Long userId, List<Long> wishListIds) {
//        if (wishListIds.isEmpty()) {
//            throw new CustomApiException("위시리스트 ID 목록이 비어있습니다.");
//        }
//
//        List<WishListOrderedRespDto> foundWishLists = goodsServiceClient.getOrderedWishLists(
//                new WishListOrderedReqDto(userId, wishListIds));
//        if (foundWishLists == null || foundWishLists.size() != wishListIds.size()) {
//            throw new CustomApiException("유효하지 않은 위시리스트 ID가 포함되어 있습니다.");
//        }
//        return foundWishLists;
//    }
//
//    private static void validateReservationTime(List<WishListOrderedRespDto> foundWishLists) {
//        LocalDateTime now = LocalDateTime.now();
//        for (WishListOrderedRespDto wishList : foundWishLists) {
//            // 주문하려는 상품의 예약구매 시작 시간 확인
//            LocalDateTime reservationStartTime = wishList.getReservationStartTime();
//
//            // 예약구매 시작 시간이 설정되어 있고, 현재 시간이 예약 시작 시간보다 이전인 경우,
//            // 또는 예약 시작 시간이 설정되어 있지 않은 경우(즉각 구매가능한 상품) 예외 발생
//            if (reservationStartTime != null && now.isBefore(reservationStartTime)) {
//                throw new CustomApiException("상품 " + wishList.getGoodsName() + "은(는) 아직 주문할 수 있는 시간이 아닙니다.");
//            }
//        }
//    }
//
//    private Map<Long, Integer> generateGoodsIdToOrderQuantityMap(List<WishListOrderedRespDto> foundWishLists) {
//        return foundWishLists.stream()
//                .collect(Collectors.toMap(
//                        WishListOrderedRespDto::getGoodsId, // 키로 사용될 goodsId
//                        WishListOrderedRespDto::getOrderQuantity, // 값으로 사용될 orderQuantity
//                        Integer::sum));// 동일한 키에 대한 값 병합
//    }
//
//    private void decreaseStocks(Map<Long, Integer> goodsIdToOrderQuantityMap) {
//        goodsServiceClient.decreaseStock(goodsIdToOrderQuantityMap);
//    }
//
//    private List<OrderGoodsRespDto> createOrderDetailsAndShippings(Order order,
//                                                                   List<WishListOrderedRespDto> foundWishLists,
//                                                                   String address) {
//        List<OrderDetail> orderDetails = new ArrayList<>();
//        List<OrderGoodsRespDto> orderGoods = new ArrayList<>();
//        for (WishListOrderedRespDto foundWishList : foundWishLists) {
//            OrderDetail orderDetail = new OrderDetail(order, foundWishList);
//            orderDetails.add(orderDetail);
//            orderGoods.add(new OrderGoodsRespDto(orderDetail, foundWishList.getGoodsName(),
//                    foundWishList.getReservationStartTime()));
//        }
//        orderDetailRepository.saveAll(orderDetails);
//        shippingRepository.saveAll(
//                orderDetails.stream().map(od -> new Shipping(od.getId(), address)).collect(Collectors.toList()));
//        return orderGoods;
//    }
//
//    private void deleteOrderedWishLists(List<Long> wishListIds) {
//        goodsServiceClient.deleteOrderedWishLists(wishListIds);
//    }
//
//    private int calculateTotalOrderPrice(List<WishListOrderedRespDto> foundWishLists) {
//        return foundWishLists.stream()
//                .mapToInt(wish -> wish.getOrderQuantity() * wish.getUnitPrice())
//                .sum();
//    }

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
