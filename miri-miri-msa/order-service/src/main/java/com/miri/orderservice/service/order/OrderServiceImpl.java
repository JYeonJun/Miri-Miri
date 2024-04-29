package com.miri.orderservice.service.order;

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
import com.miri.orderservice.dto.order.RequestOrderDto.CreateOrderReqDto;
import com.miri.orderservice.dto.order.RequestOrderDto.ReturnOrderReqDto;
import com.miri.orderservice.dto.order.ResponseOrderDto.CreateOrderRespDto;
import com.miri.orderservice.dto.order.ResponseOrderDto.OrderGoodsListRespDto;
import com.miri.orderservice.dto.order.ResponseOrderDto.OrderGoodsRespDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
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
    private final CircuitBreakerFactory circuitBreakerFactory;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository,
                            ShippingRepository shippingRepository, ReturnRequestRepository returnRequestRepository,
                            GoodsServiceClient goodsServiceClient, CircuitBreakerFactory circuitBreakerFactory) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.shippingRepository = shippingRepository;
        this.returnRequestRepository = returnRequestRepository;
        this.goodsServiceClient = goodsServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    @Transactional
    public CreateOrderRespDto createOrder(Long userId, CreateOrderReqDto reqDto) {

        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");

        List<Long> wishListIds = reqDto.getWishListIds();

        // 위시리스트 유효성 검사
        List<WishListOrderedRespDto> foundWishLists = validateWishLists(userId, reqDto.getWishListIds(),
                circuitbreaker);

        // 재고 감소 처리
        Map<Long, Integer> goodsIdToOrderQuantityMap = generateGoodsIdToOrderQuantityMap(foundWishLists);
        decreaseStocks(goodsIdToOrderQuantityMap, circuitbreaker);

        // 주문 생성
        Order order = orderRepository.save(new Order(userId));

        // 주문 상세 및 배송 정보 생성
        List<OrderGoodsRespDto> orderGoods = createOrderDetailsAndShippings(order, foundWishLists, reqDto.getAddress());

        // 위시리스트 삭제 요청
        deleteOrderedWishLists(reqDto.getWishListIds(), circuitbreaker);

        int totalOrderPrice = calculateTotalOrderPrice(foundWishLists);

        return new CreateOrderRespDto(order, orderGoods, totalOrderPrice);
    }

    private List<WishListOrderedRespDto> validateWishLists(Long userId, List<Long> wishListIds,
                                                           CircuitBreaker circuitbreaker) {
        List<WishListOrderedRespDto> foundWishLists = circuitbreaker.run(
                () -> goodsServiceClient.getOrderedWishLists(new WishListOrderedReqDto(userId, wishListIds)),
                throwable -> null);
        if (foundWishLists == null || foundWishLists.size() != wishListIds.size()) {
            throw new CustomApiException("유효하지 않은 위시리스트 ID가 포함되어 있습니다.");
        }
        return foundWishLists;
    }

    private Map<Long, Integer> generateGoodsIdToOrderQuantityMap(List<WishListOrderedRespDto> foundWishLists) {
        return foundWishLists.stream()
                .collect(Collectors.toMap(
                        WishListOrderedRespDto::getGoodsId, // 키로 사용될 goodsId
                        WishListOrderedRespDto::getOrderQuantity, // 값으로 사용될 orderQuantity
                        Integer::sum));// 동일한 키에 대한 값 병합
    }

    private void decreaseStocks(Map<Long, Integer> goodsIdToOrderQuantityMap, CircuitBreaker circuitbreaker) {
        circuitbreaker.run(
                () -> goodsServiceClient.decreaseStock(goodsIdToOrderQuantityMap),
                throwable -> {
                    throw new CustomApiException("재고 감소 요청이 실패했습니다.");
                }
        );
    }

    private List<OrderGoodsRespDto> createOrderDetailsAndShippings(Order order,
                                                                   List<WishListOrderedRespDto> foundWishLists,
                                                                   String address) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        List<OrderGoodsRespDto> orderGoods = new ArrayList<>();
        for (WishListOrderedRespDto foundWishList : foundWishLists) {
            OrderDetail orderDetail = new OrderDetail(order, foundWishList);
            orderDetails.add(orderDetail);
            orderGoods.add(new OrderGoodsRespDto(orderDetail, foundWishList.getGoodsName()));
        }
        orderDetailRepository.saveAll(orderDetails);
        shippingRepository.saveAll(
                orderDetails.stream().map(od -> new Shipping(od.getId(), address)).collect(Collectors.toList()));
        return orderGoods;
    }

    private void deleteOrderedWishLists(List<Long> wishListIds, CircuitBreaker circuitbreaker) {
        try {
            goodsServiceClient.deleteOrderedWishLists(wishListIds);
        } catch (Exception e) {
            log.error(e.toString());
            // TODO: 롤백 요청
            throw new CustomApiException("위시리스트 목록 삭제에 실패하였습니다.");
        }
    }

    private int calculateTotalOrderPrice(List<WishListOrderedRespDto> foundWishLists) {
        return foundWishLists.stream()
                .mapToInt(wish -> wish.getOrderQuantity() * wish.getUnitPrice())
                .sum();
    }

    @Override
    public OrderGoodsListRespDto getOrderGoodsList(Long userId, Pageable pageable) {
        return new OrderGoodsListRespDto(orderRepository.findPagingOrderList(userId, pageable));
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderDetailId) {
        OrderDetail findOrderDetail = validateOrderDetail(userId, orderDetailId);

        validateCancelCondition(findOrderDetail);

//        Goods goods = findGoodsOrThrow(findOrderDetail);

//        restoreGoodsStock(goods, findOrderDetail);

        updateOrderStatusToCanceled(findOrderDetail);
    }

    @Override
    @Transactional
    public void returnOrder(Long userId, Long orderDetailId, ReturnOrderReqDto reqDto) {
        OrderDetail findOrderDetail = validateOrderDetail(userId, orderDetailId);

        validateReturnCondition(findOrderDetail);

        processReturnRequest(findOrderDetail, reqDto);
    }

    private void validateReturnCondition(OrderDetail findOrderDetail) {
        LocalDate now = LocalDate.now();
        if (findOrderDetail.getOrderStatus() == OrderStatus.DELIVERED
                && !now.isAfter(findOrderDetail.getLastModifiedDate().toLocalDate().plusDays(1))) {
            return;
        }
        throw new CustomApiException("반품이 불가능한 상태입니다.");
    }

    private void processReturnRequest(OrderDetail findOrderDetail, ReturnOrderReqDto reqDto) {
        findOrderDetail.changeOrderStatus(OrderStatus.RETURN_IN_PROGRESS);
        returnRequestRepository.save(new ReturnRequest(findOrderDetail, reqDto.getReason()));
    }

    private OrderDetail validateOrderDetail(Long userId, Long orderDetailId) {
        return orderDetailRepository.findByIdAndUserId(orderDetailId, userId)
                .orElseThrow(() -> new CustomApiException("유효하지 않은 주문입니다."));
    }

    private void validateCancelCondition(OrderDetail findOrderDetail) {
        if (findOrderDetail.getOrderStatus() != OrderStatus.PENDING) {
            throw new CustomApiException("주문 취소가 불가능한 상태입니다.");
        }
    }

//    private void restoreGoodsStock(Goods goods, OrderDetail findOrderDetail) {
//        goods.increaseStock(findOrderDetail.getQuantity());
//    }

    private void updateOrderStatusToCanceled(OrderDetail findOrderDetail) {
        findOrderDetail.changeOrderStatus(OrderStatus.CANCELED);
    }
}
